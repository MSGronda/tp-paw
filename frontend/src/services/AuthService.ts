import {axiosService} from "./index";

const USER_PATH: string = "users";

const login = async (mail: string, password: string, rememberMe: boolean) => {
    try {
        let config: Record<string, any> = {}
        config['headers'] =  {Authorization: axiosService.getBasicToken(mail, password)}

        const response = await axiosService.axiosWrapper(axiosService.GET, `${USER_PATH}`, config);
        if (!response || !response.data.id) {
            console.error("Unable to login");
            return false;
        }
        const token = response.headers.authorization.split(" ")[1];
        
        if (rememberMe){
            localStorage.setItem('token', token);
        } else{
            sessionStorage.setItem('token', token);
        }
        if (response.data) localStorage.setItem('user', JSON.stringify(response.data));
        return token;
    } catch (err) {
        console.error(err);
        return false;
    }
};

const logout = () => {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    sessionStorage.removeItem('token');
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