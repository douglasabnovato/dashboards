// Falha se o portal apontar para um arquivo local que não existe.
import { existsSync, readFileSync } from 'node:fs';

const html = readFileSync('index.html', 'utf8');
const refs = [...html.matchAll(/(?:href|src)=["']([^"'#?]+)/gi), ...html.matchAll(/loadInViewer\('([^']+)'/g)].map((m) => m[1]);
const locais = refs.filter((r) => !/^([a-z]+:|\/\/)/i.test(r));
const faltando = locais.filter((r) => !existsSync(decodeURIComponent(r).replace(/^\.?\//, '')));
if (faltando.length) {
  console.error('Referências quebradas:', faltando);
  process.exit(1);
}
console.log(`${locais.length} referências locais conferidas`);
