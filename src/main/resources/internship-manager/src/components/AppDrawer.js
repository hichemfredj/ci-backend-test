import React, { useState, useEffect } from 'react';
import clsx from 'clsx';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import AuthenticationService from '../services/AuthenticationService';

import Drawer from '@material-ui/core/Drawer';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import List from '@material-ui/core/List';
import CssBaseline from '@material-ui/core/CssBaseline';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import Button from '@material-ui/core/Button';
import PeopleIcon from '@material-ui/icons/People';
import HomeIcon from '@material-ui/icons/Home';
import CreateIcon from '@material-ui/icons/Create';
import { useHistory } from 'react-router-dom';
import GavelOutlinedIcon from '@material-ui/icons/GavelOutlined';
import AssignmentLateIcon from '@material-ui/icons/AssignmentLate';
import VisibilityIcon from '@material-ui/icons/Visibility';
import CloudUploadIcon from '@material-ui/icons/CloudUpload';
import AssignmentTurnedInIcon from '@material-ui/icons/AssignmentTurnedIn';
import SettingsService from "../services/SettingsService";
import SettingsIcon from '@material-ui/icons/Settings';
import CheckIcon from '@material-ui/icons/Check';


const drawerWidth = 250;

const useStyles = makeStyles((theme) => ({
  root: {
    display: 'flex',
  },
  appBar: {
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
    flexGrow: 1,
  },
  panelTitle: {
    flexGrow: 1,
  },
  appBarShift: {
    marginLeft: drawerWidth,
    width: `calc(100% - ${drawerWidth}px)`,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  menuButton: {
    marginRight: 36,
  },
  hide: {
    display: 'none',
  },
  drawer: {
    width: drawerWidth,
    flexShrink: 0,
    whiteSpace: 'nowrap',
    color: 'white',
  },
  drawerOpen: {
    width: drawerWidth,
    transition: theme.transitions.create('width', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  drawerClose: {
    transition: theme.transitions.create('width', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
    overflowX: 'hidden',
    width: theme.spacing(7) + 1,
    [theme.breakpoints.up('sm')]: {
      width: theme.spacing(9) + 1,
    },
  },
  toolbar: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    padding: theme.spacing(0, 1),
    ...theme.mixins.toolbar,
  },
  content: {
    flexGrow: 1,
    padding: theme.spacing(3),
  },
}));

export default function AppDrawer(props) {

  const [open, setOpen] = useState(false);
  const [session, setSession] = useState();

  const classes = useStyles();
  const theme = useTheme();
  const history = useHistory();

  useEffect(() => { getSession(); }, [])

  const userType = () => {

    let type = localStorage.getItem('UserType').toLowerCase();

    switch (type) {
      case "student": return "ÉTUDIANT";
      case "employer": return "EMPLOYEUR";
      case "administrator": return "ADMINISTRATEUR";
      default: return "ÉTUDIANT";
    }
  }

  const getSession = () => {

    SettingsService.getSemester().then((response) => {
      let split = response.data.split('-');

      setSession(translateSession(split[0]) + "-" + split[1]);
    });
  }

  const translateSession = (session) => {
    switch (session) {
      case "AUTUMN": return "Automne";
      case "WINTER": return "Hiver";
      case "SUMMER": return "Été";
      default: return "ERROR";
    }
  }

  const isStudent = () => {
    return localStorage.getItem('UserType') === "STUDENT";
  }

  const isEmployer = () => {
    return localStorage.getItem('UserType') === "EMPLOYER";
  }

  const isAdministrator = () => {
    return localStorage.getItem('UserType') === "ADMINISTRATOR";
  }

  const handleDrawerOpen = () => {
    setOpen(true);
  };

  const handleDrawerClose = () => {
    setOpen(false);
  };

  return (
    <div className={classes.root}>
      <CssBaseline />
      <AppBar
        position="fixed"
        className={clsx(classes.appBar, {
          [classes.appBarShift]: open,
        })}
      >
        <Toolbar>
          <IconButton
            color="inherit"
            aria-label="open drawer"
            onClick={handleDrawerOpen}
            edge="start"
            className={clsx(classes.menuButton, {
              [classes.hide]: open,
            })}
          >
            <MenuIcon />
          </IconButton>
          <Typography variant="h6" noWrap className={classes.panelTitle}>
            {"PANNEAU " + userType()}
          </Typography>

          <Typography variant="h6" noWrap className={classes.panelTitle}>
            {"Session Actuelle : " + session}
          </Typography>

          <Button
            color="inherit"
            onClick={() => {
              AuthenticationService.logout();
              history.push("/login");
            }}>Se déconnecter</Button>

        </Toolbar>
      </AppBar>
      <Drawer
        variant="permanent"
        className={clsx(classes.drawer, {
          [classes.drawerOpen]: open,
          [classes.drawerClose]: !open,
        })}
        classes={{
          paper: clsx({
            [classes.drawerOpen]: open,
            [classes.drawerClose]: !open,
          }),
        }}
      >
        <div className={classes.toolbar}>
          <IconButton onClick={handleDrawerClose}>
            {theme.direction === 'rtl' ? <ChevronRightIcon /> : <ChevronLeftIcon />}
          </IconButton>
        </div>

        <Divider />

        <List>
          <ListItem
            button
            key={"Home"}
            onClick={() => history.push("/home")}>
            <ListItemIcon><HomeIcon /> </ListItemIcon>
            <ListItemText primary={"Accueil"} />
          </ListItem>
        </List>

        <Divider />

        {
          isStudent() &&
          <List>
            <ListItem
              button
              key={"Portfolio"}
              onClick={() => history.push("/portfolio")}>
              <ListItemIcon><CloudUploadIcon /> </ListItemIcon>
              <ListItemText primary={"Portfolio"} />
            </ListItem>
            <ListItem
              button
              key={"Application Creation"}
              onClick={() => history.push("/internship-application-creation")}>
              <ListItemIcon><CreateIcon /> </ListItemIcon>
              <ListItemText primary={"Application"} />
            </ListItem>
            <ListItem
              button
              key={"Application Status"}
              onClick={() => history.push("/internship-application-status")}>
              <ListItemIcon><VisibilityIcon /> </ListItemIcon>
              <ListItemText primary={"État de la candidature"} />
            </ListItem>
            <ListItem
              button
              key={"Signature"}
              onClick={() => history.push("/create-signature")}>
              <ListItemIcon><CheckIcon /> </ListItemIcon>
              <ListItemText primary={"Signature"} />
            </ListItem>
          </List>
        }

        {
          isEmployer() &&
          <List>
            <ListItem
              button
              key={"Offer Creation"}
              onClick={() => history.push("/internship-offer-creation")}>
              <ListItemIcon><CreateIcon /> </ListItemIcon>
              <ListItemText primary={"Créer une offre de stage"} />
            </ListItem>
            <ListItem
              button
              key={"Student Selection"}
              onClick={() => history.push("/student-selection-page")}>
              <ListItemIcon><AssignmentTurnedInIcon /> </ListItemIcon>
              <ListItemText primary={"Sélection des étudiants"} />
            </ListItem>
            <ListItem
              button
              key={"Signature"}
              onClick={() => history.push("/create-signature")}>
              <ListItemIcon><CheckIcon /> </ListItemIcon>
              <ListItemText primary={"Signature"} />
            </ListItem>
          </List>
        }

        {
          isAdministrator() &&
          <List>
            <ListItem
              button
              key={"Student Offer Validation"}
              onClick={() => history.push("/student-offer-validation")}>
              <ListItemIcon><VisibilityIcon /> </ListItemIcon>
              <ListItemText primary={"Visualisation des offres"} />
            </ListItem>
            <ListItem
              button
              key={"Pending Approval"}
              onClick={() => history.push("/pending-approval")}>
              <ListItemIcon><GavelOutlinedIcon /> </ListItemIcon>
              <ListItemText primary={"Offres en attentes"} />
            </ListItem>
            <ListItem
              button
              key={"Applications en attente"}
              onClick={() => history.push("/internship-application-validation")}>
              <ListItemIcon><AssignmentLateIcon /> </ListItemIcon>
              <ListItemText primary={"Applications en attentes"} />
            </ListItem>
            <ListItem
              button
              key={"Liste des étudiants"}
              onClick={() => history.push("/student-list-page")}>
              <ListItemIcon><PeopleIcon /> </ListItemIcon>
              <ListItemText primary={"Liste des étudiants"} />
            </ListItem>
            <ListItem
              button
              key={"Liste des employés"}
              onClick={() => history.push("/employer-list-page")}>
              <ListItemIcon><PeopleIcon /> </ListItemIcon>
              <ListItemText primary={"Liste des employés"} />
            </ListItem>
            <ListItem
              button
              key={"Paramètres"}
              onClick={() => history.push("/semester-selection")}>
              <ListItemIcon><SettingsIcon /> </ListItemIcon>
              <ListItemText primary={"Paramètres"} />
            </ListItem>
            <ListItem
              button
              key={"Signature"}
              onClick={() => history.push("/create-signature")}>
              <ListItemIcon><CheckIcon /> </ListItemIcon>
              <ListItemText primary={"Signature"} />
            </ListItem>
          </List>
        }

        <List>
          <ListItem
            button
            key={"contracts"}
            onClick={() => history.push("/contracts")}>
            <ListItemIcon><HomeIcon /> </ListItemIcon>
            <ListItemText primary={"contrats"} />
          </ListItem>
        </List>

      </Drawer>
      <main className={classes.content}>
        <div className={classes.toolbar} />
        {props.children}
      </main>
    </div>
  );
}
