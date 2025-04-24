import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";
import path from "node:path";

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
  define: {
    // 브라우저에서 process.env 참조 에러 방지
    "process.env": {},
  },
  build: {
    lib: {
      entry: path.resolve(__dirname, "src/embed-chat.tsx"),
      name: "FineappleChat",
      fileName: () => `fineapple-chat.js`,
      formats: ["iife"],
    },
  },
});
