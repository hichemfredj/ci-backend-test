import React, { useEffect, useState } from "react";
import { useHistory, useParams } from 'react-router-dom';
import { useSnackbar } from 'notistack';

import ContractService from '../services/ContractService'
import SignatureService from "../services/SignatureService";

import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import Box from '@material-ui/core/Box';
import Button from '@material-ui/core/Button';
import IconButton from '@material-ui/core/IconButton';
import LinearProgress from '@material-ui/core/LinearProgress';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';

import KeyboardBackspaceIcon from '@material-ui/icons/KeyboardBackspace';

export default function StudentProfile() {

    const { uuid } = useParams();
    const history = useHistory()

    const { enqueueSnackbar } = useSnackbar();

    const [contract, setContract] = useState({});
    const [application, setApplication] = useState({});

    const [canSign, setCanSign] = useState(false);
    const [pdfUrl, setPdfUrl] = useState(null);

    const fetchContract = () => {
        ContractService.find(uuid).then(response => {

            setContract(response.data);
            setApplication(response.data.application);

            if (localStorage.getItem("UserUniqueId") == contract.currentUserUniqueId) setCanSign(true);
        });

        ContractService.generate(uuid).then(response => {

            let blob = new Blob([response.data], { type: response.headers['content-type'] });

            setPdfUrl(URL.createObjectURL(blob));
        });
    }

    const sign = () => {

        let userUniqueId = localStorage.getItem("UserUniqueId");

        SignatureService.find(userUniqueId).then(data => {

            if (data.data) {

                ContractService.sign(uuid).then(() => {
                    fetchContract();
                    enqueueSnackbar("Contrat Signé", { variant: 'success' });
                });

            } else {
                enqueueSnackbar("Vous n'avez pas de signature!", { variant: 'error' });
            }

        });
    }

    const translateSession = (session) => {

        if (session) {

            let translated = "";

            let split = session.split('-');

            switch (split[0]) {
                case "AUTUMN": translated += "Automne"; break;
                case "WINTER": translated += "Hiver"; break;
                case "SUMMER": translated += "Été"; break;
            }

            translated += " " + split[1];

            return translated
        }
    }

    const formattedStatus = () => {

        switch (contract.status) {
            case "STUDENT": return "En attente de la signature de l'étudiant";
            case "EMPLOYER": return "En attente de la signature de l'employeur";
            case "ADMINISTRATOR": return "En attente de la signature du gestionaire de stage";
            case "COMPLEATED": return "Contrat complet!";
        }
    }

    const progress = () => {
        switch (contract.status) {
            case "STUDENT": return 25;
            case "EMPLOYER": return 50;
            case "ADMINISTRATOR": return 75;
            case "COMPLEATED": return 100;
        }
    }

    useEffect(() => { fetchContract(); }, [])

    return (
        <div>
            <IconButton
                onClick={() => history.goBack()}>
                <KeyboardBackspaceIcon />
                <Typography>Retour</Typography>
            </IconButton>

            <Container>
                <Box
                    margin={2}
                    textAlign="center">

                    <Typography variant="h3">Contrat</Typography>
                    <Typography
                        style={{color: "gray"}}
                        variant="subtitle1">{contract.uniqueId}</Typography>
                </Box>

                <Box>

                    <Table size="small">

                        <TableHead>
                            <TableCell width="30%"></TableCell>
                            <TableCell width="70%"></TableCell>
                        </TableHead>

                        <TableRow>
                            <TableCell align="left"><strong>Session</strong></TableCell>
                            <TableCell align="right">{translateSession(contract.semester)}</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell align="left"><strong>Company</strong></TableCell>
                            <TableCell align="right">{application.company}</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell align="left"><strong>Titre de poste</strong></TableCell>
                            <TableCell align="right">{application.jobTitle}</TableCell>
                        </TableRow>
                    </Table>

                </Box>

                <Box
                    marginTop={2}
                    textAlign="center">
                    <Box
                        marginBottom={1}
                        style={{backgroundColor: "lightsteelblue"}}>
                        <Typography>{formattedStatus()}</Typography>
                    </Box>
                    <LinearProgress variant="determinate" value={progress()} />
                </Box>
                

                <Box
                    marginTop={2}
                    border="1px solid black">
                    <iframe src={pdfUrl} width="100%" height="800px">
                    </iframe>
                </Box>

                <Box
                    margin={2}
                    paddingBottom={2}
                >
                    {
                        canSign && <Button
                            variant="contained" color="secondary"
                            size="small"
                            onClick={sign}
                        >
                            Signer
                        </Button>
                    }
                </Box>

            </Container>
        </div>
    )
}