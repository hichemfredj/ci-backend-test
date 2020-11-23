import axios from 'axios'
import { Config } from "../environment";

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
            'Authorization': token
        }
    }
}

class ContractService {

    //
    // Get
    //

    find(request) {
        return axios.get(Config.target + `/contract/${request}`, headers());
    }

    generate(request) {
        return axios.get(Config.target + `/contract/generate/${request}`, downloadHeaders());
    }

    awaitingSignature(request) {
        return axios.get(Config.target + `/contract/awaiting-signature/${request}`, headers());
    }

    //
    // Put
    //

    sign(request) {
        return axios.put(Config.target + `/contract/sign/${request}`, {}, headers());
    }

    //
    // Post
    //

    create(request){
        return axios.post(Config.target + `/contract/create/${request}`, {}, headers());
    }

}

export default new ContractService();