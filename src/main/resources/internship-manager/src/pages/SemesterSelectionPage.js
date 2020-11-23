import React, { Component } from "react";

import SemesterSelectionForm from '../components/SemesterSelectionForm'

import CssBaseline from '@material-ui/core/CssBaseline';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import Paper from '@material-ui/core/Paper';
import Divider from '@material-ui/core/Divider';

export default class SemesterSelectionPage extends Component {

  //
  // Constructors
  //

  constructor(props) {
    super();
  }

  //
  // Rendering
  //

  render() {
    return (
      <div>
        <CssBaseline />

        <Container>

          <Paper elevation={3}>

            <SemesterSelectionForm/>

          </Paper>

        </Container>

      </div>
    )
  }
}