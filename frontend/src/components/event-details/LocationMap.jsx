export default function LocationMap({ mapUrl }) {
  return (
    <div className="w-full rounded-xl overflow-hidden shadow">
      <iframe
        src={mapUrl}
        className="w-full h-64 md:h-80 border-none"
        loading="lazy"
      ></iframe>
    </div>
  );
}
