import axios from 'axios'
import { Config } from "../environment";


function headers() {

  let token = localStorage.getItem("AccessToken");

  return {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': token,
    }
  }
}

class UserService {

  //
  // Get
  //

  students() {
    return axios.get(Config.target + '/user/students', headers());
  }

  studentsWithApplication() {
    return axios.get(Config.target + '/user/students-with-application', headers());
  }

  studentsWithoutApplication() {
    return axios.get(Config.target + '/user/students-without-application', headers());
  }
  
  employers() {
    return axios.get(Config.target + '/user/employers', headers());
  }

  employersWithOffer() {
    return axios.get(Config.target + '/user/employers-with-offer', headers());
  }

  employersWithoutOffer() {
    return axios.get(Config.target + '/user/employers-without-offer', headers());
  }

}

export default new UserService();