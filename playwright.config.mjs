import { defineConfig } from '@playwright/test';

/* Porta própria por variável: vários agentes na mesma máquina colidem na 4173 */
const PORTA = process.env.PORTA_PORTAL || '4173';

export default defineConfig({
  testDir: 'tests',
  use: { baseURL: `http://127.0.0.1:${PORTA}` },
  webServer: {
    command: `npx http-server . -p ${PORTA} -a 127.0.0.1 -s`,
    url: `http://127.0.0.1:${PORTA}`,
    reuseExistingServer: false,
  },
});
