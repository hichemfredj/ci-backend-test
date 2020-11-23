import axios from 'axios'
import { Config } from "../environment";

function headers() {

    let token = localStorage.getItem("AccessToken");

    return {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': token
        }
    }
}

class SettingsService {

    //
    // Get
    //

    getSemester(){
        return axios.get(Config.target + '/settings/semester', headers());
    }

    getRequireApproval(){
        return axios.get(Config.target + '/settings/require-approval', headers());
    }

    //
    // Put
    //

    setSemester(request){
        return axios.put(Config.target + `/settings/semester/${request}`, {}, headers());
    }

    setRequireApproval(request){
        return axios.put(Config.target + `/settings/require-approval/${request}`, {}, headers());
    }

}

export default new SettingsService();