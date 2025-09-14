import {useState} from "react";
import {Link, useLocation } from "react-router"
import {Menu, X} from 'lucide-react';
import {useAuth} from "../hooks/useAuth.ts";

const Header = () => {
    const [menuOpen, setMenuOpen] = useState(false);
    const path = useLocation().pathname;
    const { logoutUser } = useAuth();
    let navLinks;

    if (path === "/") {
        navLinks = (
            <>
                <Link
                    to="/login"
                    className="block md:inline hover:underline hover:underline-offset-4 p-4 md:p-1"
                >
                    Book a Tour
                </Link>
                <Link
                    to="/guides"
                    className="block md:inline hover:underline hover:underline-offset-4 p-4 md:p-1"
                >
                    For our Guides
                </Link>
                <Link
                    to="login"
                    className="block md:inline hover:underline hover:underline-offset-4 p-4 md:p-1"
                >
                    Login
                </Link>
            </>
        );
    } else if (path.startsWith("/customers")) {
        navLinks = (
            <>
                <Link
                    to="/customers"
                    className="block md:inline hover:underline hover:underline-offset-4 p-4 md:p-1"
                >
                    Home
                </Link>
                <Link
                    to="/customers/booking-form"
                    className="block md:inline hover:underline hover:underline-offset-4 p-4 md:p-1"
                >
                    Make a Booking
                </Link>
                <Link
                    to="/customers/my-bookings"
                    className="block md:inline hover:underline hover:underline-offset-4 p-4 md:p-1"
                >
                    My Bookings
                </Link>
                <Link
                    onClick={logoutUser}
                    to="/"
                    className="block md:inline hover:underline hover:underline-offset-4 p-4 md:p-1"
                >
                    Logout
                </Link>
            </>
            );
    } else if (path.startsWith("/guides")) {
        navLinks = (
            <>
                <Link
                    to="/guides"
                    className="block md:inline hover:underline hover:underline-offset-4 p-4 md:p-1"
                >
                    Home
                </Link>
                <Link
                    to="/guides/available-bookings"
                    className="block md:inline hover:underline hover:underline-offset-4 p-4 md:p-1"
                >
                    Find Your Next Tour
                </Link>
                <Link
                    to="/guides/my-bookings"
                    className="block md:inline hover:underline hover:underline-offset-4 p-4 md:p-1"
                >
                    My Tours
                </Link>
                <Link
                    onClick={logoutUser}
                    to="/"
                    className="block md:inline hover:underline hover:underline-offset-4 p-4 md:p-1"
                >
                    Logout
                </Link>
            </>
        );
    }

    return (
        <>
            <header className="bg-white fixed w-full z-50">
                <div className="container mx-auto px-4 py-4 flex items-center justify-between">
                    <img
                        className="h-12"
                        src="../../public/invanto-logotype.png"
                        alt="Logotype"
                    />
                    <button
                        className="md:hidden text-black focus:outline-none"
                        onClick={() => setMenuOpen(!menuOpen)}
                    >
                        {menuOpen ? <X size={36} /> : <Menu size={36}/>}
                    </button>
                    <nav
                        className={`${menuOpen ? "block" : "hidden"}
                        md:flex gap-6 text-black font-medium absolute md:static top-16 right-0 w-full md:w-auto bg-white md:bg-transparent px-4 py-4 md:py-0`}
                    >
                        {navLinks}
                    </nav>
                </div>
            </header>
        </>
    )
};

export default Header;