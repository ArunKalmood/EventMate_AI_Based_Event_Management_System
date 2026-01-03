const images = [
  "/moments/concert.jpg",
  "/moments/workshop.jpg",
  "/moments/festival.jpg",
  "/moments/meetup.jpg",
  "/moments/crowd.jpg",
  "/moments/speaker.jpg",
];

function EventMateMoments() {
  return (
    <section className="bg-gradient-to-b from-slate-50 to-white py-16">
      <div className="max-w-6xl mx-auto px-6 mb-10 text-center">
        <h3 className="text-2xl md:text-3xl font-semibold text-brand">
          Moments made on EventMate
        </h3>
        <p className="mt-2 text-gray-600">
          Real events. Real people. Happening every day.
        </p>
      </div>

      <div className="relative overflow-hidden">
        <div className="flex gap-6 animate-marquee hover:[animation-play-state:paused]">
          {[...images, ...images].map((src, i) => (
            <div
              key={i}
              className="min-w-[260px] h-[180px] rounded-xl overflow-hidden shadow-md
                         transition-transform duration-300 hover:scale-105"
            >
              <img
                src={src}
                alt=""
                className="w-full h-full object-cover"
              />
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

export default EventMateMoments;
