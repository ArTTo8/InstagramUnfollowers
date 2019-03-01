package com.artto.unfollowers.data.remote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import dev.niekirk.com.instagram4android.Instagram4Android;
import dev.niekirk.com.instagram4android.requests.InstagramFbLoginRequest;
import dev.niekirk.com.instagram4android.requests.InstagramLoginRequest;
import dev.niekirk.com.instagram4android.requests.InstagramRequest;
import dev.niekirk.com.instagram4android.requests.internal.InstagramFetchHeadersRequest;
import dev.niekirk.com.instagram4android.requests.payload.InstagramFbLoginPayload;
import dev.niekirk.com.instagram4android.requests.payload.InstagramLoginPayload;
import dev.niekirk.com.instagram4android.requests.payload.InstagramLoginResult;
import dev.niekirk.com.instagram4android.util.InstagramGenericUtil;
import dev.niekirk.com.instagram4android.util.InstagramHashUtil;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Response;

public class Instagram extends Instagram4Android {
    private String deviceId;
    private String username;
    private String password;
    private String accessToken;
    private boolean isLoggedIn;
    private String uuid;
    private String rankToken;
    private long userId;
    private Response lastResponse;
    private OkHttpClient client;
    private final HashMap<String, Cookie> cookieStore = new HashMap<>();

    public Instagram(String username, String password) {
        super(username, password);
        this.username = username;
        this.password = password;
    }

    public void setup() {
        this.deviceId = InstagramHashUtil.generateDeviceId(this.username, this.password);
        this.uuid = InstagramGenericUtil.generateUuid(true);
        this.client = (new Builder()).cookieJar(new CookieJar() {

            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                if (cookies != null)
                    for (Cookie cookie : cookies)
                        Instagram.this.cookieStore.put(cookie.name(), cookie);

            }

            public List<Cookie> loadForRequest(HttpUrl url) {
                ArrayList<Cookie> validCookies = new ArrayList<>();

                for (Entry<String, Cookie> entry : Instagram.this.cookieStore.entrySet()) {
                    Cookie cookie = entry.getValue();
                    if (cookie.expiresAt() >= System.currentTimeMillis())
                        validCookies.add(cookie);
                }

                return validCookies;
            }
        }).build();
    }

    public InstagramLoginResult loginFb() throws IOException {
        InstagramFbLoginPayload loginRequest = InstagramFbLoginPayload.builder()
                .dryrun(true)
                .adid(InstagramGenericUtil.generateUuid(false))
                .device_id(this.deviceId)
                .fb_access_token(this.password)
                .phone_id(InstagramGenericUtil.generateUuid(false))
                .waterfall_id(InstagramGenericUtil.generateUuid(false))
                .build();

        InstagramLoginResult loginResult = this.sendRequest(new InstagramFbLoginRequest(loginRequest));
        if (loginResult.getStatus().equalsIgnoreCase("ok")) {
            System.out.println(this.cookieStore.toString());
            this.userId = loginResult.getLogged_in_user().getPk();
            this.rankToken = this.userId + "_" + this.uuid;
            this.isLoggedIn = true;
        }

        System.out.println("Hello! --> " + loginResult.toString());
        return loginResult;
    }

    public InstagramLoginResult login() throws IOException {
        InstagramLoginPayload loginRequest = InstagramLoginPayload.builder()
                .username(this.username)
                .password(this.password)
                .guid(this.uuid)
                .device_id(this.deviceId)
                .phone_id(InstagramGenericUtil.generateUuid(true))
                .login_attempt_account(0)
                ._csrftoken(this.getOrFetchCsrf(null))
                .build();

        InstagramLoginResult loginResult = this.sendRequest(new InstagramLoginRequest(loginRequest));
        if (loginResult.getStatus().equalsIgnoreCase("ok")) {
            this.userId = loginResult.getLogged_in_user().getPk();
            this.rankToken = this.userId + "_" + this.uuid;
            this.isLoggedIn = true;
        }

        return loginResult;
    }

    public String getOrFetchCsrf(HttpUrl url) throws IOException {
        Cookie cookie = this.getCsrfCookie(url);
        if (cookie == null) {
            this.sendRequest(new InstagramFetchHeadersRequest());
            cookie = this.getCsrfCookie(url);
        }

        return cookie.value();
    }

    public Cookie getCsrfCookie(HttpUrl url) {
        Iterator var2 = this.client.cookieJar().loadForRequest(url).iterator();

        Cookie cookie;
        do {
            if (!var2.hasNext()) return null;

            cookie = (Cookie) var2.next();
        } while (!cookie.name().equalsIgnoreCase("csrftoken"));

        return cookie;
    }

    public <T> T sendRequest(InstagramRequest<T> request) throws IOException {
        if (!this.isLoggedIn && request.requiresLogin()) {
            throw new IllegalStateException("Need to login first!");
        } else {
            request.setApi(this);
            return request.execute();
        }
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getRankToken() {
        return this.rankToken;
    }

    public void setRankToken(String rankToken) {
        this.rankToken = rankToken;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Response getLastResponse() {
        return this.lastResponse;
    }

    public void setLastResponse(Response lastResponse) {
        this.lastResponse = lastResponse;
    }

    public OkHttpClient getClient() {
        return this.client;
    }

}
