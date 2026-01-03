/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx,ts,tsx}"],

  theme: {
    extend: {
      /* =========================
         COLORS & TYPOGRAPHY
         ========================= */
      colors: {
        brand: {
          DEFAULT: "#0B132B",
          dark: "#050A18",
        },
      },

      fontFamily: {
        sans: ["Urbanist", "system-ui", "sans-serif"],
      },

      /* =========================
         MARQUEE KEYFRAMES (NO JUMP)
         ========================= */
      keyframes: {
        marquee: {
          "0%": { transform: "translateX(0)" },
          "100%": { transform: "translateX(-100%)" },
        },
        "marquee-reverse": {
          "0%": { transform: "translateX(-100%)" },
          "100%": { transform: "translateX(0)" },
        },
      },

      animation: {
        marquee: "marquee 60s linear infinite",
        "marquee-reverse": "marquee-reverse 60s linear infinite",
      },
    },
  },

  plugins: [
    /* =========================
       PAUSE ON HOVER (CLEAN)
       ========================= */
    function ({ addUtilities }) {
      addUtilities({
        ".pause": {
          animationPlayState: "paused",
        },
      });
    },
  ],
};
