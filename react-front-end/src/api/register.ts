import { z } from "zod";

const genderTypes = ["MALE", "FEMALE", "OTHER"] as const;
const roles = ["ADMIN", "CUSTOMER", "GUIDE"] as const;
const tourCategories = ["URBAN", "SIGHTSEEING", "HIKING", "WELLNESS", "FOOD"] as const;
const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$/;

export const RegisterSchema = z.object({
    isActive: z.literal(true),
    userInsertDTO: z.object({
        username: z.string().min(1, "Username is required"),
        password: z.string().regex(passwordRegex, "Password must be at least 8 characters and include uppercase, lowercase, number, and special character."),
        firstname: z.string().min(1, "First name is required"),
        lastname: z.string().min(1, "Last name is required"),
        email: z.string().min(1, "Email is required"),
        vat: z.string().min(9, "Vat is required"),
        dateOfBirth: z.string().regex(/^\d{4}-\d{2}-\d{2}$/, {
            message: "Date must be in YYYY-MM-DD format",
        }),
        genderType: z.enum(genderTypes, { message: "Gender is required" }),
        nationality: z.string().min(1, "Nationality is required"),
        role: z.enum(roles, { message: "Role is required" }),
        isActive: z.literal(true)
    }),
    tourCategory: z.enum(tourCategories, { message: "Tour category is required" }),
});

export const RegisterCustomerSchema = RegisterSchema.omit({ tourCategory: true })

export const ResponseSchema = z.object({
    id: z.number(),
    uuid: z.string(),
    isActive: z.boolean(),
    userReadOnlyDTO: z.object({
        firstname: z.string().min(1, "First name is required"),
        lastname: z.string().min(1, "Last name is required"),
        vat: z.string().min(9, "Vat is required"),
        }),
    tourReadOnlyDTO: z.object({
        id: z.number(),
        category: z.enum(tourCategories),
        price: z.number(),
    })
});

export const ResponseCustomerSchema = ResponseSchema.omit({ tourReadOnlyDTO: true })

export type RegisterFields = z.infer<typeof RegisterSchema>;
export type ResponseFields = z.infer<typeof ResponseSchema>;

export type RegisterCustomerFields = z.infer<typeof RegisterCustomerSchema>;
export type ResponseCustomerFields = z.infer<typeof ResponseCustomerSchema>;

export async function registerGuide(fields: RegisterFields): Promise<ResponseFields> {

    const res = await fetch(import.meta.env.VITE_API_URL + "guides/save", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(fields)
    });

    if (!res.ok) {
        let detail = "Register Failed";
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

export async function registerCustomer(fields: RegisterCustomerFields): Promise<ResponseCustomerFields> {

    const res = await fetch(import.meta.env.VITE_API_URL + "customers/save", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(fields)
    });

    if (!res.ok) {
        let detail = "Register Failed";
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