package com.isidroid.b21.data.source.remote

import android.webkit.CookieManager
import com.google.gson.Gson
import com.isidroid.core.ext.fromJson
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

class LocalCookieJar : CookieJar {
    private val cookieStore = HashMap<HttpUrl?, List<Cookie>>()
    private val cookieManager = CookieManager.getInstance()

//    "cookie_name=_cookie_value; path=path"

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val host = url.host

        cookieStore[host.toHttpUrl()] = cookies
        cookieManager.removeAllCookie()
        val cookies1 = cookieStore[host.toHttpUrl()]
        if (cookies1 != null) {
            for (cookie in cookies1) {
                val cookieString = cookie.name + "=" + cookie.value + "; path=" + cookie.path
                cookieManager.setCookie(cookie.domain, cookieString)
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val host = url.host
        val cookies = cookieStore[host.toHttpUrl()]
        return cookies ?: ArrayList()
    }
}

private val cookieString =
    "[{\"domain\":\".livejournal.com\",\"expirationDate\":1714469754.593633,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"luid\",\"path\":\"/\",\"sameSite\":\"no_restriction\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"URNKImQhY3q70wRaEcAxAgB=\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1714469755.423115,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"ljuniq\",\"path\":\"/\",\"sameSite\":\"no_restriction\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"8TtGRusCdJ64K6d:1679909755:pgstats0\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1714469916.684873,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"_ga\",\"path\":\"/\",\"sameSite\":\"unspecified\",\"secure\":false,\"session\":false,\"storeId\":\"0\",\"value\":\"GA1.2.1346284733.1679909756\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1679996316,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"_gid\",\"path\":\"/\",\"sameSite\":\"unspecified\",\"secure\":false,\"session\":false,\"storeId\":\"0\",\"value\":\"GA1.2.998228877.1679909756\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1714469916.782889,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"ljprof\",\"path\":\"/\",\"sameSite\":\"no_restriction\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"f0ed40b3eb802ab086421636a9aab4cc1b9df0782668bca4d\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1711445918,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"adtech_uid\",\"path\":\"/\",\"sameSite\":\"unspecified\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"d3bdb5f3-d312-4fb6-8391-2eb3a5a99448%3Alivejournal.com\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1711445916.937844,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"top100_id\",\"path\":\"/\",\"sameSite\":\"unspecified\",\"secure\":false,\"session\":false,\"storeId\":\"0\",\"value\":\"t1.4515972.97890738.1679909755992\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1711445756,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"_ym_uid\",\"path\":\"/\",\"sameSite\":\"no_restriction\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"1679909756580798865\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1711445756,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"_ym_d\",\"path\":\"/\",\"sameSite\":\"no_restriction\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"1679909756\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1714037756,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"__gfp_64b\",\"path\":\"/\",\"sameSite\":\"unspecified\",\"secure\":false,\"session\":false,\"storeId\":\"0\",\"value\":\"XMIQqwZ8nCy_PDC2ekxbn.8ZJ99_Nrr53Ah2n6BooQf.g7|1679909756\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1679981756,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"_ym_isad\",\"path\":\"/\",\"sameSite\":\"no_restriction\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"2\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1682501916,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"sspjs_38.20.0_af_lpdid\",\"path\":\"/\",\"sameSite\":\"unspecified\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"%7B%22DATE%22%3A1679909756314%2C%22ID%22%3A%228571%3A61043%22%7D\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1711445914,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"FCNEC\",\"path\":\"/\",\"sameSite\":\"unspecified\",\"secure\":false,\"session\":false,\"storeId\":\"0\",\"value\":\"%5B%5B%22AKsRol-Ot0kANozGJ5vcjY7nRrLjhEQcvncKk3jpksaCgo4XMwxs6TKJ7T-U5UkkQm8dAQAPcS0Gjf-3ZwcN8n3ALIL_zPLiL8OTsoXgh_F6uTY9MVzFK1CtbjmyqiA2wzh_iYTzjKRhNv-VH5EvJ_bKRkCPjFBdxQ%3D%3D%22%5D%2Cnull%2C%5B%5D%5D\"},{\"domain\":\".www.livejournal.com\",\"expirationDate\":1685093915.382062,\"hostOnly\":false,\"httpOnly\":true,\"name\":\"ljmastersession\",\"path\":\"/\",\"sameSite\":\"no_restriction\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"v2:u21031374:s889:aomZD1oi6Ze:g1a81e2e9140fedc484083c031a2881c64f818f63//1\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1685093915.382139,\"hostOnly\":false,\"httpOnly\":true,\"name\":\"ljloggedin\",\"path\":\"/\",\"sameSite\":\"no_restriction\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"v2:u21031374:s889:t1679909915:g2234538e955a180052d395d19663a259cdda66bd\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1685093915.382177,\"hostOnly\":false,\"httpOnly\":true,\"name\":\"BMLschemepref\",\"path\":\"/\",\"sameSite\":\"no_restriction\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"lanzelot\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1685093915.38221,\"hostOnly\":false,\"httpOnly\":true,\"name\":\"langpref\",\"path\":\"/\",\"sameSite\":\"no_restriction\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"ru/1679909915\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1685093915.382238,\"hostOnly\":false,\"httpOnly\":true,\"name\":\"ljsession\",\"path\":\"/\",\"sameSite\":\"no_restriction\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"v1:u21031374:s889:t1679907600:gd86cd069968bf9ec3b3206656b62966df32eff79//1\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1714469916.939757,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"last_visit\",\"path\":\"/\",\"sameSite\":\"unspecified\",\"secure\":false,\"session\":false,\"storeId\":\"0\",\"value\":\"1679891916939%3A%3A1679909916939\"},{\"domain\":\".www.livejournal.com\",\"expirationDate\":1711445970.520609,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"t3_sid_1111412\",\"path\":\"/\",\"sameSite\":\"unspecified\",\"secure\":false,\"session\":false,\"storeId\":\"0\",\"value\":\"s1.2004737124.1679909912175.1679909970520.1.10\"}]"

fun getCookie(gson: Gson) = gson.fromJson<List<CookieResponse>>(cookieString)