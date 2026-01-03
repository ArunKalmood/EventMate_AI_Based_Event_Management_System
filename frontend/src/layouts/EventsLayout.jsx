import EventDetailsNavbar from "../components/event-details/EventDetailsNavbar";

function EventsLayout({ children }) {
  return (
    <div className="min-h-screen bg-[#F8FAFC]">
      <EventDetailsNavbar />
      <main className="pt-20 w-full">
        {children}
      </main>
    </div>
  );
}

export default EventsLayout;
