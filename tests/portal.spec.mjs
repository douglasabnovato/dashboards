import { expect, test } from '@playwright/test';

test('o portal não depende de CDN para pintar', async ({ page }) => {
  const externos = [];
  page.on('request', (q) => {
    if (!q.url().startsWith('http://127.0.0.1')) externos.push(q.url());
  });
  await page.goto('/');
  await expect(page.locator('#view-home')).toBeVisible();
  expect(externos).toEqual([]);
});

test('abrir um dashboard mostra o visualizador e voltar retorna ao início', async ({ page }) => {
  await page.goto('/');
  await page.locator('button[onclick^="loadInViewer"]').first().click();
  await expect(page.locator('#view-viewer')).toBeVisible();
  await expect(page.locator('#hub-iframe')).toHaveAttribute('src', /dashboard\//);
  await page.locator('#nav-home').click();
  await expect(page.locator('#view-home')).toBeVisible();
});

test('em 375 px não há rolagem horizontal', async ({ page }) => {
  await page.setViewportSize({ width: 375, height: 800 });
  await page.goto('/');
  expect(await page.evaluate(() => document.documentElement.scrollWidth)).toBeLessThanOrEqual(375);
});
