// components/CapacityBar.jsx

export default function CapacityBar({ booked, capacity }) {
  const percentage = Math.min(Math.round((booked / capacity) * 100), 100);

  const getColor = () => {
    if (percentage < 40) return "bg-green-500";
    if (percentage < 80) return "bg-orange-500";
    return "bg-red-600";
  };

  return (
    <div className="w-full">
      <div className="flex justify-between mb-1">
        <span className="text-sm font-medium text-gray-700">
          {percentage}% filled
        </span>
        <span className="text-sm text-gray-500">
          {booked} / {capacity} booked
        </span>
      </div>

      {/* Bar background */}
      <div className="w-full h-3 bg-gray-200 rounded-full overflow-hidden">
        <div
          className={`h-full ${getColor()} transition-all duration-500`}
          style={{ width: `${percentage}%` }}
        ></div>
      </div>
    </div>
  );
}
