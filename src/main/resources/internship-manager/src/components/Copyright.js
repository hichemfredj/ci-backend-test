import React, { Component } from "react";

import Link from '@material-ui/core/Link';
import Typography from '@material-ui/core/Typography';

export default class Copyright extends Component {


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
            <Typography variant="body2" color="textSecondary" align="center">
                {'Copyright © '}
                <Link color="inherit" href="https://material-ui.com/">
                    CAL
            </Link>{' '}
                {new Date().getFullYear()}
                {'.'}
            </Typography>
        )
    }
}