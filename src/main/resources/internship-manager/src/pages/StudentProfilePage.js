import React, { Component } from "react";
import CssBaseline from '@material-ui/core/CssBaseline';
import Container from '@material-ui/core/Container';
import Paper from '@material-ui/core/Paper';
import StudentSelection from '../components/StudentSelection'
import StudentProfile from "../components/StudentProfile";

export default class StudentProfilePage extends Component {

    //
    // Constructor
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
                        <StudentProfile/>
                    </Paper>
                </Container>
            </div>
        )
    }
}