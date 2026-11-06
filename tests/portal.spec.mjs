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

test('dashboard que não abre mostra o erro e deixa tentar de novo', async ({ page }) => {
  await page.route('**/dashboard/2-analytics_dashboard_v1-14-08-2026.html', (rota) =>
    rota.fulfill({ status: 404, body: 'não encontrado' }),
  );
  await page.goto('/');
  await page.locator('button[onclick*="2-analytics_dashboard_v1"]').click();
  const erro = page.getByRole('alert');
  await expect(erro).toContainText('Não foi possível abrir este dashboard');
  await expect(page.getByRole('button', { name: 'Tentar de novo' })).toBeFocused();
  await expect(page.locator('#hub-iframe')).not.toHaveAttribute('src', /dashboard\//);

  await page.unroute('**/dashboard/2-analytics_dashboard_v1-14-08-2026.html');
  await page.getByRole('button', { name: 'Tentar de novo' }).click();
  await expect(page.locator('#hub-iframe')).toHaveAttribute('src', /2-analytics_dashboard_v1/);
  await expect(page.locator('#viewer-erro')).toBeHidden();
  await expect(page.locator('#viewer-carregando')).toBeHidden();
});

test('o último dashboard escolhido vence, mesmo com a lista em outra ordem', async ({ page }) => {
  await page.goto('/');
  const botoes = page.locator('button[onclick^="loadInViewer"]');
  await botoes.last().click();
  await page.locator('#nav-home').click();
  await botoes.first().click();
  const esperado = (await botoes.first().getAttribute('onclick')).match(/'([^']+)'/)[1];
  await expect(page.locator('#hub-iframe')).toHaveAttribute('src', esperado);
});
