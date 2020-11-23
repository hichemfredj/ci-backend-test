import React, { useEffect, useState } from 'react';
import { fade, makeStyles } from '@material-ui/core/styles';
import { useHistory } from 'react-router-dom';
import { useParams } from "react-router-dom";
import { useSnackbar } from 'notistack';
import InternshipOfferService from '../services/InternshipOfferService';
import UserService from '../services/UserService';
import KeyboardBackspaceIcon from '@material-ui/icons/KeyboardBackspace';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemAvatar from '@material-ui/core/ListItemAvatar';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemSecondaryAction from '@material-ui/core/ListItemSecondaryAction';
import ListItemText from '@material-ui/core/ListItemText';
import Avatar from '@material-ui/core/Avatar';
import IconButton from '@material-ui/core/IconButton';
import FormGroup from '@material-ui/core/FormGroup';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Dialog from '@material-ui/core/Dialog';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import ListSubheader from '@material-ui/core/ListSubheader';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import Box from '@material-ui/core/Box';
import PropTypes from 'prop-types';
import { Container, Divider } from '@material-ui/core';
import AttachmentIcon from '@material-ui/icons/Attachment';
import StudentDocumentsList from "./StudentDocumentsList";
import InternshipApplicationService from '../services/InternshipApplicationService';
import ContractService from "../services/ContractService";
import ThumbUpAltOutlinedIcon from '@material-ui/icons/ThumbUpAltOutlined';
import ThumbDownAltOutlinedIcon from '@material-ui/icons/ThumbDownAltOutlined';
import { saveAs } from 'file-saver';
import PageviewIcon from '@material-ui/icons/Pageview';

const useStyles = makeStyles((theme) => ({
  root: {
    width: 300,
    backgroundColor: 'lightgray',
  },
  nested: {
    paddingLeft: theme.spacing(4),
  },
  search: {
    position: 'relative',
    borderRadius: theme.shape.borderRadius,
    backgroundColor: fade(theme.palette.common.white, 0.15),
    '&:hover': {
      backgroundColor: fade(theme.palette.common.white, 0.25),
    },
    marginRight: theme.spacing(2),
    marginLeft: 0,
    width: '100%',
    [theme.breakpoints.up('sm')]: {
      marginLeft: theme.spacing(3),
      width: 'auto',
    },
  },
  searchIcon: {
    padding: theme.spacing(0, 2),
    height: '100%',
    position: 'absolute',
    pointerEvents: 'none',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
  inputRoot: {
    color: 'inherit',
  },
  inputInput: {
    padding: theme.spacing(1, 1, 1, 0),
    paddingLeft: `calc(1em + ${theme.spacing(4)}px)`,
    transition: theme.transitions.create('width'),
    width: '100%',
    [theme.breakpoints.up('md')]: {
      width: '20ch',
    },
  },
}));

function StudentPortfolioDetailsDialogProps(props) {

  const { onClose, selectedValue, open } = props;
  
  const handleClose = () => {
    onClose(selectedValue);
  };

  return (
    <Dialog onClose={handleClose} aria-labelledby="simple-dialog-title" open={open}>
      <StudentDocumentsList onClose={handleClose} selectedValue={selectedValue} />
    </Dialog>
  );

}

StudentPortfolioDetailsDialogProps.propTypes = {
  onClose: PropTypes.func.isRequired,
  open: PropTypes.bool.isRequired,
  selectedValue: PropTypes.string.isRequired,
};


export default function InteractiveLists() {
  const [open, setOpen] = React.useState(false);
  const [rows, setRows] = useState([]);
  const [currentStudentId, setCurrentStudentId] = useState('');
  const { uuid } = useParams();
  const classes = useStyles();
  const history = useHistory();

  const { enqueueSnackbar } = useSnackbar();
  
  const handleClose = (value) => {
    setOpen(false);
  };

  const handleClickOpen = (userId) => {
    setCurrentStudentId(userId)
    setOpen(true);
  };

  const onApprovedClicked = (application) => {

    InternshipApplicationService.select(application).then(() => fetchStudentApplications());
    enqueueSnackbar("Contrat Apprové",  { variant: 'success' });

  }

  const onRejectedClicked = (application) => {

    InternshipApplicationService.reject(application).then(() => fetchStudentApplications());
    enqueueSnackbar("Contrat Rejeté",  { variant: 'success' });

  }

  const fetchStudentApplications = async () => {
    const r1 = await InternshipApplicationService.findByOffer(uuid);
    setRows(r1.data.applications);
  }

  useEffect(() => { fetchStudentApplications() }, [])

  return (
    <div>
      <Container>
        <IconButton
          onClick={() => history.goBack()}>
          <KeyboardBackspaceIcon />
          <Typography>Retour</Typography>
        </IconButton>

        <Box
          mb={2}
          paddingTop={2}
          textAlign="center">

          <Typography component="h1" variant="h4">Liste d'étudiants intéressés</Typography>
        </Box>
      </Container>
      {rows.length == 0 &&
        <Container>
          <Box
            mb={2}
            paddingTop={2}
            paddingBottom={2}
            textAlign="center">
            <Typography >Pas d'application pour cette offre</Typography>
          </Box>
        </Container>
      }


      <TableContainer>
        <Table className={classes.table} aria-label="simple table">
          {rows.length > 0 &&
            <TableHead>
              <TableRow>
                <TableCell align="center"><strong>Nom</strong></TableCell>
                <TableCell align="center"><strong>Date</strong></TableCell>
                <TableCell align="center"><strong>Décision</strong></TableCell>
              </TableRow>
            </TableHead>
          }
          <TableBody>
            {rows.map((application, index) => (
              <TableRow key={index}>
                <TableCell align="center">{application.studentFirstName + " " + application.studentLastName}</TableCell>
                <TableCell component="th" scope="row" align="center">{new Date(application.date).toLocaleDateString()}</TableCell>
                <TableCell align="center">

                  <Box margin={1}>
                    <Button
                      variant="contained" color="secondary"
                      size="small"
                      onClick={() => handleClickOpen(application.uniqueId)}

                      startIcon={<AttachmentIcon />}
                    >
                        Documents
                        </Button>
                  </Box>

                  <Box margin={1}>
                    <Button
                      variant="contained" color="primary"
                      size="small" startIcon={<ThumbUpAltOutlinedIcon />}
                      onClick={() => onApprovedClicked(application.uniqueId)}
                    >
                      Approuver
                        </Button>
                  </Box>

                  <Box margin={1}>
                    <Button
                      variant="contained" color="secondary"
                      size="small"
                      startIcon={<ThumbDownAltOutlinedIcon />} onClick={() => onRejectedClicked(application.uniqueId)}
                    >
                      Refuser
                        </Button>
                  </Box>

                  <Box margin={1}>
                  </Box>
                </TableCell>
              </TableRow>
            ))}
            <StudentPortfolioDetailsDialogProps open={open} onClose={handleClose} selectedValue={currentStudentId} />
          </TableBody>
        </Table>
      </TableContainer>

    </div>
  );
}