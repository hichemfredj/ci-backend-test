import React, { useEffect, useState } from 'react';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import { Box } from "@material-ui/core";
import InternshipOfferService from '../services/InternshipOfferService';
import Button from '@material-ui/core/Button';
import Divider from '@material-ui/core/Divider';
import { useHistory } from 'react-router-dom';


const useStyles = makeStyles({
  table: {
    minWidth: 600,
  },
});

export default function StudentInternshipValidationTable() {

  const history = useHistory();
  const classes = useStyles();

  const [rows, setRows] = useState([]);

  const fetchInternshipOffers = async () => {
    const response = await InternshipOfferService.approved();
    setRows(response.data.internshipOffers);
  }

  useEffect(() => { fetchInternshipOffers(); }, [])

  return (
    <div>
      <Container>
        <Box
          mb={2}
          paddingTop={2}
          textAlign="center">

          <Typography component="h1" variant="h4">Accès aux offres</Typography>
        </Box>
      </Container>

      <TableContainer>
        <Table className={classes.table} aria-label="simple table">
          <TableHead>
            <TableRow>
              <TableCell width="40%" align="center"><strong>Poste</strong></TableCell>
              <TableCell width="60%" align="center"><strong>Détails</strong></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {rows.map((offer, index) => (
              <TableRow key={index}>
                <TableCell component="th" scope="row" style={{ verticalAlign: 'top' }} align="left">
                  <p><strong>Compagnie: </strong>{offer.company}</p>
                  <p><strong>Titre: </strong>{offer.jobTitle}</p>
                  <Box
                    style={{ backgroundColor: 'lightgray' }}
                    padding={1}>
                    <Typography>Actions</Typography>
                    <Divider></Divider>
                    <Box margin={1}>
                      <Button
                        variant="contained"
                        color="primary"
                        onClick={() => history.push(`/manage-access/${offer.uniqueId}`)}>
                        Gérer l'accès 
                      </Button>
                    </Box>
                  </Box>
                </TableCell>
                <TableCell component="th" scope="row" style={{ verticalAlign: 'top' }} align="left">
                  <p><strong>Début: </strong>{new Date(offer.startDate).toLocaleDateString()} <strong>Fin: </strong>{new Date(offer.endDate).toLocaleDateString()}</p>
                  <p><strong>Lieu du stage: </strong>{offer.location}</p>
                  <p><strong>Salaires: </strong>{offer.salary.toFixed(2) + '$'} <strong>Heures: </strong>{offer.hours}</p>
                  <p><strong>Portée de travail: </strong>{offer.jobScope.map(scope => (<li style={{}}>{scope}</li>))}</p>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>

        </Table>
      </TableContainer>
    </div>
  );
}