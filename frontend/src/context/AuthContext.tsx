import { useState } from "react";
import React from "react";
import { jwtDecode, JwtPayload } from "jwt-decode";

type CustomJwtPayload = JwtPayload & { roles: string; userUrl: string; exp: number };

export interface AuthContextInterface {
    isAuthenticated: boolean;
    logoutHandler: () => void;
    loginHandler: (token: string, refreshToken?: string) => Promise<void>;
    authKey?: string | undefined;
    refreshToken?: string | undefined;
    // role?: string | undefined;
    userId?: number | undefined;
    profileImage: String;
    updateProfileImage: (image: String) => void;
    setToken: React.Dispatch<React.SetStateAction<string | undefined>>;
    setRefreshToken: React.Dispatch<React.SetStateAction<string | undefined>>;

}

const AuthContext = React.createContext<AuthContextInterface>({
    isAuthenticated: false,
    profileImage: "",
    updateProfileImage: () => { },
    logoutHandler: () => { },
    loginHandler: async () => { },
    setToken: () => { },
    setRefreshToken: () => { },
    userId: undefined,
});

export const AuthContextProvider = ({ children }: { children: React.ReactNode }) => {
    const isInLocalStorage = localStorage.hasOwnProperty("token");
    const isInSessionStorage = sessionStorage.hasOwnProperty("token");
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(isInLocalStorage || isInSessionStorage);

    const token = isInLocalStorage ? localStorage.getItem("token") as string : sessionStorage.getItem("token") as string;
    const [authKey, setAuthKey] = useState<string | undefined>(token);

    const refreshTokenstorage = isInLocalStorage ? localStorage.getItem("refreshToken") as string : sessionStorage.getItem("refreshToken") as string;
    const [refreshToken, setRefreshTokenKey] = useState<string | undefined>(refreshTokenstorage);

    // const [email, setEmail] = useState<string | undefined>(() => {
    //     try {
    //         return jwtDecode<CustomJwtPayload>(token as string).sub as string;
    //     } catch (e) {
    //         if (isAuthenticated) {
    //             console.error(e);
    //         }
    //     }
    // });

    //TODO revisar
    // const [role, setRole] = useState<string | undefined>(() => {
    //     try {
    //         return jwtDecode<CustomJwtPayload>(token as string).authorization as string;
    //     } catch (error) {
    //         if (isAuthenticated) {
    //             console.error(error);
    //         }
    //     }
    // });

    const [userId, setUserId] = useState<number | undefined>(() => {
        try {
            // console.log(jwtDecode<CustomJwtPayload>(token as string))
            return JSON.parse(localStorage.getItem("user") as string).id
        } catch (error) {
            if (isAuthenticated)
                console.error(error);
        }
    });

    const logoutHandler = () => {
        localStorage.removeItem("token");
        sessionStorage.removeItem("token");
        localStorage.removeItem("refreshToken");
        sessionStorage.removeItem("refreshToken");
        setIsAuthenticated(false);
        setAuthKey(undefined);
        setRefreshTokenKey(undefined);
        // setEmail(undefined);
        // setRole(undefined);
        // setUserId(undefined);
    }


    const loginHandler = async (authKey: string, refreshToken?: string) => {
        try {
            setAuthKey(authKey);
            setRefreshTokenKey(refreshToken);
            setIsAuthenticated(true);
            //console.log(jwtDecode<CustomJwtPayload>(authKey as string))
            // setEmail(jwtDecode<CustomJwtPayload>(authKey as string).sub as string);
            // setRole(jwtDecode<CustomJwtPayload>(authKey as string).roles as string);
            const id = JSON.parse(localStorage.getItem("user") as string).id
            if (id) setUserId(parseInt(id));
        } catch (e) {
            console.error(e);
        }
    };

    const [profileImage, updateProfileImage] = useState<string>("");


    return (
        <AuthContext.Provider
            value={{
                isAuthenticated,
                logoutHandler,
                loginHandler,
                authKey,
                refreshToken,
                // role,
                userId,
                // email,
                profileImage,
                updateProfileImage,
                setAuthKey,
                setRefreshTokenKey,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};


export default AuthContext;