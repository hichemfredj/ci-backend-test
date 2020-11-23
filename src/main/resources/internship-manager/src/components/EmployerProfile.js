import { Box, Container, IconButton, Typography } from "@material-ui/core";
import KeyboardBackspaceIcon from '@material-ui/icons/KeyboardBackspace';
import React from "react";
import { useHistory, useParams } from 'react-router-dom';
import StudentSelection from "./StudentSelection";

export default function EmployerProfile() {

    const { uuid } = useParams();
    const { fullName } = useParams();
    const history = useHistory()

    return (
        <div style={{ paddingBottom: '100px' }}>
            <IconButton
                onClick={() => history.goBack()}>
                <KeyboardBackspaceIcon />
                <Typography>Retour</Typography>
            </IconButton>

            <Container>
                <Box
                    pt={2}
                    textAlign="center"
                    borderBottom="1px solid black"
                    width="40%"
                    margin="0px auto">

                    <Typography component="h1" variant="h4">{fullName}</Typography>
                </Box>
            </Container>

            <Box
                mt={10}
            >
                <StudentSelection employerId={uuid} />
            </Box>
        </div>
    )
}