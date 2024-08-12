/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors:{
        put:{
          light: 'rgba(44,103,141,1)',
          dark: 'rgba(35,85,114,1)'
        }
      }
    },
  },
  plugins: [],
}