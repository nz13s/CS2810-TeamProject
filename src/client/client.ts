import axios from "axios";

export default class Client {
  static servlet = `api`;
  static baseURL = `tomcat.xhex.uk/${Client.servlet}`;

  static makeRequest(
    method: "GET" | "POST" | "DELETE",
    endpoint: string,
    data = {}
  ): Promise<any> {
    return axios({
      method: method,
      url: `https://${Client.baseURL}${endpoint}`,
      headers: {
        "X-Session-ID": localStorage.getItem("session")
      },
      params: data
    });
  }

  static makeSocket(): WebSocket {
    const sessionID = localStorage.getItem("session");
    return new WebSocket(
      `wss://${Client.baseURL}/sockets/staff?X-Session-ID=${sessionID}`
    );
  }
}
