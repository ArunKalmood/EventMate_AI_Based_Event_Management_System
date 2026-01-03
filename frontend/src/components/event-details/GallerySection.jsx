// src/components/event-details/GallerySection.jsx
import React from "react";

export default function GallerySection({ images = [] }) {
  if (!images || images.length === 0) return null;

  return (
    <section className="bg-white rounded-xl p-4 border shadow-sm">
      <h3 className="text-xl font-semibold text-gray-900 mb-3">Photo Gallery</h3>
      <div className="flex gap-4 overflow-x-auto pb-2">
        {images.map((url, i) => (
          <img
            key={i}
            src={url}
            alt={`gallery-${i}`}
            className="w-72 h-48 object-cover rounded-xl shadow-sm border"
            loading="lazy"
          />
        ))}
      </div>
    </section>
  );
}
