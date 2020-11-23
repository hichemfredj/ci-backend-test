import axios from 'axios'
import { Config } from "../environment";

const headers = {
  'Content-Type': 'application/json'
}

class RegistrationService {

  //
  // Post
  //

  student(request) {
    return axios.post(Config.target + '/registration/student', request, headers);
  }

  employer(request) {
    return axios.post(Config.target + '/registration/employer', request, headers);
  }

}

export default new RegistrationService();