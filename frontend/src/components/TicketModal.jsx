import QRCode from "react-qr-code";

export default function TicketModal({
  title,
  date,
  time,
  location,
  ticketCode,
  qrPayload,
  onClose,
}) {
  return (
    <div className="fixed inset-0 z-50 bg-black/60 flex items-center justify-center">
      <div className="bg-white rounded-2xl p-6 w-full max-w-sm relative">
        <button
          onClick={onClose}
          className="absolute top-3 right-3 text-gray-500"
        >
          ✕
        </button>

        <h2 className="text-xl font-bold mb-2">{title}</h2>

        <div className="text-sm text-gray-600 mb-4">
          <p>{date} {time}</p>
          <p>{location}</p>
        </div>

        <div className="flex justify-center my-4">
          <QRCode value={qrPayload} size={180} />
        </div>

        <p className="text-xs text-center text-gray-500">
          Ticket Code
        </p>
        <p className="text-sm text-center font-mono">
          {ticketCode}
        </p>
      </div>
    </div>
  );
}
