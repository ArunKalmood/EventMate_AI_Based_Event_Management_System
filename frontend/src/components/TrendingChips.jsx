import React from "react";
import { useNavigate } from "react-router-dom";

const categories = [
  {
    label: "Music",
    value: "MUSIC",
    img: "https://images.unsplash.com/photo-1507874457470-272b3c8d8ee2?w=800",
  },
  {
    label: "Business",
    value: "BUSINESS",
    img: "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=800",
  },
  {
    label: "Concerts",
    value: "CONCERT",
    img: "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=800",
  },
  {
    label: "Parties",
    value: "PARTY",
    img: "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=800",
  },
  {
    label: "Food & Drinks",
    value: "FOOD",
    img: "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=800",
  },
  {
    label: "Comedy",
    value: "COMEDY",
    img: "https://images.unsplash.com/photo-1527224857830-43a7acc85260?w=800",
  },
  {
    label: "Festivals",
    value: "FESTIVAL",
    img: "/public/istockphoto-473534924-612x612.jpg",
  },
  {
    label: "Health & Wellness",
    value: "WELLNESS",
    img: "https://images.unsplash.com/photo-1545205597-3d9d02c29597?w=800",
  },
];

function TrendingChips() {
  const navigate = useNavigate();

  const handleClick = (categoryValue) => {
    navigate(`/events?category=${categoryValue}`);
  };

  return (
    <section className="max-w-7xl mx-auto px-6 py-16">
      <h2 className="text-3xl font-bold text-gray-900 mb-8">
        Trending Categories
      </h2>

      <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-6">
        {categories.map((cat) => (
          <div
            key={cat.value}
            onClick={() => handleClick(cat.value)}
            className="relative h-40 rounded-xl overflow-hidden cursor-pointer group"
          >
            <img
              src={cat.img}
              alt={cat.label}
              className="absolute inset-0 h-full w-full object-cover transition-transform duration-300 group-hover:scale-105"
            />

            {/* dark overlay */}
            <div className="absolute inset-0 bg-black/40" />

            <span className="absolute bottom-4 left-4 text-white text-lg font-medium">
              {cat.label}
            </span>
          </div>
        ))}
      </div>
    </section>
  );
}

export default TrendingChips;
