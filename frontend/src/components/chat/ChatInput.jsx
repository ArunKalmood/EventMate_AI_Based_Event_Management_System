import { useState } from "react";

export default function ChatInput({ onSend }) {
  const [input, setInput] = useState("");

  const send = () => {
    if (!input.trim()) return;
    onSend(input);
    setInput("");
  };

  return (
    <div className="p-3 border-t flex gap-2">
      <input
        value={input}
        onChange={(e) => setInput(e.target.value)}
        onKeyDown={(e) => e.key === "Enter" && send()}
        placeholder="Ask about events..."
        className="flex-1 rounded border px-2 py-1 text-sm"
      />
      <button
        onClick={send}
        className="rounded bg-black px-3 py-1 text-white text-sm"
      >
        Send
      </button>
    </div>
  );
}
