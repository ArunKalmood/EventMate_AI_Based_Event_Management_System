export default function ChatBubble({ message }) {
  const isUser = message.sender === "user";

  return (
    <div className={`flex ${isUser ? "justify-end" : "justify-start"}`}>
      <div
        className={`max-w-[75%] px-3 py-2 rounded-lg text-sm ${
          isUser
            ? "bg-black text-white"
            : "bg-gray-100 text-gray-800"
        }`}
      >
        {message.text}
      </div>
    </div>
  );
}
