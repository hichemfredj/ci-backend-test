import React, { Component } from "react";
import InternshipApplicationService from "../services/InternshipApplicationService";
import { withRouter } from 'react-router';
import { withSnackbar } from 'notistack';
import Lock from '../utils/Lock'
import DateFnsUtils from '@date-io/date-fns';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { MuiPickersUtilsProvider, KeyboardDatePicker } from '@material-ui/pickers';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import Button from '@material-ui/core/Button';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import ContractService from "../services/ContractService";


//
// Data
//

const state = {
  applications: []
}


class StudentApplicationStatusList extends Component {

  //
  // Constructors
  //

  constructor(props) {
    super(props);
    this.state = state;
    this.onUpdateStudentApplicationsList();
    this.submitLock = new Lock();
  }

  
  isAdministrator = () => {
    return localStorage.getItem('UserType') === "ADMINISTRATOR";
  }

  //
  // Event Handlers
  //

  onCreateContract = applicationUniqueId => {
    ContractService.create(applicationUniqueId).then(response => {
      this.props.enqueueSnackbar("Contrat crée",  { variant: 'success' });
    })
  }

  onDateChange = index => date => {
    this.state.applications[index].interviewDate = date;
    InternshipApplicationService.addInterview(this.state.applications[index].uniqueId, date.getTime());
    this.forceUpdate();
  };

  onUpdateStudentApplicationsList() {
    let userId = this.props.studentId ? this.props.studentId : localStorage.getItem("UserUniqueId");
    InternshipApplicationService.internshipApplications(userId).then(response => {
      this.setState(response.data);
    })
  }

  //
  // Rendering
  //

  renderTableData() {
    return this.state.applications.map((studentAppList, index) => {
      const { uniqueId, company, jobTitle, date, status, interviewDate } = studentAppList
      return (
        <TableRow key={index}>
          <TableCell component="th" scope="row" align="center">{company}</TableCell>
          <TableCell component="th" scope="row" align="center">{jobTitle}</TableCell>
          <TableCell component="th" scope="row" align="center">{new Date(date).toLocaleDateString()}</TableCell>

          <TableCell component="th" scope="row" align="center" >
            {status == "PENDING_APPROVAL" && <Typography color="primary">en attente</Typography>}
            {status == "REJECTED" && <Typography color="error">rejeté</Typography>}
            {status == "APPROVED" && <Typography color="primary">approuvé</Typography>}
            {status == "SELECTED" &&
              <div>
                <Typography color="secondary">selectionné</Typography>

                <MuiPickersUtilsProvider utils={DateFnsUtils}>
                  {interviewDate == 0 &&
                    <KeyboardDatePicker
                      disableToolbar
                      variant="inline"
                      format="MM/dd/yyyy"
                      margin="normal"
                      label="Date intervu"
                      autoOk={true}
                      value={null}
                      onChange={this.onDateChange(index)}
                      KeyboardButtonProps={{
                        'aria-label': 'change date',
                      }}
                    />
                  }
                  {interviewDate > 0 &&
                    <KeyboardDatePicker
                      disableToolbar
                      variant="inline"
                      format="MM/dd/yyyy"
                      margin="normal"
                      label="Date intervu"
                      autoOk={true}
                      value={new Date(interviewDate)}
                      onChange={this.onDateChange(index)}
                      KeyboardButtonProps={{
                        'aria-label': 'change date',
                      }}
                    />
                  }
                </MuiPickersUtilsProvider>

                {
                  this.isAdministrator() &&
                  <Button
                    variant="contained" color="primary"
                    size="small"
                    onClick={() => this.onCreateContract(uniqueId)}
                  >
                    Create contract
                  </Button>
                }
                
              </div>
            }
          </TableCell>

        </TableRow>

      )
    })
  }

  render() {
    return (
      <div>
        <Container>
          <Box
            mb={2}
            pt={2}
            textAlign="left"
          >
            <Typography component="h1" variant="h4" align="center">État de la candidature</Typography>
          </Box>
        </Container>

        <TableContainer>
          <Table size="small" aria-label="a dense table">
            <TableHead>
              <TableRow>
                <TableCell align="center"><strong>Compagnie</strong></TableCell>
                <TableCell align="center"><strong>Titre du poste</strong></TableCell>
                <TableCell align="center"><strong>Date</strong></TableCell>
                <TableCell align="center"><strong>État</strong></TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {this.renderTableData()}
            </TableBody>
          </Table>
        </TableContainer>
      </div>


    )
  }




}
export default withSnackbar(withRouter(StudentApplicationStatusList));