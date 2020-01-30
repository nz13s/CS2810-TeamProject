import axios from "axios";

const CORS = `https://cors.x7.workers.dev/`;
const servlet = `backend_sprint1`;
const baseURL = `${CORS}https://tomcat.xhex.uk/${servlet}`;

export default class Client {
  static makeRequest(
    method: "GET" | "POST" | "DELETE",
    endpoint: string,
    data = {}
  ): Promise<any> {
    return axios({
      method: method,
      url: `${baseURL}${endpoint}`,
      headers: {
        "X-Session-ID": localStorage.getItem("session")
      },
      params: data
    });
  }
}
