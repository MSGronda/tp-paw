import {User} from "../models/User.ts";
import {userService} from "../services";

export function isModerator(user?: User): boolean {
    user = user || userService.getUserData()!;
    
    return user?.roles.includes("EDITOR") || false;
}
