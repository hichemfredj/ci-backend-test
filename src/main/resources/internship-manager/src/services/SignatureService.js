import axios from 'axios'
import { Config } from "../environment";


function uploadHeaders() {

    let token = localStorage.getItem("AccessToken");

    return {
        headers: {
            'Content-Type': 'multipart/form-data',
            'Authorization': token
        }
    }
}

function downloadHeaders() {

    let token = localStorage.getItem("AccessToken");

    return {
        headers: {
            'Authorization': token
        },
        responseType: 'blob'
    }
}

function headers() {

    let token = localStorage.getItem("AccessToken");

    return {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': token,
        }
    }
}

class SignatureService {

    //
    // Post
    //

    upload(request){
        let fd = new FormData();
        fd.append('file', request.file);

        return axios.post(Config.target + '/signature/upload', fd, uploadHeaders());
    }


    //
    // Get
    //

    download(request){
        return axios.get(Config.target + `/signature/${request}`, downloadHeaders());
    }
    
    find(request){
        return axios.get(Config.target + `/signature/find/${request}`, headers());
    }
    
}

export default new SignatureService();