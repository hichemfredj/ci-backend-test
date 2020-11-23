import React from 'react';
import { BrowserRouter as Router, Route, Redirect, Switch } from 'react-router-dom';
import './App.css';
import { SnackbarProvider } from 'notistack';

import AppDrawer from './components/AppDrawer';

import AuthenticationService from './services/AuthenticationService';

import LoginPage from './pages/LoginPage';
import RegistrationPage from './pages/RegistrationPage';
import InternshipOfferCreationPage from './pages/InternshipOfferCreationPage';
import HomePage from './pages/HomePage';
import StudentInternshipOfferValidationPage from './pages/StudentInternshipOfferValidationPage';
import PendingApprovalPage from './pages/PendingApprovalPage';
import PortfolioPage from './pages/PortfolioPage';
import StudentInternshipApplicationPage from './pages/StudentInternshipApplicationPage';
import StudentInternshipApplicationValidationPage from './pages/StudentInternshipApplicationValidationPage';
import StudentSelectionPage from './pages/StudentSelectionPage';
import StudentInternshipApplicationStatusPage from './pages/StudentInternshipApplicationStatusPage';
import ManageAccessPage from './pages/ManageAccessPage';
import SemesterSelectionPage from './pages/SemesterSelectionPage';
import StudentListPage from './pages/StudentListPage';
import StudentProfilePage from './pages/StudentProfilePage';
import SelectActionPage from './pages/SelectActionPage';
import EmployerProfilePage from './pages/EmployerProfilePage';
import EmployerListPage from './pages/EmployerListPage';
import ContractListPage from './pages/ContractListPage';
import ContractPage from './pages/ContractPage';
import CreateSignaturePage from './pages/CreateSignaturePage';

function App() {

  return (
    <Router>
      <div className="App">
        <div className="container">
          <SnackbarProvider maxSnack={3}>

            <Switch>

              <Route
                exact
                path="/"
                render={() => {
                  return (
                    AuthenticationService.authenticationRequired() ?
                      <Redirect to="/login" /> :
                      <Redirect to="/home" />
                  )
                }}
              />

              <Route exact path="/login" component={LoginPage}></Route>
              <Route exact path="/registration" component={RegistrationPage}></Route>

              <AppDrawer>

                <Route exact path="/home" component={HomePage}></Route>

                <Route exact path="/internship-offer-creation" component={InternshipOfferCreationPage}></Route>

                <Route exact path="/student-offer-validation" component={StudentInternshipOfferValidationPage}></Route>
                <Route exact path="/pending-approval" component={PendingApprovalPage}></Route>

                <Route exact path="/internship-application-creation" component={StudentInternshipApplicationPage}></Route>
                <Route exact path="/portfolio" component={PortfolioPage}></Route>

                <Route exact path="/internship-application-validation" component={StudentInternshipApplicationValidationPage}></Route>
                <Route exact path="/internship-application-status" component={StudentInternshipApplicationStatusPage}></Route>
                <Route exact path="/student-selection-page" component={StudentSelectionPage}></Route>
                <Route exact path="/student-list-page" component={StudentListPage}></Route>
                <Route exact path="/employer-list-page" component={EmployerListPage}></Route>

                <Route exact path="/manage-access/:uuid" component={ManageAccessPage}></Route>

                <Route exact path="/semester-selection" component={SemesterSelectionPage}></Route>
                <Route exact path="/student-profile-page/:uuid/:fullName" component={StudentProfilePage}></Route>
                <Route exact path="/employer-profile-page/:uuid/:fullName" component={EmployerProfilePage}></Route>
                
                <Route exact path="/select-action/:uuid" component={SelectActionPage}></Route>
                <Route exact path="/create-signature" component={CreateSignaturePage}></Route>

                <Route exact path="/contracts" component={ContractListPage}></Route>
                <Route exact path="/contract/:uuid" component={ContractPage}></Route>

              </AppDrawer>

            </Switch>
          </SnackbarProvider>
        </div>
      </div>
    </Router>
  );
}

export default App;
