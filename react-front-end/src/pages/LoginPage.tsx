import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { type LoginFields, loginSchema } from "../api/login.ts";
import { Input } from "../components/ui/input.tsx";
import { Button } from "../components/ui/button.tsx";
import { Label } from "../components/ui/label.tsx";
import { useAuth } from "../hooks/useAuth";
import { toast } from "sonner";
import { Link, useNavigate } from "react-router";

export default function LoginPage() {
    const { loginUser } = useAuth();
    const navigate = useNavigate();

    const {
        register,
        handleSubmit,
        formState: { errors, isSubmitting },
    } = useForm<LoginFields>({
        resolver: zodResolver(loginSchema),
    });

    const onSubmit = async (data: LoginFields) => {
        try {
            const newRole = await loginUser(data);
            toast.success("Login successful");

            if (newRole === "ADMIN") {
                navigate("/admin");
            } else if (newRole === "CUSTOMER") {
                navigate("/customers");
            } else if (newRole === "GUIDE") {
                navigate("/guides");
            }
        } catch (err) {
            toast.error(err instanceof Error ? err.message : "Login failed");
        }
    };

    return (
        <>
            <form
                onSubmit={handleSubmit(onSubmit)}
                className="max-w-sm mx-auto p-8 space-y-4 border rounded"
            >
                <h1 className="text-xl text-app-blue font-bold underline">Login</h1>
                <div>
                    <Label htmlFor="username" className="mb-1">Username</Label>
                    <Input
                        id="username"
                        autoFocus
                        {...register("username")}
                        disabled={isSubmitting}
                    />
                    {errors.username && (
                        <div className="text-cf-dark-red">{errors.username.message}</div>
                    )}
                </div>

                <div>
                    <Label htmlFor="password" className="mb-1">Password</Label>
                    <Input
                        id="password"
                        type="password"
                        autoFocus
                        {...register("password")}
                        disabled={isSubmitting}
                    />
                    {errors.password && (
                        <div className="text-cf-dark-red">{errors.password.message}</div>
                    )}
                </div>

                <Button type="submit" disabled={isSubmitting} className="bg-app-blue text-white">
                    {isSubmitting ? "Logging ..." : "Login"}
                </Button>

                <p className="text-sm text-center text-gray-600 mt-4 text-left">
                    Don't have an account? <br/> Are you {" "}
                    <Link to="/register/customer" className="text-app-blue hover:underline">
                        Customer
                    </Link>
                    <span>{" "} or {" "}</span>
                    <Link to="/register/guide" className="text-app-blue hover:underline">
                        Guide
                    </Link>
                    <span>{" "} ?</span>
                </p>
            </form>
        </>
    );
}