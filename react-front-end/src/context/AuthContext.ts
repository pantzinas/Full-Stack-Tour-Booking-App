import {createContext} from "react";
import type {LoginFields} from "../api/login.ts";


type AuthContextProps = {
    isAuthenticated: boolean;
    token: string | null;
    sub: string | null;
    role: "ADMIN" | "GUIDE" | "CUSTOMER" | null;
    loginUser: (fields: LoginFields) => Promise<"ADMIN" | "CUSTOMER" | "GUIDE" | null>;
    logoutUser: () => void;
    loading: boolean;
}

export const AuthContext = createContext<AuthContextProps | undefined>(undefined);