import { z } from "zod";

export const loginSchema = z.object({
    username: z.string().min(1, "Username is required"),
    password: z.string().min(1, "Password is required")
})

export type LoginFields = z.infer<typeof loginSchema>;

export type LoginResponse = {
    firstname: string;
    lastname: string;
    token: string;
}

export async function login(fields: LoginFields): Promise<LoginResponse> {

    const res = await fetch(import.meta.env.VITE_API_URL + "auth/authenticate", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(fields)
    });

    if (!res.ok) {
        let detail = "Login Failed";
        try {
            const data = await res.json();
            if (typeof data?.detail == "string") detail = data.detail;
        } catch (e) {
            console.error(e);
        }
        throw new Error(detail);
    }
    return await res.json();
}