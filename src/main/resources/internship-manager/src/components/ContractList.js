import React, { useEffect, useState } from 'react';
import { useHistory, useParams } from 'react-router-dom';
import { useSnackbar } from 'notistack';
import SignatureService from "../services/SignatureService";
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import Button from '@material-ui/core/Button';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import { Box } from "@material-ui/core";
import CloudDownloadIcon from '@material-ui/icons/CloudDownload';
import ContractService from '../services/ContractService';
import CreateIcon from '@material-ui/icons/Create';
import { saveAs } from 'file-saver';

const useStyles = makeStyles({
    table: {
        minWidth: 600,
    },
});


export default function ContractList() {

    const classes = useStyles();
    const history = useHistory()

    const { enqueueSnackbar } = useSnackbar();

    const [rows, setRows] = useState([]);

    const generateContract = (contractId) => {
        ContractService.generate(contractId).then(response => {
            saveAs(new Blob([response.data], { type: response.headers['content-type'] }), 'contrat.pdf');
            enqueueSnackbar("Contrat téléchargé",  { variant: 'info' });
        });
    }

    const signContract = (contractId) => {
        let uuid = localStorage.getItem("UserUniqueId");

        SignatureService.find(uuid).then(data => {

            if (data.data) {

                ContractService.sign(contractId).then(() => 
                {
                    fetchAllAwaitingContracts();
                    enqueueSnackbar("Contrat Signé",  { variant: 'success' });
                })

            }else{
                enqueueSnackbar("Vous n'avez pas de signature!",  { variant: 'error' });
            }

        });
        
    }

    const fetchAllAwaitingContracts = async () => {
        const response = await ContractService.awaitingSignature(localStorage.getItem('UserUniqueId'));
        console.log(response.data.contracts);
        setRows(response.data.contracts);
    }

    useEffect(() => { fetchAllAwaitingContracts(); }, [])

    return (
        <div>
            <Container>
                <Box
                    mb={2}
                    paddingTop={2}
                    textAlign="center">

                    <Typography component="h1" variant="h4">Contrats</Typography>
                </Box>
            </Container>

            <TableContainer>
                <Table className={classes.table} aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            <TableCell align="center"><strong>Étudiant</strong></TableCell>
                            <TableCell align="center"><strong>Détails</strong></TableCell>
                            <TableCell align="center"><strong>Actions</strong></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {rows.map((contract, index) => (
                            <TableRow  
                                key={index} 
                                button
                                onClick={() => history.push(`/contract/${contract.uniqueId}`)}>

                                <TableCell style={{ verticalAlign: 'top' }} align="center">{contract.application.studentFirstName + " " + contract.application.studentLastName} </TableCell>
                                <TableCell component="th" scope="row" style={{ verticalAlign: 'top' }} align="left">
                                    <p><strong>Compagnie: </strong>{contract.application.company}</p>
                                    <p><strong>Titre de poste: </strong>{contract.application.jobTitle}</p>
                                    <p><strong>Date: </strong>{new Date(contract.creationDate).toDateString()}</p>
                                </TableCell>
                                <TableCell align="center" style={{ verticalAlign: 'top' }}>
                                    <div style={{ marginBottom: '10px' }}>
                                        <Button
                                            variant="contained" color="secondary"
                                            size="small"
                                            startIcon={<CreateIcon />}
                                            onClick={() => signContract(contract.uniqueId)}
                                        >
                                            Signer
                                        </Button>
                                    </div>
                                    <div>

                                        <Button
                                            variant="contained" color="secondary"
                                            size="small"
                                            startIcon={<CloudDownloadIcon />}
                                            onClick={() => generateContract(contract.uniqueId)}
                                        >
                                            Télécharger
                                        </Button>
                                    </div>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    )
}