import React, { useEffect, useState } from "react";
import PropTypes from 'prop-types';
import { makeStyles } from '@material-ui/core/styles';
import DialogTitle from '@material-ui/core/DialogTitle';
import Dialog from '@material-ui/core/Dialog';
import Button from '@material-ui/core/Button';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import InternshipOfferService from "../services/InternshipOfferService";
import StudentApplicationList from "./StudentApplicationList";


import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Divider from '@material-ui/core/Divider';
import { useHistory } from 'react-router-dom';
import EditIcon from '@material-ui/icons/Edit';
import IconButton from '@material-ui/core/IconButton';



//tables values
const useStyles = makeStyles({
  table: {
    minWidth: 600,
  },
});

// Dialog


function StudentApplicationDialogProps(props) {
  
  const { onClose, selectedValue, open } = props;

  const handleClose = () => {
    onClose(selectedValue);
  };

  return (
    <Dialog onClose={handleClose} aria-labelledby="simple-dialog-title" open={open}>
      <StudentApplicationList onClose={handleClose} selectedValue={selectedValue} />
    </Dialog>
  );

}

StudentApplicationDialogProps.propTypes = {
  onClose: PropTypes.func.isRequired,
  open: PropTypes.bool.isRequired,
  selectedValue: PropTypes.string.isRequired,
};

export default function StudentSelection(props) {

  const [open, setOpen] = React.useState(false);
  const [rows, setRows] = useState([]);
  const [currentInternshipId, setCurrentInternshipId] = useState('');
  const history = useHistory();

  const classes = useStyles();

  const uniqueId = props.employerId ? props.employerId : localStorage.getItem('UserUniqueId');


  const handleClickOpen = (internshipId) => {
    setCurrentInternshipId(internshipId)
    setOpen(true);
  };

  const handleClose = (value) => {
    setOpen(false);
  };

  const fetchInternshipOffers = async () => {
    const response = await InternshipOfferService.findAllByEmployer(uniqueId);
    setRows(response.data.internshipOffers);
  }

  useEffect(() => { fetchInternshipOffers(); }, [])


  return (
    <div>
      <Container>
        <Box
          mb={2}
          paddingTop={2}
          textAlign="left"
        >
          <Typography component="h1" variant="h4" align="center">Mes offres de stage</Typography>
        </Box>
      </Container>

      <TableContainer>
        <Table className={classes.table} aria-label="a dense table">
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
                    {!props.employerId && <Button 
                      variant="contained" 
                      color="primary" 
                      onClick={() => history.push(`select-action/${offer.uniqueId}`)}>
                        Voir Applications
                      </Button>}
                      {props.employerId && <Button 
                      variant="contained" 
                      color="primary" 
                      onClick={() => history.push(`../../select-action/${offer.uniqueId}`)}>
                        Voir Applications
                      </Button>}
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

            <StudentApplicationDialogProps open={open} onClose={handleClose} selectedValue={currentInternshipId} />
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
}