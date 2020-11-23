import React, { useEffect, useState } from 'react';
import { useSnackbar } from 'notistack';

import Button from '@material-ui/core/Button';
import ListItemText from '@material-ui/core/ListItemText';
import DialogTitle from '@material-ui/core/DialogTitle';
import Dialog from '@material-ui/core/Dialog';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import { Box, ListItem } from "@material-ui/core";
import InternshipOfferService from '../services/InternshipOfferService';
import InternshipApplicationService from '../services/InternshipApplicationService';
import PortfolioService from '../services/PortfolioService';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import List from '@material-ui/core/List';
import Divider from '@material-ui/core/Divider';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import Checkbox from '@material-ui/core/Checkbox';

//tables values
const useStyles = makeStyles({
  table: {
    minWidth: 600,
  }
});

//other class

export default function InternshipApplicationCreationForm() {

  const [open, setOpen] = React.useState(false);
  const [rows, setRows] = useState([]);
  const [checked, setChecked] = React.useState([]);
  const [documents, setDocuments] = React.useState([]);
  const [offerUniqueId, setOfferUniqueId] = React.useState();

  const classes = useStyles();

  const { enqueueSnackbar } = useSnackbar();

  const onApply = () => {

    let docs = checked.map(doc => doc.uniqueId);

    const request = {
      offerUniqueId: offerUniqueId,
      documents: docs,
    }

    InternshipApplicationService.createInternshipApplication(request).then(response => {
      fetchInternshipOffers();
      enqueueSnackbar("Application envoyée",  { variant: 'success' });
    })

    setOpen(false);
  }

  const handleClickOpen = (internshipId, index) => {

    setOfferUniqueId(internshipId);

    let uuid = localStorage.getItem("UserUniqueId");

    PortfolioService.approved(uuid).then(response => {
      setDocuments(response.data.portfolioDocuments);
      setOpen(true);
    });
  };

  const handleToggle = (value) => () => {
    const currentIndex = checked.indexOf(value);
    const newChecked = [...checked];

    if (currentIndex === -1) {
      newChecked.push(value);
    } else {
      newChecked.splice(currentIndex, 1);
    }

    setChecked(newChecked);
  };

  const handleClose = (value) => {
    setOpen(false);
  };

  const fetchInternshipOffers = async () => {

    let uuid = localStorage.getItem("UserUniqueId");

    const response1 = await InternshipOfferService.accessible(uuid);
    const response2 = await InternshipApplicationService.internshipApplications(uuid);

    const offers = response1.data.internshipOffers.filter(offer =>
      !response2.data.applications.some(app => app.offerUniqueId == offer.uniqueId));

    setRows(offers);
  }

  useEffect(() => {
    fetchInternshipOffers();
  }, [])

  return (
    <div>
      <Container>
        <Box
          mb={2}
          paddingTop={2}
          textAlign="center">

          <Typography component="h1" variant="h4">Offres de stages</Typography>
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
                        onClick={() => handleClickOpen(offer.uniqueId, index)}>
                        Appliquer
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
      <Dialog
        open={open}
        onClose={handleClose}
      >
        <DialogTitle>
          Select documents to send
        </DialogTitle>
        <DialogContent>
          <List className={classes.root}>
            {documents.length > 0 && documents.map((value) => {

              const labelId = `checkbox-list-label-${value}`;

              return (
                <ListItem key={value} role={undefined} dense button onClick={handleToggle(value)}>
                  <ListItemIcon>
                    <Checkbox
                      edge="start"
                      checked={checked.indexOf(value) !== -1}
                      tabIndex={-1}
                      disableRipple
                      inputProps={{ 'aria-labelledby': labelId }}
                    />
                  </ListItemIcon>
                  <ListItemText id={labelId} primary={value.fileName} />
                </ListItem>
              );
            })}
            {documents.length == 0 &&
              <ListItem key={1}>
                <ListItemText primary={'No documents!'} />
              </ListItem>
            }
          </List>
        </DialogContent>
        <DialogActions>
          <Button autoFocus onClick={handleClose} color="primary">
            Cancel
          </Button>
          <Button onClick={onApply} color="primary">
            Apply
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
