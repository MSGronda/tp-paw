import axiosInstance from "../api"
import authService from "./AuthService.ts";
import {AxiosError} from "axios";


export class AxiosService {
  GET = 0;
  PUT = 1;
  POST = 2;
  DELETE = 3;
  PATCH = 4;

  async authAxiosWrapper(action: any, path: any, config: any = {}, data = {}) {
    if (!config.hasOwnProperty('headers'))
      config.headers = {}
    if (!config.headers.hasOwnProperty('Authorization')) {
      config.headers['Authorization'] = this.getToken();
    }

    let res;
    try {
      res = await this.axiosWrapper(action, path, config, data);
      return res;
    } catch (err) {
      if(!(err instanceof AxiosError) || !err.response) throw new Error("Unable to connect to server");
      if ( err.response.status == 404 || err.response.status == 403 || err.response.status == 500) {
        window.location.replace("/paw-2023a-06/error?code=" + err.response.status);
        return;
      }
      if (err.response.status !== 401) throw err;
    }

    config.headers['Authorization'] = this.getRefreshToken();
    try {
      res = await this.axiosWrapper(action, path, config, data);
      if (!res) throw new Error("Unable to connect to server");

      const token = res.headers['x-auth'];

      if (localStorage.getItem('token') || localStorage.getItem("refresh")) localStorage.setItem('token', token);
      else sessionStorage.setItem('token', token);

      return res;
    } catch (err) {
      if(!(err instanceof AxiosError) || !err.response) throw new Error("Unable to connect to server");
      if ( err.response.status == 404 || err.response.status == 403 || err.response.status == 500) {
        window.location.replace("/paw-2023a-06/error?code=" + err.response.status);
        return;
      }
      if (err.response.status === 401) {
        console.log("Invalid refresh token");
        authService.logout();
        window.location.replace("/paw-2023a-06/login");
        throw new Error("Unauthorized");
      }
      throw err;
    }
  }

  async axiosWrapper(action: any, path: any, config: any = {}, data = {}) {
    const aux = path
    switch (action) {
      case this.GET:
        return await axiosInstance.get(aux, config);
      case this.PUT:
        return await axiosInstance.put(aux, data, config)
      case this.POST:
        // config.headers['Content-Type'] =  'application/vnd.unimart.api.v1+json'
        return await axiosInstance.post(aux, data, config);
      case this.DELETE:
        return await axiosInstance.delete(aux, config);
      case this.PATCH:
        return await axiosInstance.patch(aux, data, config)
      default:
        break;
    }
  }

  getToken() {
    if (localStorage.getItem("token") !== null)
      return localStorage.getItem("token");
    else
      return sessionStorage.getItem("token");
  }

  getRefreshToken() {
    if (localStorage.getItem("refresh") !== null)
      return localStorage.getItem("refresh");
    else
      return sessionStorage.getItem("refresh");
  }

  getBasicToken(mail: string, password: string) {
    const credentials = mail + ":" + password;
    const hash = btoa(credentials);
    return "Basic " + hash;
  }
}
