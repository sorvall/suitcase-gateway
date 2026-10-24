// Service worker админки BikeBoxGo: показывает push-уведомления о новых заказах.
// Лежит в корне сайта, чтобы его область (scope) могла включать /admin.

self.addEventListener('install', function () {
    self.skipWaiting();
});

self.addEventListener('activate', function (event) {
    event.waitUntil(self.clients.claim());
});

self.addEventListener('push', function (event) {
    var data = {};
    try {
        data = event.data ? event.data.json() : {};
    } catch (e) {
        data = { body: event.data ? event.data.text() : '' };
    }
    // iOS требует показывать уведомление на каждый push, иначе отзовёт подписку
    event.waitUntil(self.registration.showNotification(data.title || 'BikeBoxGo', {
        body: data.body || '',
        icon: '/pwa/icon-192.png',
        badge: '/pwa/icon-192.png',
        data: { url: data.url || '/admin' }
    }));
});

self.addEventListener('notificationclick', function (event) {
    event.notification.close();
    var url = (event.notification.data && event.notification.data.url) || '/admin';
    event.waitUntil(self.clients.matchAll({ type: 'window', includeUncontrolled: true }).then(function (clients) {
        for (var i = 0; i < clients.length; i++) {
            if (new URL(clients[i].url).pathname === url && 'focus' in clients[i]) {
                // Открытая админка: перезагружаем, чтобы сразу увидеть новый заказ
                clients[i].navigate(url);
                return clients[i].focus();
            }
        }
        return self.clients.openWindow(url);
    }));
});
