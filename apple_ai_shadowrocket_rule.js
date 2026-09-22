/*
 * Shadowrocket rule script: Apple Intelligence / Siri / PCC
 *
 * Register this file as a Script:
 *   Name: AppleAI
 *   Type: rule
 *   Engine: jsc
 *
 * Then add a routing Rule:
 *   Type: SCRIPT
 *   Value/Script: AppleAI
 *   Policy: PROXY
 *
 * The script only decides whether a connection matches.
 * The PROXY policy is selected in Shadowrocket's Rule entry, not here.
 */

const exact = new Set([
  "guzzoni.apple.com",
  "seed.siri.apple.com",
  "gspe1-ssl.ls.apple.com",

  "apple-relay.apple.com",
  "apple-relay.cloudflare.com",
  "apple-relay.fastly-edge.com",
  "apple-relay.akamaized.net",
  "apple-relay.mask.apple-dns.net",
  "apple-carry-relay.cloudflare.com",
  "cp4.cloudflare.com",

  "mask-api.icloud.com",
  "mask-api.fe.apple-dns.net",
  "mask-api.fe2.apple-dns.net",
  "mask-t.apple-dns.net",
  "mask.apple-dns.net",
  "mask-boot.icloud.com",
  "mask-canary.icloud.com",

  "probe.icloud.com",
  "metrics.icloud.com",
  "binary-blob-repository.apple.com",
  "p67-keyvalueservice.icloud.com",
  "p106-keyvalueservice.icloud.com",
  "aapps.mzstatic.com"
]);

const suffixes = [
  "smoot.apple.com",

  "apple-relay.apple.com",
  "apple-relay.cloudflare.com",
  "apple-relay.fastly-edge.com",
  "apple-relay.akamaized.net",
  "apple-relay.mask.apple-dns.net",
  "apple-carry-relay.cloudflare.com",
  "cp4.cloudflare.com",

  "mask.icloud.com",
  "mask-h2.icloud.com",

  "gateway.icloud.com",
  "apple-cloudkit.com",
  "icloud-content.com",
  "apps.mzstatic.com",
  "apple-livephotos.com",

  // Broad diagnostic coverage.
  // Keep these while locating the traffic that escaped the narrower ruleset.
  "apple.com",
  "icloud.com",
  "apple-dns.net",
  "mzstatic.com",
  "aaplimg.com",
  "cdn-apple.com"
];

function normalizeHost(value) {
  if (!value) return "";
  let h = String(value).trim().toLowerCase();

  // Strip [ ] from IPv6 literals and :port from simple host:port values.
  if (h.startsWith("[") && h.includes("]")) {
    h = h.slice(1, h.indexOf("]"));
  } else {
    const colonCount = (h.match(/:/g) || []).length;
    if (colonCount === 1) {
      const idx = h.lastIndexOf(":");
      const port = h.slice(idx + 1);
      if (/^\d+$/.test(port)) h = h.slice(0, idx);
    }
  }

  return h.replace(/\.$/, "");
}

function isIPv4(host) {
  const m = host.match(/^(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})$/);
  if (!m) return false;
  for (let i = 1; i <= 4; i++) {
    const n = Number(m[i]);
    if (n < 0 || n > 255) return false;
  }
  return true;
}

function matchesAppleAI(host) {
  if (!host) return false;

  // Apple-owned IPv4 space. Useful when Shadowrocket sees only a destination IP.
  if (isIPv4(host) && host.startsWith("17.")) return true;

  if (exact.has(host)) return true;

  for (const suffix of suffixes) {
    if (host === suffix || host.endsWith("." + suffix)) return true;
  }

  return false;
}

const host = normalizeHost(
  ($request && ($request.hostname || $request.host)) || ""
);

$done({ matched: matchesAppleAI(host) });
