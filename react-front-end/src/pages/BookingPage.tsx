import {useNavigate} from "react-router";
import {useForm} from "react-hook-form";
import {type BookingForm, bookingFormSchema, createBooking} from "../api/bookings.ts";
import {zodResolver} from "@hookform/resolvers/zod";
import {toast} from "sonner";
import {Label} from "../components/ui/label.tsx";
import {Input} from "../components/ui/input.tsx";
import {Button} from "../components/ui/button.tsx";
import {useAuth} from "../hooks/useAuth.ts";

const tourCategories = ["URBAN", "SIGHTSEEING", "HIKING", "WELLNESS", "FOOD", ""] as const;

const BookingPage = () => {
    const navigate = useNavigate();
    const { token } = useAuth();

    const {
        register,
        handleSubmit,
        formState: { errors, isSubmitting }
    } = useForm<BookingForm>({
        resolver: zodResolver(bookingFormSchema),
        defaultValues: {
            bookingDate: "",
            tourCategory: ""
        }
    });

    const onSubmit = async (data: BookingForm) => {
        try {
            if (!token) throw new Error("No auth token found");
            await createBooking(data, token);
            toast.success("Booking created successfully.");
            navigate("/customers");
        } catch (error) {
            toast.error(
                error instanceof Error ? error.message : "Something went wrong."
            );
        }
    };

    return (
        <>
            <form
                onSubmit={handleSubmit(onSubmit)}
                className="max-w-xl mx-auto mt-12 p-8 border rounded-md space-y-6 bg-white"
                autoComplete="off"
            >
                <h1 className="text-xl font-bold mb-4">Make a new booking</h1>
                <div>
                    <Label htmlFor="bookingDate">Booking Date</Label>
                    <Input className="mt-4" id="date" {...register("bookingDate")}/>
                    {errors.bookingDate && (
                        <div className="text-red-800 text-sm mt-2">
                            {errors.bookingDate.message}
                        </div>
                    )}
                </div>
                <div>
                    <Label htmlFor="tourCategory">Tour Category</Label>
                    <select
                        id="tourCategory"
                        className="mt-4 block w-full border border-gray-300 rounded-md px-3 py-2 text-sm"
                        {...register("tourCategory")}
                    >
                        <option value="" disabled hidden>Select a category</option>
                        {tourCategories.map((category) => (
                            <option key={category} value={category}>
                                {category.charAt(0) + category.slice(1)}
                            </option>
                        ))}
                    </select>
                    {errors.tourCategory && (
                        <div className="text-red-800 text-sm mt-2">
                            {errors.tourCategory.message}
                        </div>
                    )}
                </div>
                <Button type="submit" disabled={isSubmitting} className="w-full bg-app-blue text-white">
                    {isSubmitting ? "Submitting..." : "Submit"}
                </Button>
            </form>
        </>
    );
};

export default BookingPage;