import axios from 'axios'
import { Config } from "../environment";

const headers = {
  'Content-Type': 'application/json'
}

class AuthenticationService {

  //
  // Post
  //

  authenticate(request) {
    return axios.post(Config.target + '/authenticate', request, headers)
      .then(response => {
        localStorage.setItem('UserUniqueId', response.data.userUniqueId);
        localStorage.setItem('UserType', response.data.userType);
        localStorage.setItem('AccessToken', response.data.token);
        localStorage.setItem('LastAuthenticationDate', new Date().getTime())
      });
  }

  //
  // Non-HTTP
  //

  logout(){
    localStorage.setItem('UserUniqueId', '');
    localStorage.setItem('UserType', '');
    localStorage.setItem('AccessToken', '');
    localStorage.setItem('LastAuthenticationDate', '');
  }

  authenticationRequired(){
    let last = localStorage.getItem('LastAuthenticationDate');
    return  last == '' || new Date().getTime() - Number(last) >= 1000 * 60 * 4;
  }

}

export default new AuthenticationService();