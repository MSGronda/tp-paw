import {axiosService} from "./index";

const USER_PATH: string = "users";

const login = async (mail: string, password: string) => {
    try {
        let config: Record<string, any> = {}
        config['headers'] =  {Authorization: axiosService.getBasicToken(mail, password)}

        const response = await axiosService.axiosWrapper(axiosService.GET, `${USER_PATH}`, config);

        if (!response || !response.config.headers.Authorization) {
            console.error("Unable to login");
            return false;
        }

        const token = response.config.headers.Authorization.split(" ")[1];
        console.log(response)
        if (token) localStorage.setItem('token', token);
        console.log(response.data)
        if (response.data) localStorage.setItem('user', JSON.stringify(response.data));
    } catch (err) {
        console.error(err);
        return false;
    }
    return true;
};

const logout = () => {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
};

const getCurrentUser = () => {
    let user = localStorage.getItem('user')
    if(user){
        return JSON.parse(user);
    }
    return ""
};

export default {
    login,
    getCurrentUser,
    logout
};