import { useState } from "react";
import { Sparkles } from "lucide-react";
import ChatWindow from "./ChatWindow";

export default function ChatWidget() {
  const [open, setOpen] = useState(false);

  return (
    <>
      {/* Floating Button */}
      <button
        onClick={() => setOpen(!open)}
        className="fixed bottom-6 right-6 z-50 bg-black text-white w-14 h-14 rounded-full shadow-lg flex items-center justify-center hover:scale-105 transition"
        aria-label="Open chat"
      >
        <Sparkles size={22} />
      </button>

      {/* Chat Window */}
      {open && <ChatWindow onClose={() => setOpen(false)} />}
    </>
  );
}
