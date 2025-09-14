import {useEffect, useState} from "react";
import {
    type BookingListForGuide,
    getBookingsofGuide, getBookingsWithoutGuide, updateBookingByGuide
} from "../api/bookings.ts";
import {useLocation, useNavigate} from "react-router";
import {toast} from "sonner";
import {Button} from "../components/ui/button.tsx";
import {CirclePlus } from "lucide-react";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "../components/ui/table.tsx";
import {useAuth} from "../hooks/useAuth.ts";

const BookingListOfGuidePage = () => {
    const [bookings, setBookings] = useState<BookingListForGuide[]>([]);
    const [loading, setLoading] = useState(true);
    const [updating, setUpdating] = useState<number | null>(null);
    const navigate = useNavigate();

    const location = useLocation();
    const path = location.pathname;
    const { token } = useAuth();

    useEffect(() => {
        const fetchBookings = async () => {
            try {
                setLoading(true);

                let data: BookingListForGuide[] = [];

                if (path === "/guides/available-bookings") {
                    data = await getBookingsWithoutGuide(token)
                } else if (path === "/guides/my-bookings") {
                    data = await getBookingsofGuide(token)
                }

                setBookings(data);
            } catch (error) {
                console.error("Failed to fetch bookings:", error);
            } finally {
                setLoading(false);
            }
        }

        fetchBookings();
    }, [token, path]);

    const handleSubmit = async (id: number) => {
        if (!window.confirm("Do you want to guide this tour?")) return;
        setUpdating(id);
        try {
            await updateBookingByGuide(id, token);
            setBookings((prev) => prev.filter((p) => p.id !== id));
            toast.success("Booking was updated successfully!");
        } catch (error) {
            toast.error(
                error instanceof Error && error.message
                    ? error.message
                    : "Error at updating the booking.",
            );
        } finally {
            setUpdating(null);
        }
    };

    if (loading) return <div className="p-8">Loading...</div>;

    return (
        <>
            <div className="p-8">
                <div className="flex justify-between items-center mb-6">
                    {path === "/guides/available-bookings" && (
                        <>
                            <h1 className="text-2xl font-bold">Available Tours to Guide</h1>
                            <Button className="bg-app-blue text-white" onClick={() => navigate("/guides/my-bookings")}>
                                Your Upcoming Tours
                            </Button>
                        </>
                    )}
                    {path === "/guides/my-bookings" && (
                        <>
                            <h1 className="text-2xl font-bold">My Guides</h1>
                            <Button className="bg-app-blue text-white" onClick={() => navigate("/guides/available-bookings")}>
                                Do you want to guide more tours?
                            </Button>
                        </>
                    )}
                </div>
                <Table>
                    <TableHeader className="bg-gray-50">
                        <TableRow>
                            <TableHead>Booking ID</TableHead>
                            <TableHead>Date</TableHead>
                            <TableHead>Tour Category</TableHead>
                            <TableHead>Price (in Euro)</TableHead>
                            <TableHead>Customer ID</TableHead>
                            <TableHead>Customer Surname</TableHead>
                            {path === "/guides/available-bookings" && (
                                <>
                                    <TableHead className="w-32 text-right">Guide the tour</TableHead>
                                </>
                            )}
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {bookings.map((booking) => (
                            <TableRow key={booking.id}>
                                <TableCell>{booking.id}</TableCell>
                                <TableCell>{booking.bookingDate}</TableCell>
                                <TableCell>{booking.tourCategory}</TableCell>
                                <TableCell>{booking.tourPrice}</TableCell>
                                <TableCell>{booking.customerId}</TableCell>
                                <TableCell>{booking.customerLastname}</TableCell>
                                {path === "/guides/available-bookings" && (
                                    <>
                                        <TableCell className="text-right space-x-2">
                                            <Button
                                                variant="destructive"
                                                size="icon"
                                                onClick={() => handleSubmit(booking.id)}
                                                disabled={updating === booking.id}
                                                aria-label="Guide the tour"
                                            >
                                                <CirclePlus className="w-15 h-15 text-black" />
                                            </Button>
                                        </TableCell>
                                    </>
                                )}
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </div>
        </>
    );
}

export default BookingListOfGuidePage;