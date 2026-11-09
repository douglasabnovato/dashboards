/* Acessibilidade: axe no portal (claro e escuro) e nos 3 dashboards, e uso só com teclado (issues #12 e #13) */
import { expect, test } from '@playwright/test';
import AxeBuilder from '@axe-core/playwright';

const DASHBOARDS = [
  { url: 'dashboard/2-analytics_dashboard_v1-14-08-2026.html', controles: ['btn-mode-real', 'btn-mode-sim'] },
  { url: 'dashboard/3-analytics_dashboard_v2-14-08-2026.html', controles: ['tab-btn-guide', 'btn-mode-sim', 'sim-budget-daily', 'sim-ticket'] },
  { url: 'kanban/1-weekly_dashboard-31-07-2026.html', controles: ['Coluna Backlog Geral', 'Coluna Concluído'] },
];

/* Os dashboards usam o Tailwind e o Chart.js por CDN; sem rede, a página fica sem estilo e o teste perde sentido */
const semRedeExterna = (url) => !url.startsWith('http://127.0.0.1');

/* Percorre a página com Tab e devolve, para cada parada, id ou nome, se tem nome acessível e se o foco aparece */
async function percorrerComTab(page, limite = 80) {
  const paradas = [];
  for (let i = 0; i < limite; i++) {
    await page.keyboard.press('Tab');
    const info = await page.evaluate(() => {
      const el = document.activeElement;
      if (!el || el === document.body) return null;
      const cs = getComputedStyle(el);
      const contorno = cs.outlineStyle !== 'none' && parseFloat(cs.outlineWidth) > 0;
      const nome = (el.getAttribute('aria-label') || el.getAttribute('title') || el.innerText || '').trim()
        || (el.getAttribute('aria-labelledby') && document.getElementById(el.getAttribute('aria-labelledby'))?.innerText.trim()) || '';
      return { chave: el.id || el.getAttribute('aria-label') || el.outerHTML.slice(0, 80), nome, foco: contorno || cs.boxShadow !== 'none' };
    });
    if (!info || paradas.some((p) => p.chave === info.chave && p.nome === info.nome)) break;
    paradas.push(info);
  }
  return paradas;
}

for (const esquema of ['light', 'dark']) {
  test(`portal sem violações do axe no modo ${esquema}, no início, no visualizador e no erro`, async ({ page }) => {
    await page.emulateMedia({ colorScheme: esquema });
    await page.goto('/');
    expect((await new AxeBuilder({ page }).analyze()).violations).toEqual([]);

    await page.locator('button[onclick^="loadInViewer"]').first().click();
    await expect(page.locator('#view-viewer')).toBeVisible();
    expect((await new AxeBuilder({ page }).exclude('#hub-iframe').analyze()).violations).toEqual([]);

    await page.route('**/kanban/1-weekly_dashboard-31-07-2026.html', (rota) => rota.fulfill({ status: 404, body: 'não encontrado' }));
    await page.locator('#quick-selector').selectOption('kanban/1-weekly_dashboard-31-07-2026.html');
    await expect(page.getByRole('alert')).toBeVisible();
    expect((await new AxeBuilder({ page }).exclude('#hub-iframe').analyze()).violations).toEqual([]);
  });
}

for (const { url, controles } of DASHBOARDS) {
  test(`${url}: sem violação séria ou crítica do axe e controles alcançados por Tab com foco visível`, async ({ page }) => {
    await page.goto(url);
    await page.waitForLoadState('networkidle');
    const graves = (await new AxeBuilder({ page }).analyze()).violations.filter((v) => ['serious', 'critical'].includes(v.impact));
    expect(graves.map((v) => `${v.id}: ${v.nodes.map((n) => n.target.join(' ')).join(', ')}`)).toEqual([]);

    if (url.includes('3-analytics')) await page.locator('#btn-mode-sim').click();
    await page.locator('body').click({ position: { x: 1, y: 1 } });
    const paradas = await percorrerComTab(page);
    const chaves = paradas.map((p) => p.chave);
    for (const controle of controles) expect(chaves).toContain(controle);
    expect(paradas.filter((p) => !p.foco).map((p) => p.chave)).toEqual([]);
    expect(paradas.filter((p) => !p.nome).map((p) => p.chave)).toEqual([]);
  });
}

test('no visualizador, Tab entra no dashboard e Shift+Tab volta à barra do portal', async ({ page }) => {
  await page.goto('/');
  await page.locator('button[onclick*="2-analytics_dashboard_v1"]').click();
  await expect(page.locator('#view-viewer')).toBeVisible();
  await page.frameLocator('#hub-iframe').locator('#btn-mode-real').waitFor();

  await page.locator('button[onclick="reloadIframe()"]').first().focus();
  await page.keyboard.press('Tab');
  await expect(page.locator('#hub-iframe')).toBeFocused();
  const dentro = await page.frameLocator('#hub-iframe').locator(':focus').count();
  if (dentro === 0) await page.keyboard.press('Tab');
  await expect(page.frameLocator('#hub-iframe').locator(':focus')).toHaveCount(1);

  await page.keyboard.press('Shift+Tab');
  if (await page.frameLocator('#hub-iframe').locator(':focus').count()) await page.keyboard.press('Shift+Tab');
  await expect(page.locator('button[onclick="reloadIframe()"]').first()).toBeFocused();
});
/* fim de acessibilidade.spec.mjs */
