import { defineConfig } from '@playwright/test';

export default defineConfig({
  testDir: 'tests',
  use: { baseURL: 'http://127.0.0.1:4173' },
  webServer: {
    command: 'npx http-server . -p 4173 -a 127.0.0.1 -s',
    url: 'http://127.0.0.1:4173',
    reuseExistingServer: false,
  },
});
