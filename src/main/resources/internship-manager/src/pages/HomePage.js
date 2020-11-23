import React, { Component } from "react";

import CssBaseline from '@material-ui/core/CssBaseline';

export default class HomePage extends Component {

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
          <p>Home page</p>
      </div>
    )
  }
}