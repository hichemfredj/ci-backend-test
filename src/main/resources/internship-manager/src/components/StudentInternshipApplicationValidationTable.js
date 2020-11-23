import React, { useEffect, useState } from 'react';
import Button from '@material-ui/core/Button';
import PropTypes from 'prop-types';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import Dialog from '@material-ui/core/Dialog';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import { Box } from "@material-ui/core";
import ThumbUpAltOutlinedIcon from '@material-ui/icons/ThumbUpAltOutlined';
import ThumbDownAltOutlinedIcon from '@material-ui/icons/ThumbDownAltOutlined';
import AttachmentIcon from '@material-ui/icons/Attachment';
import InternshipApplicationService from '../services/InternshipApplicationService';
import StudentDocumentsList from "./StudentDocumentsList";


//tables values
const useStyles = makeStyles({
  table: {
    minWidth: 600,
  },
});

//other class

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


export default function StudentInternshipApplicationValidationTable(props) {
  const [open, setOpen] = React.useState(false);
  const [rows, setRows] = useState([]);
  const [currentApplicationId, setCurrentApplicationId] = useState('');

  const classes = useStyles();

  const handleClose = () => {
    setOpen(false);
  };

  const handleClickOpen = (applicationId) => {
    setCurrentApplicationId(applicationId)
    setOpen(true);
  };

  const onApprovedClicked = (appId) => {
    InternshipApplicationService.approve(appId).then(response => fetchApplications());
  };

  const onRejectedClicked = (appId) => {
    InternshipApplicationService.reject(appId).then(response => fetchApplications());
  };

  const fetchApplications = async () => {
    const response = await InternshipApplicationService.pendingApproval();
    setRows(response.data.applications);
  }

  useEffect(() => {
    fetchApplications();
  }, [])

  return (
    <div>
      <Container>
        <Box
          mb={2}
          paddingTop={2}
          textAlign="center">

          <Typography component="h1" variant="h4">Approbations</Typography>
        </Box>
      </Container>
      {rows.length == 0 &&
        <Container>
          <Box
            mb={2}
            paddingTop={2}
            paddingBottom={2}
            textAlign="center">

            <Typography >Pas d'application pour vous</Typography>
          </Box>
        </Container>
      }
      {rows.length > 0 &&

        <TableContainer>
          <Table className={classes.table} aria-label="simple table">
            <TableHead>
              <TableRow>
                <TableCell align="center"><strong>Student Name</strong></TableCell>
                <TableCell align="center"><strong>Compagnie</strong></TableCell>
                <TableCell align="center"><strong>Job Title</strong></TableCell>
                <TableCell align="center"><strong>Date</strong></TableCell>
                <TableCell align="center"><strong>Décision</strong></TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {rows.map((application, index) => (
                <TableRow key={index}>
                  <TableCell align="center">{application.studentFirstName + " " + application.studentLastName}</TableCell>
                  <TableCell align="center">{application.company}</TableCell>
                  <TableCell align="center">{application.jobTitle}</TableCell>
                  <TableCell align="center">{new Date().toLocaleDateString()}</TableCell>
                  <TableCell align="center">
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
                    <Button
                      variant="contained" color="secondary"
                      size="small"
                      onClick={() => handleClickOpen(application.uniqueId)}
                      startIcon={<AttachmentIcon />}
                    >
                      Documents
                    </Button>
                  </Box>
                  </TableCell>
                </TableRow>
              ))}
              <StudentPortfolioDetailsDialogProps open={open} onClose={handleClose} selectedValue={currentApplicationId} />
            </TableBody>
          </Table>
        </TableContainer>
      }
    </div>
  );
}