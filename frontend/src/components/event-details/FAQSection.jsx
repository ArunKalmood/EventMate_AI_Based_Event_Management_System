// src/components/event-details/FAQSection.jsx
import React, { useState } from "react";

const DEFAULT_FAQ = [
  { q: "What is the refund policy?", a: "Refunds may be available up to 24 hours before the event." },
  { q: "Is parking available?", a: "Most venues provide parking options (paid or free)." },
  { q: "Are children allowed?", a: "Children are welcome unless the event is marked 18+." },
];

export default function FAQSection({ faqs = DEFAULT_FAQ }) {
  const [open, setOpen] = useState(null);

  return (
    <section className="bg-white rounded-xl p-4 border shadow-sm">
      <h3 className="text-xl font-semibold text-gray-900 mb-3">❓ Frequently Asked Questions</h3>
      <div className="space-y-3">
        {faqs.map((f, i) => (
          <div key={i} className="border rounded-lg p-3 bg-gray-50 cursor-pointer" onClick={() => setOpen(open === i ? null : i)}>
            <div className="flex justify-between items-center">
              <p className="font-medium text-gray-800">{f.q}</p>
              <span className="text-2xl">{open === i ? "−" : "+"}</span>
            </div>
            {open === i && <p className="mt-2 text-gray-700">{f.a}</p>}
          </div>
        ))}
      </div>
    </section>
  );
}
