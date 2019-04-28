import axios from "axios";

export default axios.create({
    baseURL: "http://localhost:4567/",
    responseType: "json",
    headers: {'Access-Control-Allow-Origin': '*'}
});