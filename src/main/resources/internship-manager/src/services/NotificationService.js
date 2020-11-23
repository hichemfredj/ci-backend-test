import axios from 'axios'
import { Config } from "../environment";
import { useSnackbar } from 'notistack';

function headers() {

    let token = localStorage.getItem("AccessToken");

    return {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': token
        }
    }
}


class NotificationService {

    //
    // Post
    //

    send(request) {
        return axios.post(Config.target + '/notification/send', request, headers());
    }

}

export default new NotificationService();