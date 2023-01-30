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
    "[{\"domain\":\".livejournal.com\",\"expirationDate\":1677646590,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"sspjs_38.11.0_af_lpdid\",\"path\":\"/\",\"sameSite\":\"unspecified\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"%7B%22DATE%22%3A1674809602685%2C%22ID%22%3A%2211070%3A9319%22%7D\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1680002604.075069,\"hostOnly\":false,\"httpOnly\":true,\"name\":\"ljloggedin\",\"path\":\"/\",\"sameSite\":\"no_restriction\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"v2:u21031374:s886:t1674818604:g5b4d2159cfb96629366ca5824d4d83091a8aa785\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1680002604.075134,\"hostOnly\":false,\"httpOnly\":true,\"name\":\"BMLschemepref\",\"path\":\"/\",\"sameSite\":\"no_restriction\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"lanzelot\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1680002604.075192,\"hostOnly\":false,\"httpOnly\":true,\"name\":\"langpref\",\"path\":\"/\",\"sameSite\":\"no_restriction\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"ru/1674818604\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1680002604.075311,\"hostOnly\":false,\"httpOnly\":true,\"name\":\"ljsession\",\"path\":\"/\",\"sameSite\":\"no_restriction\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"v1:u21031374:s886:t1674817200:g0ff54b79b6c7d132fea7303dca0428b4f9453822//1\"},{\"domain\":\".fixin.livejournal.com\",\"hostOnly\":false,\"httpOnly\":true,\"name\":\"ljdomsess.fixin\",\"path\":\"/\",\"sameSite\":\"no_restriction\",\"secure\":true,\"session\":true,\"storeId\":\"0\",\"value\":\"v1:u21031374:s886:t1674817200:g580a582b8c70df16e179cdfe7685c5c981f48ff7//1\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1709614590.701407,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"ljprof\",\"path\":\"/\",\"sameSite\":\"no_restriction\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"f5ed98f6be5259a9063d3b424418f19b912ca3059ff2dfe2a\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1709378606.809507,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"ljuniq\",\"path\":\"/\",\"sameSite\":\"no_restriction\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"3iWjxPQXKSpMx4f:1674818606:pgstats0\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1709614590.847196,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"_ga\",\"path\":\"/\",\"sameSite\":\"unspecified\",\"secure\":false,\"session\":false,\"storeId\":\"0\",\"value\":\"GA1.2.1677963380.1674818607\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1706590592,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"adtech_uid\",\"path\":\"/\",\"sameSite\":\"unspecified\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"4504ba2f-d630-45aa-a9dd-42f5be0e2f50%3Alivejournal.com\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1706590590.737047,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"top100_id\",\"path\":\"/\",\"sameSite\":\"unspecified\",\"secure\":false,\"session\":false,\"storeId\":\"0\",\"value\":\"t1.1111412.1582980396.1674818607409\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1706354607,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"_ym_uid\",\"path\":\"/\",\"sameSite\":\"no_restriction\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"1671171689413088505\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1706354607,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"_ym_d\",\"path\":\"/\",\"sameSite\":\"no_restriction\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"1674818607\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1708946607,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"__gfp_64b\",\"path\":\"/\",\"sameSite\":\"unspecified\",\"secure\":false,\"session\":false,\"storeId\":\"0\",\"value\":\"3TXA4sWrFaoMCes3itayZ3YG9tINLOqLpigG2zPRXXH.l7|1674818607\"},{\"domain\":\".fixin.livejournal.com\",\"expirationDate\":1706354763.344355,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"t3_sid_4515972\",\"path\":\"/\",\"sameSite\":\"unspecified\",\"secure\":false,\"session\":false,\"storeId\":\"0\",\"value\":\"s1.2094450952.1674809603424.1674818763343.1.57\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1675140990,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"_gid\",\"path\":\"/\",\"sameSite\":\"unspecified\",\"secure\":false,\"session\":false,\"storeId\":\"0\",\"value\":\"GA1.2.391133870.1675053466\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1675125469,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"_ym_isad\",\"path\":\"/\",\"sameSite\":\"no_restriction\",\"secure\":true,\"session\":false,\"storeId\":\"0\",\"value\":\"2\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1675054650,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"_gat\",\"path\":\"/\",\"sameSite\":\"unspecified\",\"secure\":false,\"session\":false,\"storeId\":\"0\",\"value\":\"1\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1709614590.741629,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"last_visit\",\"path\":\"/\",\"sameSite\":\"unspecified\",\"secure\":false,\"session\":false,\"storeId\":\"0\",\"value\":\"1675036590741%3A%3A1675054590741\"},{\"domain\":\".livejournal.com\",\"expirationDate\":1675054650,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"_gat_gtag_UA_38559103_1\",\"path\":\"/\",\"sameSite\":\"unspecified\",\"secure\":false,\"session\":false,\"storeId\":\"0\",\"value\":\"1\"},{\"domain\":\".fixin.livejournal.com\",\"expirationDate\":1706590591.131521,\"hostOnly\":false,\"httpOnly\":false,\"name\":\"t3_sid_1111412\",\"path\":\"/\",\"sameSite\":\"unspecified\",\"secure\":false,\"session\":false,\"storeId\":\"0\",\"value\":\"s1.1559280364.1675053466311.1675054591131.2.44\"}]"

fun getCookie(gson: Gson) = gson.fromJson<List<CookieResponse>>(cookieString)