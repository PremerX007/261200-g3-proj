/** @type {import('tailwindcss').Config} */
const withMT = require("@material-tailwind/react/utils/withMT");

export default withMT({
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
});
