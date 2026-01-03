import { useState } from "react";
import { useNavigate } from "react-router-dom";
import ChatMessages from "./ChatMessages";
import ChatInput from "./ChatInput";
import { sendChatQuery } from "@/services/chatApi";

export default function ChatWindow({ onClose }) {
  const navigate = useNavigate();

  const [messages, setMessages] = useState([
    {
      id: 1,
      sender: "bot",
      text: "I’m getting ready to help you discover events."
    }
  ]);

  const [loading, setLoading] = useState(false);

  // 🔹 Unified add
  const addMessage = (message) => {
    setMessages(prev => [...prev, message]);
  };

  // 🔹 MAIN SEND HANDLER
  const handleSend = async (text) => {
    if (!text.trim()) return;

    // user message
    addMessage({
      id: Date.now(),
      sender: "user",
      text
    });

    setLoading(true);

    try {
      const data = await sendChatQuery(text);

      addMessage({
        id: Date.now() + 1,
        sender: "bot",
        text: data.reply,
        events: data.events || []
      });
    } catch (err) {
      addMessage({
        id: Date.now() + 1,
        sender: "bot",
        text: "Something went wrong. Please try again."
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed bottom-24 right-6 w-80 h-[420px] bg-white rounded-xl shadow-xl flex flex-col z-50">

      {/* Header */}
      <div className="p-3 border-b flex justify-between items-center">
        <span className="font-semibold">EventMate Assistant</span>
        <button onClick={onClose} className="text-gray-500 hover:text-black">
          ✕
        </button>
      </div>

      {/* Messages */}
      <ChatMessages
        messages={messages}
        loading={loading}
        onEventClick={(id) => navigate(`/events/${id}`)}
      />

      {/* Input */}
      <ChatInput onSend={handleSend} />
    </div>
  );
}
