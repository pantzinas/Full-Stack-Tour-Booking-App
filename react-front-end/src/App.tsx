import {BrowserRouter, Routes, Route} from "react-router";
import HomePage from "./pages/HomePage.tsx";
import Layout from "./components/Layout";
import {AuthProvider} from "./context/AuthProvider.tsx";
import LoginPage from "./pages/LoginPage.tsx";
import BookingPage from "./pages/BookingPage.tsx";
import BookingListOfCustomerPage from "./pages/BookingListOfCustomerPage.tsx";
import BookingListOfGuidePage from "./pages/BookingListOfGuidePage.tsx";
import ProtectedRoute from "./components/ProtectedRoute.tsx";
import RegistrationPage from "./pages/RegistrationPage.tsx";

function App() {
  return (
    <>
        <AuthProvider>
            <BrowserRouter>
                <Routes>
                    <Route element={<Layout />}>
                        <Route index element={<HomePage />} />
                        <Route path="login" element={<LoginPage />} />
                        <Route path="register/customer" element={<RegistrationPage />}/>
                        <Route path="register/guide" element={<RegistrationPage />}/>

                        <Route path="customers" element={<ProtectedRoute />}>
                            <Route index element={<HomePage />}/>
                            <Route path="booking-form" element={<BookingPage />}/>
                            <Route path="my-bookings" element={<BookingListOfCustomerPage />}/>
                        </Route>

                        <Route path="guides" element={<ProtectedRoute />}>
                            <Route index element={<HomePage />}/>
                            <Route path="available-bookings" element={<BookingListOfGuidePage />}/>
                            <Route path="my-bookings" element={<BookingListOfGuidePage />}/>
                        </Route>

                    </Route>
                </Routes>
            </BrowserRouter>
        </AuthProvider>
    </>
  )
}

export default App
