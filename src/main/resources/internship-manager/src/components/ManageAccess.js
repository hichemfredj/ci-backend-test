import React, { useEffect, useState } from 'react';
import { fade, makeStyles } from '@material-ui/core/styles';
import { useHistory } from 'react-router-dom';
import { useParams } from "react-router-dom";
import InternshipOfferService from '../services/InternshipOfferService';
import UserService from '../services/UserService';
import KeyboardBackspaceIcon from '@material-ui/icons/KeyboardBackspace';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import IconButton from '@material-ui/core/IconButton';
import ListSubheader from '@material-ui/core/ListSubheader';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import AddCircleOutlineOutlinedIcon from '@material-ui/icons/AddCircleOutlineOutlined';
import RemoveCircleOutlineOutlinedIcon from '@material-ui/icons/RemoveCircleOutlineOutlined';
import Box from '@material-ui/core/Box';
import { Container, Divider } from '@material-ui/core';

const useStyles = makeStyles((theme) => ({
  root: {
    width: 300,
    backgroundColor: 'lightgray',
  },
  nested: {
    paddingLeft: theme.spacing(4),
  },
  search: {
    position: 'relative',
    borderRadius: theme.shape.borderRadius,
    backgroundColor: fade(theme.palette.common.white, 0.15),
    '&:hover': {
      backgroundColor: fade(theme.palette.common.white, 0.25),
    },
    marginRight: theme.spacing(2),
    marginLeft: 0,
    width: '100%',
    [theme.breakpoints.up('sm')]: {
      marginLeft: theme.spacing(3),
      width: 'auto',
    },
  },
  searchIcon: {
    padding: theme.spacing(0, 2),
    height: '100%',
    position: 'absolute',
    pointerEvents: 'none',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
  inputRoot: {
    color: 'inherit',
  },
  inputInput: {
    padding: theme.spacing(1, 1, 1, 0),
    // vertical padding + font size from searchIcon
    paddingLeft: `calc(1em + ${theme.spacing(4)}px)`,
    transition: theme.transitions.create('width'),
    width: '100%',
    [theme.breakpoints.up('md')]: {
      width: '20ch',
    },
  },
}));

export default function InteractiveList() {

  const { uuid } = useParams();
  const classes = useStyles();
  const history = useHistory();

  const [visible, setVisible] = useState([]);
  const [invisible, setInvisible] = useState([]);

  const removeStudent = (student) => {
    const request = { offerUniqueId: uuid, userUniqueId: student.uniqueId };
    InternshipOfferService.removeUser(request).then(() => fetchStudents());
  }

  const addStudent = (student) => {
    const request = { offerUniqueId: uuid, userUniqueId: student.uniqueId };
    InternshipOfferService.addUser(request).then(() => fetchStudents());
  }

  const fetchStudents = async () => {

    const r1 = await InternshipOfferService.users(uuid);
    const r2 = await UserService.students();

    setVisible(r1.data.users);
    setInvisible(r2.data.users.filter(student => !r1.data.users.some(x => student.uniqueId == x.uniqueId)));
  }

  useEffect(() => { fetchStudents() }, [])

  return (
    <Container>

      <IconButton
        onClick={() => history.goBack()}>
        <KeyboardBackspaceIcon />
        <Typography>Retour</Typography>
      </IconButton>

      <Box
        mb={2}
        paddingTop={2}
        textAlign="center">

        <Typography component="h1" variant="h4">Gérer l'accès</Typography>
      </Box>

      <Grid
        container
        spacing={8}
        justify="center"
        mt={4}
        paddingBottom={4}
        textAlign="center">

        <Grid item>
          <List
            component="nav"
            aria-labelledby="nested-list-subheader"
            subheader={
              <ListSubheader component="div" id="nested-list-subheader">
                Avec accès ({visible.length})
                <Divider/>
              </ListSubheader>
            }
            className={classes.root}
          >
            {visible.length > 0 && visible.map((user, index) => (
              <ListItem key={index}>
                <ListItemText primary={user.firstName + ' ' + user.lastName} />
                <IconButton onClick={() => removeStudent(user)}>
                  <RemoveCircleOutlineOutlinedIcon />
                </IconButton>
              </ListItem>
            ))}
            {visible.length == 0 &&
              <ListItem key={1}>
                <ListItemText primary={'Aucun étudiant avec accès!'} />
              </ListItem>
            }
          </List>
        </Grid>

        <Grid item>
          <List
            component="nav"
            aria-labelledby="nested-list-subheader"
            subheader={
              <ListSubheader component="div" id="nested-list-subheader">
                Sans accès ({invisible.length})
                <Divider/>
            </ListSubheader>
            }
            className={classes.root}
          >
            {invisible.length > 0 && invisible.map((user, index) => (
              <ListItem key={index}>
                <ListItemText primary={user.firstName + ' ' + user.lastName} />
                <IconButton onClick={() => addStudent(user)}>
                  <AddCircleOutlineOutlinedIcon />
                </IconButton>
              </ListItem>
            ))}
            {invisible.length == 0 &&
              <ListItem key={1}>
                <ListItemText primary={'Aucun étudiant sans accès!'} />
              </ListItem>
            }
          </List>
        </Grid>

      </Grid>

    </Container>
  );
}