/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      fontFamily: {
        beyonders: ["beyonders", "san"],
        concert: ["Concert One", "sans-serif"],
      },
    },
  },
  plugins: [],
};
