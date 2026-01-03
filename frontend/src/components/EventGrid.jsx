import EventCard from "./EventCard";

function EventGrid() {
  const events = [
    {
      title: "Live Music Night — Indie Rock",
      category: "Music",
      price: "₹499",
      location: "Hitech City, Hyderabad",
      image: "https://images.unsplash.com/photo-1511192336575-5a79af67a629?w=600",
      date: { day: "12", month: "JAN" }
    },
    {
      title: "Startup Pitch & Networking Meetup",
      category: "Business",
      price: "Free Entry",
      location: "T-Hub Phase 2",
      image: "https://images.unsplash.com/photo-1556761175-4b46a572b786?w=600",
      date: { day: "18", month: "JAN" }
    },
    {
      title: "Comedy Stand-Up Night",
      category: "Comedy",
      price: "₹299",
      location: "Jubilee Hills",
      image: "https://images.unsplash.com/photo-1525182008055-f88b95ff7980?w=600",
      date: { day: "19", month: "JAN" }
    },
    {
      title: "DJ EDM Party — Neon Fever",
      category: "Party",
      price: "₹899",
      location: "Gachibowli Arena",
      image: "https://images.unsplash.com/photo-1504384308090-c894fdcc538d?w=600",
      date: { day: "21", month: "JAN" }
    }
  ];

  return (
    <section className="max-w-7xl mx-auto px-6 py-16">
      <h2 className="text-3xl font-bold text-gray-900 mb-10">Featured Events</h2>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
        {events.map((event, i) => (
          <EventCard key={i} event={event} />
        ))}
      </div>
    </section>
  );
}

export default EventGrid;
