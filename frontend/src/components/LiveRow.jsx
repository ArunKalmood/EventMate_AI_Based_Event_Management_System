import React, { useState } from "react";

/* ============================
   EVENT IMAGES
   ============================ */
const images = [
  "https://images.pexels.com/photos/1190299/pexels-photo-1190299.jpeg",
  "https://images.pexels.com/photos/1190298/pexels-photo-1190298.jpeg",
  "https://images.pexels.com/photos/167636/pexels-photo-167636.jpeg",
  "https://images.pexels.com/photos/1712879/pexels-photo-1712879.jpeg",
  "https://images.pexels.com/photos/1543795/pexels-photo-1543795.jpeg",
  "./public/1692721146-20230822-fan-expo-meetup.jpg",
  "./public/istockphoto-2177112136-612x612.jpg",
  "./public/istockphoto-469569148-612x612.jpg",
  "./public/people-crowd-rock-live-night-600nw-2500937899.webp",
  "https://images.pexels.com/photos/210501/pexels-photo-210501.jpeg",
  "./public/1618-augustus-2019-lowlands-festival-600nw-1488792806.webp",
  "./public/360_F_741276556_eOnAhHdOzxMtqtlV3ckEU3qFZe6rI9ii.jpg",
  "https://images.pexels.com/photos/1190300/pexels-photo-1190300.jpeg",
  "./public/speaker-giving-talk-conference-hall-600nw-2622968815.webp",
  "https://images.pexels.com/photos/1190297/pexels-photo-1190297.jpeg",
  "https://images.pexels.com/photos/154147/pexels-photo-154147.jpeg",
  "https://images.pexels.com/photos/2608517/pexels-photo-2608517.jpeg",
  "https://images.pexels.com/photos/2747449/pexels-photo-2747449.jpeg",
  "https://images.pexels.com/photos/976866/pexels-photo-976866.jpeg",
  "https://images.pexels.com/photos/274192/pexels-photo-274192.jpeg",
  "./public/istockphoto-1474904802-612x612.jpg",
  "https://images.pexels.com/photos/1154189/pexels-photo-1154189.jpeg",
  "https://images.pexels.com/photos/1784186/pexels-photo-1784186.jpeg",
  "https://images.pexels.com/photos/1684187/pexels-photo-1684187.jpeg",
  "https://images.pexels.com/photos/1738641/pexels-photo-1738641.jpeg",
  "https://images.pexels.com/photos/248797/pexels-photo-248797.jpeg",
  "https://images.pexels.com/photos/1677710/pexels-photo-1677710.jpeg",
  "https://images.pexels.com/photos/1676367/pexels-photo-1676367.jpeg",
  "https://images.pexels.com/photos/2240766/pexels-photo-2240766.jpeg",
];

/* ============================
   IMAGE CARD
   ============================ */
function ImageBox({ src, size }) {
  const [failed, setFailed] = useState(false);
  if (!src || failed) return null;

  const sizeMap = {
    small: "w-44 h-32",
    medium: "w-48 h-44",
    big: "w-56 h-60",
  };

  return (
    <div className="flex-shrink-0">
      <div
        className={`${sizeMap[size]} rounded-2xl overflow-hidden bg-slate-200 shadow-md transition-transform duration-300 hover:scale-[1.03]`}
      >
        <img
          src={src}
          alt=""
          className="w-full h-full object-cover"
          loading="lazy"
          draggable={false}
          onError={() => setFailed(true)}
        />
      </div>
    </div>
  );
}

/* ============================
   COLUMN
   ============================ */
function Column({ pattern, imgs }) {
  return (
    <div className="flex flex-col gap-3">
      {pattern === "big" && <ImageBox src={imgs[0]} size="big" />}
      {pattern === "small-small" && (
        <>
          <ImageBox src={imgs[0]} size="small" />
          <ImageBox src={imgs[1]} size="small" />
        </>
      )}
      {pattern === "medium-small" && (
        <>
          <ImageBox src={imgs[0]} size="medium" />
          <ImageBox src={imgs[1]} size="small" />
        </>
      )}
      {pattern === "small-medium" && (
        <>
          <ImageBox src={imgs[0]} size="small" />
          <ImageBox src={imgs[1]} size="medium" />
        </>
      )}
    </div>
  );
}

/* ============================
   ROW
   ============================ */
function LiveRow({ reverse }) {
  const patterns = ["big", "small-small", "medium-small", "small-medium"];
  const pool = [...images, ...images, ...images];

  return (
    <div className="overflow-hidden select-none">
      <div
        className={`flex gap-4 w-max will-change-transform pause-on-hover
          ${reverse ? "md:animate-marquee-reverse" : "md:animate-marquee"}
        `}
      >
        {[0, 1, 2].map((dup) =>
          patterns.map((pattern, i) => (
            <Column
              key={`${dup}-${i}`}
              pattern={pattern}
              imgs={[
                pool[(dup * 8 + i * 2) % pool.length],
                pool[(dup * 8 + i * 2 + 1) % pool.length],
              ]}
            />
          ))
        )}
      </div>
    </div>
  );
}

/* ============================
   EXPORT
   ============================ */
export default function LiveRows() {
  return (
    <div className="space-y-8">
      <LiveRow />
      <LiveRow reverse />
    </div>
  );
}
