(function(){
    const btn = document.querySelector('.mobile-menu-toggle');
    const nav = document.querySelector('.navbar');
    if(btn && nav){ btn.addEventListener('click', ()=> nav.classList.toggle('active')); }
})();

// Search expandable on mobile
(function(){
    const wrap = document.querySelector('.search-wrap');
    const btn = document.querySelector('.search-btn');
    const input = document.getElementById('searchBox');
    if(!wrap || !btn) return;
    function ensureCompact(){ wrap.classList.add('compact'); }
    ensureCompact();
    btn.addEventListener('click', (e)=>{
        if(window.innerWidth <= 768){
            e.preventDefault();
            wrap.classList.toggle('open');
            if(wrap.classList.contains('open')) input && input.focus();
        }
    });
    document.addEventListener('click', (e)=>{
        if(window.innerWidth > 768) return;
        if(!wrap.contains(e.target)) wrap.classList.remove('open');
    });
})();

// Load more products
document.getElementById("loadMore").addEventListener("click", function () {
    const hidden = document.querySelectorAll(".product-card.hidden");
    for (let i = 0; i < 2 && i < hidden.length; i++) { hidden[i].classList.remove("hidden"); }
    if (document.querySelectorAll(".product-card.hidden").length === 0) this.style.display = "none";
});

// CSKH panel
(function () {
    const fab = document.getElementById('cskhFab');
    const panel = document.getElementById('cskhPanel');
    const closeBtn = document.getElementById('cskhClose');
    const overlay = document.getElementById('cskhOverlay');
    function openPanel() { panel.classList.add('open'); overlay.hidden = false; fab.setAttribute('aria-expanded', 'true'); }
    function closePanel() { panel.classList.remove('open'); overlay.hidden = true; fab.setAttribute('aria-expanded', 'false'); }
    fab.addEventListener('click', () => panel.classList.contains('open') ? closePanel() : openPanel());
    closeBtn.addEventListener('click', closePanel);
    overlay.addEventListener('click', closePanel);
    document.addEventListener('keydown', e => { if (e.key === 'Escape') closePanel(); });
})();

// Banner autoplay 2.5s
(function () {
    const track = document.getElementById('bannerTrack');
    const dots = Array.from(document.querySelectorAll('.banner .dot'));
    if (!track || dots.length === 0) return;
    const AUTO_DELAY = 2500; let index = 0, timer = null, total = dots.length; track.style.transition = 'transform .6s ease';
    function go(i) { index = (i + total) % total; track.style.transform = `translateX(-${index * 100}%)`; dots.forEach((d, k) => d.setAttribute('aria-selected', k === index ? 'true' : 'false')); }
    function start() { stop(); if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) return; timer = setInterval(() => go(index + 1), AUTO_DELAY); }
    function stop() { if (timer) { clearInterval(timer); timer = null; } }
    dots.forEach(d => d.addEventListener('click', e => { go(+e.currentTarget.dataset.i); start(); }));
    const banner = document.querySelector('.banner');
    banner.addEventListener('mouseenter', stop); banner.addEventListener('mouseleave', start);
    document.addEventListener('visibilitychange', () => document.hidden ? stop() : start());
    go(0); start();
})();

// === CART (localStorage) ===
(function () {
    const KEY = 'meta_cart_v1';
    const $ = (s, r = document) => r.querySelector(s);
    const $$ = (s, r = document) => Array.from(r.querySelectorAll(s));
    const cartBtn = $('#cartBtn'), cartBadge = $('#cartBadge');
    const drawer = $('#cartDrawer'), backdrop = $('#cartBackdrop');
    const cartList = $('#cartList'), cartTotal = $('#cartTotal');
    const cartClose = $('#cartClose'), cartClear = $('#cartClear'), cartCheckout = $('#cartCheckout');
    function load() { try { return JSON.parse(localStorage.getItem(KEY)) || [] } catch (e) { return [] } }
    function save(items) { localStorage.setItem(KEY, JSON.stringify(items)); }
    const toVND = n => (n || 0).toLocaleString('vi-VN') + 'đ';
    const parsePrice = t => parseInt((t || '').toString().replace(/[^\d]/g, '') || '0', 10);
    const slug = s => (s || '').toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '').replace(/[^a-z0-9]+/g, '-').replace(/(^-|-$)/g, '') || Math.random().toString(36).slice(2);
    function summary(items) { return { qty: items.reduce((a, b) => a + (b.qty || 0), 0), total: items.reduce((a, b) => a + (b.price || 0) * (b.qty || 0), 0) }; }
    function updateBadge() { cartBadge.textContent = summary(load()).qty; }
    function render() {
        const items = load(); cartList.innerHTML = items.length ? '' : '<p>Giỏ hàng trống.</p>';
        items.forEach(it => {
            const row = document.createElement('div'); row.className = 'cart-item'; row.dataset.id = it.id;
            row.innerHTML = `
          <img src="${it.image || 'https://via.placeholder.com/64'}" alt="${it.name}">
          <div>
            <div class="name">${it.name}</div>
            <div class="price">${toVND(it.price)}</div>
            <div class="qty" style="margin-top:6px;">
              <button class="dec" aria-label="Giảm">-</button>
              <span class="q">${it.qty}</span>
              <button class="inc" aria-label="Tăng">+</button>
            </div>
          </div>
          <div><button class="remove-btn" title="Xóa">🗑️</button></div>`; cartList.appendChild(row);
        }); cartTotal.textContent = toVND(summary(items).total); updateBadge();
    }
    function addItem(item) { const items = load(); const i = items.findIndex(x => x.id === item.id); if (i >= 0) items[i].qty += item.qty || 1; else items.push({ id: item.id, name: item.name, price: item.price, image: item.image, qty: item.qty || 1 }); save(items); render(); }
    function setQty(id, qty) { const items = load(); const it = items.find(x => x.id === id); if (!it) return; it.qty = Math.max(1, qty); save(items); render(); }
    function removeItem(id) { save(load().filter(x => x.id !== id)); render(); }
    function clearCart() { save([]); render(); }
    function openDrawer() { drawer.classList.add('open'); backdrop.hidden = false; }
    function closeDrawer() { drawer.classList.remove('open'); backdrop.hidden = true; }
    cartBtn?.addEventListener('click', e => { e.preventDefault(); openDrawer(); });
    cartClose?.addEventListener('click', closeDrawer);
    backdrop?.addEventListener('click', closeDrawer);
    $$('.add-to-cart').forEach(btn => {
        btn.addEventListener('click', e => { const card = e.currentTarget.closest('.product-card'); if (!card) return; const name = $('h3', card)?.textContent?.trim() || 'Sản phẩm'; const price = parsePrice($('.price', card)?.textContent); const image = $('img', card)?.src; const id = btn.dataset.id || slug(name); addItem({id, name, price, image, qty: 1}); e.currentTarget.textContent = 'Đã thêm ✓'; setTimeout(() => e.currentTarget.textContent = 'Thêm Giỏ hàng', 1200); });
    });
    $$('.buy-now').forEach(btn => {
        btn.addEventListener('click', e => { const card = e.currentTarget.closest('.product-card'); if (!card) return; const name = $('h3', card)?.textContent?.trim() || 'Sản phẩm'; const price = parsePrice($('.price', card)?.textContent); const image = $('img', card)?.src; const id = btn.dataset.id || slug(name); addItem({id, name, price, image, qty: 1}); openDrawer(); });
    });
    cartList.addEventListener('click', e => {
        const row = e.target.closest('.cart-item'); if (!row) return; const id = row.dataset.id;
        if (e.target.classList.contains('inc')) { const c = parseInt($('.q', row).textContent, 10) || 1; setQty(id, c + 1); }
        if (e.target.classList.contains('dec')) { const c = parseInt($('.q', row).textContent, 10) || 1; setQty(id, Math.max(1, c - 1)); }
        if (e.target.classList.contains('remove-btn')) { removeItem(id); }
    });
    cartClear?.addEventListener('click', clearCart);
    cartCheckout?.addEventListener('click', () => { alert('Demo checkout: chuyển tới trang /checkout tại đây.'); });
    window.addToCart = (item) => addItem(item);
    window.openCart = () => openDrawer();
    render();
})();

/* ===== WISHLIST (localStorage) ===== */
(function () {
    const FKEY = 'meta_fav_v1';
    const $ = (s, r = document) => r.querySelector(s);
    const $$ = (s, r = document) => Array.from(r.querySelectorAll(s));
    const favBtn = $('#favBtn'); const favBadge = $('#favBadge');
    const drawer = $('#favDrawer'); const backdrop = $('#favBackdrop');
    const favList = $('#favList'); const favClose = $('#favClose'); const favClear = $('#favClear');
    function loadFav() { try { return JSON.parse(localStorage.getItem(FKEY)) || [] } catch (e) { return [] } }
    function saveFav(items) { localStorage.setItem(FKEY, JSON.stringify(items)); }
    function inFav(id) { return loadFav().some(x => x.id === id); }
    const slug = s => (s || '').toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '').replace(/[^a-z0-9]+/g, '-').replace(/(^-|-$)/g, '') || Math.random().toString(36).slice(2);
    const parsePrice = t => parseInt((t || '').toString().replace(/[^\d]/g, '') || '0', 10);
    const toVND = n => (n || 0).toLocaleString('vi-VN') + 'đ';
    function updateFavBadge() { favBadge.textContent = loadFav().length; }
    function renderFav() {
        const items = loadFav(); favList.innerHTML = items.length ? '' : '<p>Chưa có sản phẩm yêu thích.</p>';
        items.forEach(it => {
            const row = document.createElement('div'); row.className = 'cart-item'; row.dataset.id = it.id;
            row.innerHTML = `
          <img src="${it.image || 'https://via.placeholder.com/64'}" alt="${it.name}">
          <div>
            <div class="name">${it.name}</div>
            <div class="price">${toVND(it.price || 0)}</div>
            <div style="margin-top:6px; display:flex; gap:8px;">
              <button class="btn btn-outline add-from-fav">Thêm vào giỏ</button>
            </div>
          </div>
          <div><button class="remove-btn" title="Bỏ yêu thích">🗑️</button></div>`; favList.appendChild(row);
        }); updateFavBadge();
    }
    function toggleFav(item) { const items = loadFav(); const i = items.findIndex(x => x.id === item.id); if (i >= 0) { items.splice(i, 1); } else { items.push(item); } saveFav(items); updateFavBadge(); }
    function openDrawer() { drawer.classList.add('open'); backdrop.hidden = false; }
    function closeDrawer() { drawer.classList.remove('open'); backdrop.hidden = true; }
    favBtn?.addEventListener('click', e => { e.preventDefault(); renderFav(); openDrawer(); });
    favClose?.addEventListener('click', closeDrawer);
    backdrop?.addEventListener('click', closeDrawer);
    favClear?.addEventListener('click', () => { saveFav([]); renderFav(); });
    favList.addEventListener('click', e => {
        const row = e.target.closest('.cart-item'); if (!row) return; const id = row.dataset.id;
        const items = loadFav(); const it = items.find(x => x.id === id); if (!it) return;
        if (e.target.classList.contains('add-from-fav')) { if (typeof window.addToCart === 'function') { window.addToCart({...it, qty: 1}); } if (typeof window.openCart === 'function') { window.openCart(); } }
        if (e.target.classList.contains('remove-btn')) { saveFav(items.filter(x => x.id !== id)); renderFav(); const btn = document.querySelector(`.wish-toggle[data-id="${CSS.escape(id)}"]`); if (btn) { btn.classList.remove('active'); btn.innerHTML = '♡'; } }
    });
    function ensureHeartButtons() {
        $$('.product-card').forEach(card => {
            if (card.querySelector('.wish-toggle')) return;
            const name = card.querySelector('h3')?.textContent?.trim() || 'Sản phẩm';
            const price = parsePrice(card.querySelector('.price')?.textContent);
            const image = card.querySelector('img')?.src;
            const idBtn = card.querySelector('.add-to-cart')?.dataset?.id || slug(name);
            const btn = document.createElement('button'); btn.className = 'wish-toggle'; btn.dataset.id = idBtn; btn.title = 'Thêm vào yêu thích';
            if (inFav(idBtn)) { btn.classList.add('active'); btn.innerHTML = '❤'; } else { btn.innerHTML = '♡'; }
            btn.addEventListener('click', () => { const item = {id: idBtn, name, price, image}; const willActive = !inFav(idBtn); toggleFav(item); btn.classList.toggle('active', willActive); btn.innerHTML = willActive ? '❤' : '♡'; });
            card.appendChild(btn);
        });
    }
    ensureHeartButtons(); updateFavBadge();
    const grid = $('#productGrid'); if (grid) { const obs = new MutationObserver(() => ensureHeartButtons()); obs.observe(grid, {childList: true, subtree: false}); }
})();