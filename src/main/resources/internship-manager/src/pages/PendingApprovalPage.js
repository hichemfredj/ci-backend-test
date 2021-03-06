import React, { Component } from "react";

import InternshipOfferCreationForm from '../components/InternshipOfferCreationForm'

import CssBaseline from '@material-ui/core/CssBaseline';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import Paper from '@material-ui/core/Paper';
import Divider from '@material-ui/core/Divider';
import PendingApprovalList from "../components/PendingApprovalList";

export default class PendingApprovalPage extends Component {

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

              <PendingApprovalList/>

          </Paper>

        </Container>

      </div>
    )
  }
}