import { useState } from "react";
import React from "react";
import { jwtDecode, JwtPayload } from "jwt-decode";

type CustomJwtPayload = JwtPayload & { role: string; userId: number; exp: number };

export interface AuthContextInterface {
    isAuthenticated: boolean;
    logoutHandler: () => void;
    loginHandler: (token: string, refreshToken?: string) => Promise<void>;
    authKey?: string;
    refreshToken?: string;
    role?: string;
    email?: string;
    userId?: number;
    profileImage: string;
    updateProfileImage: (image: string) => void;
    setToken: (token?: string) => void;
    setRefreshToken: (refresh?: string) => void;

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
    role: undefined,
    email: undefined,
});

export const AuthContextProvider = ({ children }: { children: React.ReactNode }) => {
    const isInLocalStorage = !!localStorage.getItem("token") || !!localStorage.getItem("refresh");
    const isInSessionStorage = !!sessionStorage.getItem("token") || !!sessionStorage.getItem("refresh");
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(isInLocalStorage || isInSessionStorage);

    const token = isInLocalStorage ? localStorage.getItem("token") as string : sessionStorage.getItem("token") as string;
    const [authKey, setAuthKey] = useState<string | undefined>(token);

    const refreshTokenstorage = isInLocalStorage ? localStorage.getItem("refresh") as string : sessionStorage.getItem("refresh") as string;
    const [refreshToken, setRefreshTokenKey] = useState<string | undefined>(refreshTokenstorage);

    const [email, setEmail] = useState<string | undefined>(() => {
        try {
            return jwtDecode<CustomJwtPayload>(refreshToken as string).sub as string;
        } catch (e) {
            if (isAuthenticated) {
                console.error(e);
            }
        }
    });

    const [role, setRole] = useState<string | undefined>(() => {
        try {
            return jwtDecode<CustomJwtPayload>(refreshToken as string).role as string;
        } catch (error) {
            if (isAuthenticated) {
                console.error(error);
            }
        }
    });

    const [userId, setUserId] = useState<number | undefined>(() => {
        try {
            return jwtDecode<CustomJwtPayload>(refreshToken as string).userId;
        } catch (error) {
            if (isAuthenticated)
                console.error(error);
        }
    });

    const logoutHandler = () => {
        localStorage.removeItem("token");
        sessionStorage.removeItem("token");
        localStorage.removeItem("refresh");
        sessionStorage.removeItem("refresh");
        setIsAuthenticated(false);
        setAuthKey(undefined);
        setRefreshTokenKey(undefined);
        setEmail(undefined);
        setRole(undefined);
        setUserId(undefined);
    }


    const loginHandler = async (authKey: string, refreshToken?: string) => {
        try {
            setAuthKey(authKey);
            setRefreshTokenKey(refreshToken);
            setIsAuthenticated(true);
            setEmail(jwtDecode<CustomJwtPayload>(authKey as string).sub as string);
            setRole(jwtDecode<CustomJwtPayload>(authKey as string).role as string);
            setUserId(jwtDecode<CustomJwtPayload>(authKey as string).userId);
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
                role,
                userId,
                email,
                profileImage,
                updateProfileImage,
                setToken: setAuthKey,
                setRefreshToken: setRefreshTokenKey,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};


export default AuthContext;
