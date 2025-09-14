import { z } from "zod";

const API_URL = import.meta.env.VITE_API_URL;

const tourCategories = ["URBAN", "SIGHTSEEING", "HIKING", "WELLNESS", "FOOD", ""] as const;

export const rawBookingSchema = z.object({
    id: z.number(),
    bookingDate: z.string().regex(/^\d{4}-\d{2}-\d{2}$/, {
        message: "Date must be in YYYY-MM-DD format",
    }),
    tourReadOnlyDTO: z.object({
        category: z.enum(tourCategories).nonoptional("Tour category is required"),
        price: z.number(),
    }),
    guideReadOnlyDTO: z
        .object({
            id: z.number().or(z.string()),
            userReadOnlyDTO: z
                .object({
                    firstname: z.string(),
                    lastname: z.string(),
                    vat: z.string()
                })
                .nullable(),
        })
        .nullable(),
    customerReadOnlyDTO: z
        .object({
            id: z.number().or(z.string()),
            userReadOnlyDTO: z
                .object({
                    firstname: z.string(),
                    lastname: z.string(),
                    vat: z.string(),
                })
                .nullable(),
        })
        .nullable(),
});

export const bookingListForCustomerSchema = rawBookingSchema.transform((data) => ({
    id: data.id,
    bookingDate: data.bookingDate,
    tourCategory: data.tourReadOnlyDTO.category,
    tourPrice: data.tourReadOnlyDTO.price,
    guideId: data.guideReadOnlyDTO?.id,
    guideLastname: data.guideReadOnlyDTO?.userReadOnlyDTO?.lastname ?? "",
}));

export const bookingListForGuideSchema = rawBookingSchema.transform((data) => ({
    id: data.id,
    bookingDate: data.bookingDate,
    tourCategory: data.tourReadOnlyDTO.category,
    tourPrice: data.tourReadOnlyDTO.price,
    customerId: data.customerReadOnlyDTO?.id,
    customerLastname: data.customerReadOnlyDTO?.userReadOnlyDTO?.lastname ?? ""
}));

export const bookingFormSchema = z.object({
    bookingDate: z.string().regex(/^\d{4}-\d{2}-\d{2}$/, { message: "Date must be in YYYY-MM-DD format" }),
    tourCategory: z.enum(tourCategories, {message: "Tour Category cannot be empty. Please select one of the tours"}),
});

export type BookingListForCustomer = z.infer<typeof bookingListForCustomerSchema>;
export type BookingListForGuide = z.infer<typeof bookingListForGuideSchema>;
export type BookingForm = z.infer<typeof bookingFormSchema>;

export async function getBookingsOfCustomer(token: string | null): Promise<BookingListForCustomer[]> {
    const res = await fetch(`${API_URL}customers/bookings`, {
        method: 'GET',
        headers: { "Authorization": "Bearer " + token },
    });
    if (!res.ok) throw new Error("Failed to fetch bookings of the customer");
    const data = await res.json();
    const parsed = z.array(bookingListForCustomerSchema).parse(data);

    console.log("Parsed bookings response", parsed);
    return parsed;
};

export async function getBookingsofGuide(token: string | null): Promise<BookingListForGuide[]> {
    const res = await fetch(`${API_URL}guides/bookings`, {
        method: 'GET',
        headers: { "Authorization": "Bearer " + token },
    });
    if (!res.ok) throw new Error("Failed to fetch bookings of the guide");
    const data = await res.json();
    const parsed = z.array(bookingListForGuideSchema).parse(data);

    console.log("Parsed bookings response", parsed);
    return parsed;
}

export async function getBookingsWithoutGuide(token: string | null): Promise<BookingListForGuide[]> {
    const res = await fetch(`${API_URL}guides/available-bookings`, {
        method: 'GET',
        headers: { "Authorization": "Bearer " + token },
    });
    if (!res.ok) throw new Error("Failed to fetch available bookings");
    const data = await res.json();
    const parsed = z.array(bookingListForGuideSchema).parse(data);

    console.log("Parsed bookings response", parsed);
    return parsed;
}

export async function createBooking(
    data: BookingForm, token: string | null
): Promise<BookingForm> {
    const res = await fetch(`${API_URL}customers/bookings/save`, {
        method: "POST",
        headers: { "Authorization": "Bearer " + token, "Content-Type": "application/json" },
        body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error("Failed to make a booking");
    return await res.json();
}

export async function deleteBooking(id: number, token: string | null): Promise<void> {
    const res = await fetch(`${API_URL}customers/bookings/delete/${id}`, {
        method: "DELETE",
        headers: { "Authorization": "Bearer " + token, "Content-Type": "application/json" },
    });
    if (!res.ok) throw new Error("Failed to delete booking");
}

export async function updateBookingByGuide(id: number, token: string | null): Promise<void> {
    const res = await fetch(`${API_URL}guides/bookings/save/${id}`, {
        method: "PUT",
        headers: { "Authorization": "Bearer " + token, "Content-Type": "application/json" },
    });
    if (!res.ok) throw new Error("Failed to register the guide into the booking");
}