import { Routes, Route, Navigate } from "react-router-dom";

import Layout from "./components/Layout";
import ProtectedRoute from "./components/ProtectedRoute";

import OrganizerLayout from "./layouts/OrganizerLayout";
import EventsLayout from "./layouts/EventsLayout";

import Home from "./pages/Home";
import EventsList from "./pages/EventsList";
import EventDetails from "./pages/EventDetails";
import CreateEvent from "./pages/CreateEvent";
import Login from "./pages/Login";
import MyBookings from "./pages/MyBookings";
import BookingConfirmation from "./pages/BookingConfirmation";
import ReportLostItem from "./pages/ReportLostItem";
import MyLostItems from "./pages/MyLostItems";
import OrganizerOnboarding from "./pages/OrganizerOnboarding";
import CuratedEvents from "./pages/CuratedEvents";

// Organizer pages
import OrganizerDashboard from "./pages/organizer/OrganizerDashboard";
import OrganizerBookings from "./pages/organizer/OrganizerBookings";
import OrganizerLostFound from "./pages/organizer/OrganizerLostFound";
import EventLostItems from "./pages/organizer/EventLostItems";

function App() {
  return (
    <Routes>
      {/* ROOT → LOGIN */}
      <Route path="/" element={<Navigate to="/login" replace />} />

      {/* LOGIN (PUBLIC) */}
      <Route path="/login" element={<Login />} />

      {/* APP AFTER LOGIN (PROTECTED) */}
      <Route
        path="/*"
        element={
          <ProtectedRoute>
            <Layout>
              <Routes>
                {/* USER LANDING */}
                <Route path="/home" element={<Home />} />

                {/* EVENTS LIST */}
                <Route
                  path="/events"
                  element={
                    <EventsLayout>
                      <EventsList />
                    </EventsLayout>
                  }
                />

                {/* EVENT DETAILS */}
                <Route
                  path="/events/:id"
                  element={
                    <EventsLayout>
                      <EventDetails />
                    </EventsLayout>
                  }
                />

                {/* REPORT LOST ITEM */}
                <Route
                  path="/events/:id/report-lost"
                  element={<ReportLostItem />}
                />

                {/* USER PAGES */}
                <Route path="/my-bookings" element={<MyBookings />} />
                <Route path="/my-lost-items" element={<MyLostItems />} />

                <Route
                  path="/booking/confirmation/:bookingId"
                  element={<BookingConfirmation />}
                />

                {/* 🔁 LEGACY CREATE (SAFE REDIRECT) */}
                <Route
                  path="/create"
                  element={<Navigate to="/organizer-onboarding" />}
                />

                {/* ORGANIZER ONBOARDING */}
                <Route
                  path="/organizer-onboarding"
                  element={
                    <EventsLayout>
                      <OrganizerOnboarding />
                    </EventsLayout>
                  }
                />

                {/* ORGANIZER DASHBOARD */}
                <Route
                  path="/organizer"
                  element={
                    <OrganizerLayout>
                      <OrganizerDashboard />
                    </OrganizerLayout>
                  }
                />

                <Route
                  path="/organizer/bookings"
                  element={
                    <OrganizerLayout>
                      <OrganizerBookings />
                    </OrganizerLayout>
                  }
                />

                <Route
                  path="/organizer/lost-found"
                  element={
                    <OrganizerLayout>
                      <OrganizerLostFound />
                    </OrganizerLayout>
                  }
                />

                <Route
                  path="/organizer/lost-found/:eventId"
                  element={
                    <OrganizerLayout>
                      <EventLostItems />
                    </OrganizerLayout>
                  }
                />

                {/* CREATE EVENT */}
                <Route
                  path="/organizer/create-event"
                  element={
                    <EventsLayout>
                      <CreateEvent />
                    </EventsLayout>
                  }
                />

                {/* CURATED EVENTS */}
                <Route
                  path="/curated"
                  element={
                    <EventsLayout>
                      <CuratedEvents />
                    </EventsLayout>
                  }
                />
              </Routes>
            </Layout>
          </ProtectedRoute>
        }
      />
    </Routes>
  );
}

export default App;
