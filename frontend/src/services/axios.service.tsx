import axios from "../api"


export class AxiosService {
    GET = 0;
    PUT = 1;
    POST = 2;
    DELETE = 3;
    PATCH = 4;

    async authAxiosWrapper(action: any, path: any, config: any, data = {}) {
        if(!config.hasOwnProperty('headers'))
            config.headers = {}
        if (!config.headers.hasOwnProperty('Authorization')) {
            config.headers['Authorization'] = this.getBearerToken()
        }
        return await this.axiosWrapper(action, path, config, data)
    }

     async axiosWrapper(action: any, path: any, config: any, data = {}) {
        const aux = path
        switch (action) {
            case this.GET:
                return await axios.get(aux, config);
            case this.PUT:
                return await axios.put(aux, data, config)
            case this.POST:
                config.headers['Content-Type'] =  'application/vnd.unimart.api.v1+json'
                return await axios.post(aux, data, config);
            case this.DELETE:
                return await axios.delete(aux, config);
            case this.PATCH:
                return await  axios.patch(aux, data, config)
            default:
                break;
        }
    }

    getBearerToken() {
        if (localStorage.getItem("token") !== null)
            return "Bearer " + localStorage.getItem("token");
        else
            return "Bearer " + sessionStorage.getItem("token");
    }


    getBasicToken(mail: string, password: string) {
        const credentials = mail + ":" + password;
        const hash = btoa(credentials);
        return "Basic " + hash;
    }
}