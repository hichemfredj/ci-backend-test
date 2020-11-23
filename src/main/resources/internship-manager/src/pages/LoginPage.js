import React, { Component } from "react";

import LoginForm from "../components/LoginForm";

import CssBaseline from '@material-ui/core/CssBaseline';

export default class LoginPage extends Component {

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
          <LoginForm></LoginForm>
      </div>
    )
  }
}