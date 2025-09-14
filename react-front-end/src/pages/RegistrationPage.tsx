import {type FieldErrors, useForm} from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Input } from "../components/ui/input.tsx";
import { Button } from "../components/ui/button.tsx";
import { Label } from "../components/ui/label.tsx";
import { toast } from "sonner";
import {useLocation, useNavigate} from "react-router";
import {
    registerCustomer,
    type RegisterCustomerFields,
    RegisterCustomerSchema,
    type RegisterFields,
    registerGuide,
    RegisterSchema
} from "../api/register.ts";
import {useEffect} from "react";

export default function RegistrationPage() {
    const navigate = useNavigate();
    const location = useLocation();
    const path = location.pathname;

    const isCustomer = path.includes("/register/customer");
    const isGuide = path.includes("/register/guide");

    const tourCategories = ["URBAN", "SIGHTSEEING", "HIKING", "WELLNESS", "FOOD"] as const;
    const genderTypes = ["MALE", "FEMALE", "OTHER"] as const;

    const {
        register,
        handleSubmit,
        formState: { errors, isSubmitting },
        setValue,
    } = useForm<RegisterFields | RegisterCustomerFields>({
        resolver: zodResolver(isCustomer ? RegisterCustomerSchema : RegisterSchema),
    });

    useEffect(() => {
        if (isCustomer) {
            setValue("userInsertDTO.role", "CUSTOMER");
        } else if (isGuide) {
            setValue("userInsertDTO.role", "GUIDE")
        }
        setValue("userInsertDTO.isActive", true);
        setValue("isActive", true);
    }, [isCustomer, isGuide, setValue]);

    const onSubmit = async (data: RegisterFields | RegisterCustomerFields) => {
        try {
            if (isCustomer) {
                await registerCustomer(data as RegisterCustomerFields);
            } else if(isGuide) {
                await registerGuide(data as RegisterFields)
            }

            toast.success("Successfully registered!");
            navigate("/login");
        } catch (err) {
            toast.error(err instanceof Error ? err.message : "Registration failed");
        }
    };

    function isGuideForm(
        errors: FieldErrors<RegisterFields> | FieldErrors<RegisterCustomerFields>
    ): errors is FieldErrors<RegisterFields> {
        return "tourCategory" in errors;
    }

    return (
        <>
            <form
                onSubmit={handleSubmit(onSubmit)}
                className="max-w-xl mx-auto p-8 space-y-4 border rounded"
            >
                <h1 className="text-xl text-app-blue font-bold underline">Registration Form</h1>
                <div>
                    <Label htmlFor="username" className="mb-1">Username</Label>
                    <Input
                        id="username"
                        autoFocus
                        {...register("userInsertDTO.username")}
                        disabled={isSubmitting}
                    />
                    {errors.userInsertDTO?.username && (
                        <div className="text-cf-dark-red">{errors.userInsertDTO.username.message}</div>
                    )}
                </div>
                <div>
                    <Label htmlFor="password" className="mb-1">Password</Label>
                    <Input
                        id="password"
                        type="password"
                        autoFocus
                        {...register("userInsertDTO.password")}
                        disabled={isSubmitting}
                    />
                    {errors.userInsertDTO?.password && (
                        <div className="text-cf-dark-red">{errors.userInsertDTO.password.message}</div>
                    )}
                </div>
                <div>
                    <Label htmlFor="firstname" className="mb-1">First Name</Label>
                    <Input
                        id="firstname"
                        type="firstname"
                        autoFocus
                        {...register("userInsertDTO.firstname")}
                        disabled={isSubmitting}
                    />
                    {errors.userInsertDTO?.firstname && (
                        <div className="text-cf-dark-red">{errors.userInsertDTO.firstname.message}</div>
                    )}
                </div>
                <div>
                    <Label htmlFor="lastname" className="mb-1">Last Name</Label>
                    <Input
                        id="lastname"
                        type="lastname"
                        autoFocus
                        {...register("userInsertDTO.lastname")}
                        disabled={isSubmitting}
                    />
                    {errors.userInsertDTO?.lastname && (
                        <div className="text-cf-dark-red">{errors.userInsertDTO.lastname.message}</div>
                    )}
                </div>
                <div>
                    <Label htmlFor="email" className="mb-1">Email</Label>
                    <Input
                        id="email"
                        type="email"
                        autoFocus
                        {...register("userInsertDTO.email")}
                        disabled={isSubmitting}
                    />
                    {errors.userInsertDTO?.email && (
                        <div className="text-cf-dark-red">{errors.userInsertDTO.email.message}</div>
                    )}
                </div>
                <div>
                    <Label htmlFor="vat" className="mb-1">VAT Number</Label>
                    <Input
                        id="vat"
                        type="vat"
                        autoFocus
                        {...register("userInsertDTO.vat")}
                        disabled={isSubmitting}
                    />
                    {errors.userInsertDTO?.vat && (
                        <div className="text-cf-dark-red">{errors.userInsertDTO.vat.message}</div>
                    )}
                </div>
                <div>
                    <Label htmlFor="dateOfBirth" className="mb-1">Date of Birth</Label>
                    <Input
                        id="dateOfBirth"
                        type="dateOfBirth"
                        autoFocus
                        {...register("userInsertDTO.dateOfBirth")}
                        disabled={isSubmitting}
                    />
                    {errors.userInsertDTO?.dateOfBirth && (
                        <div className="text-cf-dark-red">{errors.userInsertDTO.dateOfBirth.message}</div>
                    )}
                </div>
                <div>
                    <Label htmlFor="genderType" className="mb-1">Gender</Label>
                    <select
                        id="genderType"
                        className="block w-full border border-gray-300 rounded-md px-3 py-2 text-sm"
                        disabled={isSubmitting}
                        {...register("userInsertDTO.genderType")}
                    >
                        <option value="" disabled hidden></option>
                        {genderTypes.map((gender) => (
                            <option key={gender} value={gender}>
                                {gender.charAt(0) + gender.slice(1)}
                            </option>
                        ))}
                    </select>
                    {errors.userInsertDTO?.genderType && (
                        <div className="text-cf-dark-red">{errors.userInsertDTO.genderType.message}</div>
                    )}
                </div>
                <div>
                    <Label htmlFor="nationality" className="mb-1">Nationality</Label>
                    <Input
                        id="nationality"
                        type="nationality"
                        autoFocus
                        {...register("userInsertDTO.nationality")}
                        disabled={isSubmitting}
                    />
                    {errors.userInsertDTO?.nationality && (
                        <div className="text-cf-dark-red">{errors.userInsertDTO.nationality.message}</div>
                    )}
                </div>

                {!isCustomer && (
                    <>
                        <div>
                            <Label htmlFor="tourCategory" className="mb-1">Tour Category</Label>
                            <select
                                id="tourCategory"
                                className="block w-full border border-gray-300 rounded-md px-3 py-2 text-sm"
                                disabled={isSubmitting}
                                {...register("tourCategory")}
                            >
                                <option value="" disabled hidden>Select a category</option>
                                {tourCategories.map((category) => (
                                    <option key={category} value={category}>
                                        {category.charAt(0) + category.slice(1)}
                                    </option>
                                ))}
                            </select>
                            {!isCustomer && isGuideForm(errors) && errors.tourCategory && (
                                <div className="text-red-800 text-sm mt-2">
                                    {errors.tourCategory.message}
                                </div>
                            )}
                        </div>
                    </>
                )}

                <Button type="submit" disabled={isSubmitting} className="bg-app-blue text-white">
                    {isSubmitting ? "Loading ..." : "Register"}
                </Button>

            </form>
        </>
    );
}