package io.student.rangiffler.api.core;

import io.student.rangiffler.exceptions.BrokenTestException;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

public enum ThreadSafeCookieStore implements CookieStore {
    INSTANCE;

    private final ThreadLocal<CookieStore> threadLocalCookieStore = ThreadLocal.withInitial(
        this::inMemoryCookieStore
    );

    private CookieStore inMemoryCookieStore() {
        return new CookieManager().getCookieStore();
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        getStore().add(uri, cookie);
    }

    @Override
    public List<HttpCookie> get(URI uri) {
       return getStore().get(uri);
    }

    @Override
    public List<HttpCookie> getCookies() {
      return getStore().getCookies();
    }

    @Override
    public List<URI> getURIs() {
        return getStore().getURIs();
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        return getStore().remove(uri, cookie);
    }

    @Override
    public boolean removeAll() {
       return getStore().removeAll();
    }

    public String cookieValue(String cookieName) {
        return getCookies().stream()
            .filter(cookie -> cookie.getName().equals(cookieName))
            .findFirst()
            .map(HttpCookie::getValue)
            .orElseThrow(() -> new BrokenTestException("Can`t find cookie with name %s".formatted(cookieName)));
    }

    private CookieStore getStore() {
        return threadLocalCookieStore.get();
    }
}
