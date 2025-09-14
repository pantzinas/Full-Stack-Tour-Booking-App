import {useEffect, useState} from "react";
import {type BookingListForCustomer, deleteBooking, getBookingsOfCustomer} from "../api/bookings.ts";
import {useLocation, useNavigate} from "react-router";
import {toast} from "sonner";
import {Button} from "../components/ui/button.tsx";
import {Trash } from "lucide-react";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "../components/ui/table.tsx";
import {useAuth} from "../hooks/useAuth.ts";

const BookingListOfCustomerPage = () => {
    const [bookingsOfCustomer, setBookingsOfCustomer] = useState<BookingListForCustomer[]>([]);
    const [loading, setLoading] = useState(true);
    const [deleting, setDeleting] = useState<number | null>(null);
    const navigate = useNavigate();

    const location = useLocation();
    const { token } = useAuth();

    useEffect(() => {
        getBookingsOfCustomer(token)
            .then((data) => setBookingsOfCustomer(data))
            .finally(() => setLoading(false));
    }, [token, location]);

    const handleDelete = async (id: number) => {
        if (!window.confirm("Delete this booking?")) return;
        setDeleting(id);
        try {
            await deleteBooking(id, token);
            setBookingsOfCustomer((prev) => prev.filter((p) => p.id !== id));
            toast.success("Booking was deleted successfully!");
        } catch (error) {
            toast.error(
                error instanceof Error && error.message
                    ? error.message
                    : "Error at deleting the booking.",
            );
        } finally {
            setDeleting(null);
        }
    };

    if (loading) return <div className="p-8">Loading...</div>;

    return (
        <>
            <div className="p-8">
                <div className="flex justify-between items-center mb-6">
                    <h1 className="text-2xl font-bold">Your Tour Bookings</h1>
                    <Button className="bg-app-blue text-white" onClick={() => navigate("/customers/booking-form")}>
                        + New Booking
                    </Button>
                </div>
                <Table>
                    <TableHeader className="bg-gray-50">
                        <TableRow>
                            <TableHead>Booking ID</TableHead>
                            <TableHead>Date</TableHead>
                            <TableHead>Tour Category</TableHead>
                            <TableHead>Price (in Euro)</TableHead>
                            <TableHead>Guide ID</TableHead>
                            <TableHead>Guide Surname</TableHead>
                            <TableHead className="w-32 text-right">Delete</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {bookingsOfCustomer.map((booking) => (
                            <TableRow key={booking.id}>
                                <TableCell>{booking.id}</TableCell>
                                <TableCell>{booking.bookingDate}</TableCell>
                                <TableCell>{booking.tourCategory}</TableCell>
                                <TableCell>{booking.tourPrice}</TableCell>
                                <TableCell>{booking.guideId}</TableCell>
                                <TableCell>{booking.guideLastname}</TableCell>
                                <TableCell className="text-right space-x-2">
                                    <Button
                                        variant="destructive"
                                        size="icon"
                                        onClick={() => handleDelete(booking.id)}
                                        disabled={deleting === booking.id}
                                        aria-label="Delete"
                                    >
                                        <Trash className="w-8 h-8 text-red-800" />
                                    </Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </div>
        </>
    );
}

export default BookingListOfCustomerPage;