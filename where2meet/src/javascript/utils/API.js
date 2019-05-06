import axios from "axios";

export default axios.create({
    baseURL: "https://w2mserver.herokuapp.com/",
    responseType: "json",
    headers: {'Access-Control-Allow-Origin': '*'}
});