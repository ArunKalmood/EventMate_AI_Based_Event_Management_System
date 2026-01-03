// src/components/event-details/ShareSection.jsx
import React, { useState } from "react";

export default function ShareSection({ event }) {
  const [shareMessage, setShareMessage] = useState(null);

  const handleCopy = () => {
    const url = window.location.href;
    navigator.clipboard.writeText(url);
    setShareMessage("🔗 Link copied");
    setTimeout(() => setShareMessage(null), 3000);
  };

  const text = encodeURIComponent(`Check out this event: ${event?.title}\n${window.location.href}`);

  return (
    <div className="bg-white rounded-xl p-4 border shadow-sm">
      <h4 className="text-lg font-semibold text-gray-900 mb-3">Share This Event</h4>
      {shareMessage && <div className="mb-2 text-sm text-blue-700">{shareMessage}</div>}
      <div className="flex gap-3">
        <button onClick={handleCopy} className="px-3 py-2 bg-blue-100 text-blue-700 rounded-lg">🔗 Copy</button>
        <button onClick={() => window.open(`https://wa.me/?text=${text}`, "_blank")} className="px-3 py-2 bg-green-100 text-green-700 rounded-lg">💬 WhatsApp</button>
        <button onClick={() => window.open(`https://t.me/share/url?url=${text}`, "_blank")} className="px-3 py-2 bg-blue-50 text-blue-700 rounded-lg">📣 Telegram</button>
        <button onClick={() => window.open(`https://twitter.com/intent/tweet?text=${encodeURIComponent(event?.title || "Event")}&url=${encodeURIComponent(window.location.href)}`, "_blank")} className="px-3 py-2 bg-black text-white rounded-lg">🐦</button>
      </div>
    </div>
  );
}
