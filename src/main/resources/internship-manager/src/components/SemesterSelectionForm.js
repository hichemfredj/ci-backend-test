import React, { useEffect, useState } from "react";
import { makeStyles } from '@material-ui/core/styles';

import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import MenuItem from '@material-ui/core/MenuItem';
import InputLabel from '@material-ui/core/InputLabel';
import Select from '@material-ui/core/Select';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import Divider from '@material-ui/core/Divider';
import FormControl from '@material-ui/core/FormControl';
import SettingsService from "../services/SettingsService";
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Switch from '@material-ui/core/Switch';

const useStyles = makeStyles(theme => ({
  formControl: {
    minWidth: 150,
    margin: theme.spacing(1),
    marginBottom: 30,
  },
  selectEmpty: {
    marginTop: theme.spacing(1),
  },
}));

export default function SemesterSelectionForm() {

  const classes = useStyles();
  const [session, setSession] = useState("");
  const [annee, setAnnee] = useState("");
  const [requireApproval, setRequireApproval] = useState(false);

  let sessiontoloop = [
    { session: "Automne" },
    { session: "Hiver" },
    { session: "Été" }
  ]

  //
  // Event Handlers
  //

  const fetchSettings = () => {

    SettingsService.getSemester().then(response => {
      
      let split = response.data.split('-');

      setSession(translateSession(split[0]));
      setAnnee(split[1])
    });

    SettingsService.getRequireApproval().then(response => 
      setRequireApproval(response.data));
  }

  const translateSession = (session) =>{
    switch (session){
      case "AUTUMN" : return "Automne";
      case "WINTER" : return "Hiver";
      case "SUMMER" : return "Été";
      default: return "ERROR";
    }
  }

  const sessionName = () => {

    switch (session) {
      case "Automne": return "AUTUMN";
      case "Hiver": return "WINTER";
      case "Été": return "SUMMER";
      default: return "ERROR";
    }
  }

  const onFormSubmit = () => {
    let semester = sessionName() + "-" + annee;
    SettingsService.setSemester(semester);

    window.location.reload(true);
  }

  const onRequireApprovalChange = () => {

    let state = !requireApproval;

    setRequireApproval(state)
    SettingsService.setRequireApproval(state);
  }

  const onClicked = () => {
    onFormSubmit();
  }

  useEffect(() => { fetchSettings(); }, [])

  //
  // Rendering
  //
  return (
    <Container component="main" maxWidth="md">

      <div>

        <Box
          mb={2}
          paddingTop={2}
          textAlign="center">

          <Typography component="h1" variant="h5">Paramètres</Typography>

        </Box>

        <Divider />

        <Box
          mb={0}
          paddingTop={2}
          textAlign="left">

          <Typography component="h1" variant="h6">Sélection de semestre</Typography>

        </Box>

        <Grid container spacing={3}>

          <Grid item xs={4} >
            <FormControl className={classes.formControl} >
              <TextField
                type="number"
                label="Année"
                value={annee}
                onChange={(event) => setAnnee(event.target.value)}
              >
              </TextField>
            </FormControl>
          </Grid>

          <Grid item xs={4} >
            <FormControl className={classes.formControl} >
              <InputLabel id="session-label">Session</InputLabel>
              <Select labelId="session-label" id="session-label-select" value={session} onChange={(event) => setSession(event.target.value)} >
                <MenuItem value=""><em>Vide</em></MenuItem>
                {
                  sessiontoloop.map(session => {
                    return <MenuItem value={session.session}>{session.session}</MenuItem>;
                  })
                }
              </Select>
            </FormControl>
          </Grid>

          <Grid item xs={4}>
            <Box mt={2}>
              <Button
                type="submit"

                variant="contained"
                color="primary"
                onClick={() => onClicked()}
              >
                Sélectionner
              </Button>
            </Box>
          </Grid>
        </Grid>

        <Box
          mb={2}
          paddingTop={2}
          textAlign="left">

          <Typography component="h1" variant="h6">Approbation</Typography>

        </Box>

        <FormControlLabel
          control={
            <Switch
              checked={requireApproval}
              onChange={onRequireApprovalChange}
              name="requireApproval"
              color="primary"
            />
          }
          label="Approbation D'Application Obligatoire"
        />
        <Box paddingBottom={2} />
      </div>
    </Container>
  )
}