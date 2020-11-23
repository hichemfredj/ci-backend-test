import React, { Component } from "react";
import { Link } from 'react-router-dom';
import { withRouter } from 'react-router';
import { withSnackbar } from 'notistack';

import RegistrationService from '../services/RegistrationService';
import Validator from '../utils/Validator';
import Lock from '../utils/Lock'
import Copyright from './Copyright';

import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography';
import LinearProgress from '@material-ui/core/LinearProgress';
import Container from '@material-ui/core/Container';
import LockOutlined from '@material-ui/icons/LockOutlined';
import Divider from '@material-ui/core/Divider';

//
// Data
//

const state = {
  email: '',
  firstName: '',
  lastName: '',
  password: '',
  confirm: ''
}

const errors = {
  email: '',
  firstName: '',
  lastName: '',
  password: '',
  confirm: ''
}

class StudentRegistrationForm extends Component {

  //
  // Constructors
  //

  constructor(props) {
    super();

    this.state = state;
    this.errors = errors;

    this.submitLock = new Lock();
  }

  //
  // Event Handlers
  //

  onFormSubmit = event => {

    event.preventDefault();

    this.frontendValidation(event);

    if (Validator.hasErrors(this.errors) || true === this.submitLock.lock()) return;

    const request = {
      email: this.state.email,
      password: this.state.password,
      firstName: this.state.firstName,
      lastName: this.state.lastName,
    }

    RegistrationService.student(request)
      .then(() => {
        this.props.history.push('/login');
        this.props.enqueueSnackbar("Enregistré avec succès! Vous pouvez maintenant vous connecter",  { variant: 'success' });
      })
      .catch(error => this.backendValidation(error))
      .finally(() => { this.submitLock.unlock(); this.forceUpdate() });
  }

  onChange = event => this.setState({ [event.target.name]: event.target.value });

  //
  // Validation
  //

  frontendValidation(event) {

    Validator.clearErrors(this.errors);

    this.errors.email = Validator.email(this.state.email, "L'adresse email n'est pas valide");
    this.errors.firstName = Validator.notBlank(this.state.firstName, "Le prénom est obligatoire");
    this.errors.lastName = Validator.notBlank(this.state.lastName, "Le nom est obligatoire");
    this.errors.password = Validator.size(this.state.password, 6, 18, "La taille du mot de passe doit être entre 6 et 18 caractères");
    this.errors.confirm = Validator.match(this.state.password, this.state.confirm, "Le mot de passe ne correspond pas");

    this.forceUpdate();
  }

  backendValidation(error) {

    Validator.clearErrors(this.errors);

    this.errors.email = Validator.mapBackendError(error, "email");

    this.forceUpdate();
  }

  //
  // Rendering
  //

  render() {
    return (
      <Container component="main" maxWidth="xs">
        <CssBaseline />

        <div>

          <Box
            mt={12}
            mb={2}
            textAlign="center">

            <LockOutlined fontSize="large" />
            <Typography component="h1" variant="h4">S'inscrire en tant qu'étudiant</Typography>

          </Box>

          <Divider/>

          <form noValidate onSubmit={this.onFormSubmit}>

            <TextField
              error={this.errors.email}
              helperText={this.errors.email}
              onChange={this.onChange}
              variant="outlined"
              margin="normal"
              required
              fullWidth
              id="email"
              label="Adresse email"
              name="email"
              autoComplete="email"
              autoFocus
            />

            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <TextField
                  error={this.errors.firstName}
                  helperText={this.errors.firstName}
                  onChange={this.onChange}
                  autoComplete="fname"
                  name="firstName"
                  variant="outlined"
                  required
                  fullWidth
                  id="firstName"
                  label="Prénom"
                  margin="normal"
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  error={this.errors.lastName}
                  helperText={this.errors.lastName}
                  onChange={this.onChange}
                  variant="outlined"
                  required
                  fullWidth
                  id="lastName"
                  label="Nom de famille"
                  name="lastName"
                  autoComplete="lname"
                  margin="normal"

                />
              </Grid>
            </Grid>

            <TextField
              error={this.errors.password}
              helperText={this.errors.password}
              onChange={this.onChange}
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="password"
              label="Mot de passe"
              type="password"
              id="password"
              autoComplete="current-password"
            />

            <TextField
              error={this.errors.confirm}
              helperText={this.errors.confirm}
              onChange={this.onChange}
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="confirm"
              label="Confirmer"
              type="password"
              id="confirm"
            />

            <Box mt={2}>
              <Button
                type="submit"
                fullWidth
                variant="contained"
                color="primary"
                disabled={this.submitLock.locked}
              >
                S'inscrire 
                </Button>
            </Box>

            {
              this.submitLock.locked &&
              <Box mt={2}>
                <LinearProgress
                  variant="query" />
              </Box>
            }

            <Box mt={2}>
              <Grid container mt={4}>
                <Grid item xs>
                </Grid>
                <Grid item>

                  <Link to="/login">
                    Vous-avez déja un compte? Se connecter
                  </Link>
                  
                </Grid>
              </Grid>
            </Box>

          </form>
        </div>
        <Box mt={4}>
          <Copyright />
        </Box>
      </Container>
    )
  }
}

export default withSnackbar(withRouter(StudentRegistrationForm));