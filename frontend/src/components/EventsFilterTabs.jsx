import { useState } from "react";

const FILTERS = [
  "All",
  "Today",
  "Tomorrow",
  "This Week",
  "This Weekend",
  "Next Week",
  "Next Weekend",
  "This Month"
];

function EventsFilterTabs({ selected, onChange }) {
  return (
    <div className="max-w-7xl mx-auto px-6 mt-4">
      <div className="flex gap-10 border-b border-gray-200 pb-2 overflow-x-auto">
        {FILTERS.map((filter) => (
          <button
            key={filter}
            onClick={() => onChange(filter)}
            className={`pb-2 text-base font-medium transition-colors
              ${
                selected === filter
                  ? "text-gray-900"
                  : "text-gray-500 hover:text-gray-700"
              }`}
          >
            {filter}

            {/* underline */}
            {selected === filter && (
              <div className="h-[2px] bg-gray-900 rounded-full mt-2"></div>
            )}
          </button>
        ))}
      </div>
    </div>
  );
}

export default EventsFilterTabs;
