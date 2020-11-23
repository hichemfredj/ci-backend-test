import React, { Component } from "react";

import CssBaseline from '@material-ui/core/CssBaseline';
import Container from '@material-ui/core/Container';
import Paper from '@material-ui/core/Paper';
import ContractList from "../components/ContractList";

export default class ContractListPage extends Component {

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
              
              <ContractList/>

          </Paper>

        </Container>

      </div>
    )
  }
}