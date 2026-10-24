import { defineConfig } from 'vite';
import { resolve } from 'path';
import { fileURLToPath } from 'url';

const __dirname = fileURLToPath(new URL('.', import.meta.url));

export default defineConfig({
  root: __dirname,
  publicDir: 'public',
  build: {
    outDir: 'dist',
    emptyOutDir: true,
    rollupOptions: {
      input: {
        main: resolve(__dirname, 'index.html'),
        orders: resolve(__dirname, 'orders.html'),
        dashboardPage: resolve(__dirname, 'dashboard-page.html'),
        login: resolve(__dirname, 'login.html'),
      },
    },
  },
});
