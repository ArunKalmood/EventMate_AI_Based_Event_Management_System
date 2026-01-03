export default function ChatMessages({ messages, loading, onEventClick }) {
  return (
    <div className="flex-1 overflow-y-auto p-3 space-y-3">
      {messages.map((msg) => (
        <div
          key={msg.id}
          className={msg.sender === "user" ? "text-right" : "text-left"}
        >
          <div className="inline-block rounded-lg px-3 py-2 text-sm bg-gray-100">
            {msg.text}
          </div>

          {/* Event cards */}
          {msg.events && msg.events.length > 0 && (
            <div className="mt-2 space-y-2">
              {msg.events.map((event) => (
                <div
                  key={event.id}
                  className="cursor-pointer rounded-md border p-2 hover:bg-gray-50"
                  onClick={() => onEventClick(event.id)}
                >
                  <div className="font-medium text-sm">
                    {event.title}
                  </div>
                  {event.reason && (
                    <div className="text-xs text-gray-500">
                      {event.reason}
                    </div>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>
      ))}

      {loading && (
        <div className="text-left text-sm text-gray-400">
          EventMate Assistant is typing…
        </div>
      )}
    </div>
  );
}
