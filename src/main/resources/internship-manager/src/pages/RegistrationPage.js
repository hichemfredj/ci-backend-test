import React, { Component } from "react";

import StudentRegistrationForm from "../components/StudentRegistrationForm";
import EmployerRegistrationForm from "../components/EmployerRegistrationForm";

import Button from '@material-ui/core/Button';
import ButtonGroup from '@material-ui/core/ButtonGroup';
import CssBaseline from '@material-ui/core/CssBaseline';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';

//
// Data
//

const state = {
  student: false,
  employer: false,
}

export default class RegistrationPage extends Component {

  //
  // Constructors
  //

  constructor(props) {
    super();

    this.state = state;
  }

  //
  // Event Handlers
  //

  onSelectStudent = () => this.setState({ student: true });

  onSelectEmployer = () => this.setState({ employer: true });

  onBack = () => this.setState({ student: false, employer: false });

  //
  // Rendering
  //

  render() {
    return (
      <div>
        <CssBaseline />

        {
          (this.state.employer || this.state.student) &&
          <Box
            mt={4}
            textAlign="center">
            <Button
              variant="outlined"
              color="secondary"
              onClick={this.onBack}>
              Retourner
          </Button>
          </Box>
        }

        {
          !(this.state.employer || this.state.student) &&

          <Container
            component="main"
            maxWidth="xs"
          >
            <Box
              mt={24}
              textAlign="center">

              <Typography component="h1" variant="h4">S'inscrire en tant que</Typography>

              <Box mt={4}>
                <ButtonGroup>
                  <Button
                    variant="outlined"
                    color="primary"
                    size="large"
                    onClick={this.onSelectStudent}>
                    Étudiant
              </Button>
                  <Button
                    variant="outlined"
                    color="secondary"
                    size="large"
                    onClick={this.onSelectEmployer}>
                    Employeur
              </Button>
                </ButtonGroup>
              </Box>
            </Box>
          </Container>
        }

        {
          this.state.student &&
          <StudentRegistrationForm></StudentRegistrationForm>
        }

        {
          this.state.employer &&
          <EmployerRegistrationForm></EmployerRegistrationForm>
        }


      </div>
    )
  }
}