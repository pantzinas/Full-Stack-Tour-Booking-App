import {type ReactNode, useEffect, useState} from "react";
import {jwtDecode} from "jwt-decode";
import {deleteCookie, getCookie, setCookie} from "../util/cookies.ts";
import {login, type LoginFields} from "../api/login.ts";
import { AuthContext } from "./AuthContext.ts";


type JwtPayload = {
    sub?: string, // username
    role?: "ADMIN" | "GUIDE" | "CUSTOMER";
    exp?: number;
    iat?: number;
    iss?: string;
}

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [token, setToken] = useState<string | null>(null);
    const [sub, setSub] = useState<string | null>(null);
    const [role, setRole] = useState<'ADMIN' | 'GUIDE' | 'CUSTOMER' | null>(null);
    const [exp, setExp] = useState<number>(0);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        const token = getCookie('token');
        setToken(token ?? null);
        if (token) {
            try {
                const decoded = jwtDecode<JwtPayload>(token);
                console.log(decoded);
                setSub(decoded.sub ?? null);
                setRole(decoded.role ?? null);
                setExp(decoded.exp ?? 0);
            } catch {
                setSub(null);
                setRole(null);
                setExp(0);
            }
        } else {
            setSub(null);
            setRole(null);
            setExp(0);
        }
        setLoading(false);
    }, [])

    const loginUser = async (fields: LoginFields): Promise<"ADMIN" | "CUSTOMER" | "GUIDE" | null> => {
        const res = await login(fields);
        setCookie("token", res.token, {
            expires: new Date(exp * 1000),
            sameSite: "Lax",
            secure: false,
            path: "/"
        });

        setToken(res.token);

        try {
            const decoded = jwtDecode<JwtPayload>(res.token);
            setSub(decoded.sub ?? null);
            setRole(decoded.role ?? null);
            setExp(decoded.exp ?? 0);

            return decoded.role ?? null;
        } catch {
            setSub(null);
            setRole(null);
            setExp(0);

            return null;
        }
    }

    const logoutUser = () => {
        deleteCookie("token");
        setToken(null);
        setSub(null);
        setRole(null);
        setExp(0);
    }

    return (
        <AuthContext.Provider
            value={{
                isAuthenticated: !!token,
                token,
                sub,
                role,
                loginUser,
                logoutUser,
                loading,
            }}
        >
            {loading ? null : children}
        </AuthContext.Provider>
    );


}