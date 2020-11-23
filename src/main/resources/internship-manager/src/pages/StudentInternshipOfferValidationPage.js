import React, { Component } from "react";
import CssBaseline from '@material-ui/core/CssBaseline';
import Container from '@material-ui/core/Container';
import Paper from '@material-ui/core/Paper';
import StudentInternshipOfferValidationTable from '../components/StudentInternshipValidationTable'

export default class StudentInternshipOfferValidationPage extends Component {

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
                        <StudentInternshipOfferValidationTable />
                    </Paper>
                </Container>
            </div>
        )
    }
}