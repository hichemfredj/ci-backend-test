import React, { Component } from "react";

import Portfolio from '../components/Portfolio'

import CssBaseline from '@material-ui/core/CssBaseline';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import Paper from '@material-ui/core/Paper';
import Divider from '@material-ui/core/Divider';
import PendingApprovalList from "../components/PendingApprovalList";

export default class PortfolioPage extends Component {

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

              <Portfolio></Portfolio>

          </Paper>

        </Container>

      </div>
    )
  }
}