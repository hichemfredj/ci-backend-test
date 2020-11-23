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

class PortfolioService {

    //
    // Post
    //

    upload(request){
        let fd = new FormData();

        fd.append('type', request.type);
        fd.append('file', request.file);

        return axios.post(Config.target + '/portfolio/upload', fd, uploadHeaders());
    }

    delete(request) {
        return axios.post(Config.target + '/portfolio/delete', request, headers());
    }

    //
    // Put
    //

    approve(request){
        return axios.put(Config.target + `/portfolio/approve/${request}`, {}, headers());
    }

    //
    // Get
    //

    download(request){
        return axios.get(Config.target + `/portfolio/${request}`, downloadHeaders());
    }
    
    portfolioDocuments(request) {
        return axios.get(Config.target + `/portfolio/portfolio-documents/${request}`, headers());
    }

    approved(request){
        return axios.get(Config.target + `/portfolio/approved/${request}`, headers());
    }

}

export default new PortfolioService();