

export class UserService {

    getUserId() {
        let userData;
        if (localStorage.getItem("user") != null)
            userData = localStorage.getItem("user");
        else if(sessionStorage.getItem("user") != null)
            userData = sessionStorage.getItem("user");
        if(!userData){
            return;
        }
        return JSON.parse(userData).id;
    }
}