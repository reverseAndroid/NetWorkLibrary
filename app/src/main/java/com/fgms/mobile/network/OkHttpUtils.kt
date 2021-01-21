package com.fgms.mobile.network

import android.content.Context
import android.net.Uri
import com.fgms.mobile.network.HttpsUtils.SSLParams
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.InputStream
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.HttpsURLConnection


/**
 * 2020/09/17 xujiaxuan
 *
 * 封装OKHTTP，自定义log拦截器和token拦截器,cache拦截器
 *
 * */
open class OkHttpUtils {

    lateinit var mContext: Context

    private lateinit var mClient: OkHttpClient

    internal fun getClient(): OkHttpClient {
        return mClient
    }

    //token拦截器
    private var tokenHeaderInterceptor = Interceptor { chain: Interceptor.Chain ->
        val request: Request = chain.request()
            .newBuilder()
            .header(
                NetWorkConstant.REQUEST_HEADER_CONTENT_TYPE_KEY,
                NetWorkConstant.REQUEST_HEADER_CONTENT_TYPE_VALUE
            )
            .addHeader(
                NetWorkConstant.REQUEST_HEADER_ACCEPT_ENCODING_KEY,
                NetWorkConstant.REQUEST_HEADER_ACCEPT_ENCODING_VALUE
            )
            .addHeader(
                NetWorkConstant.REQUEST_HEADER_CONNECTION_KEY,
                NetWorkConstant.REQUEST_HEADER_CONNECTION_VALUE
            )
            .addHeader(
                NetWorkConstant.REQUEST_HEADER_ACCEPT_KEY,
                NetWorkConstant.REQUEST_HEADER_ACCEPT_VALUE
            )
            .addHeader(
                NetWorkConstant.REQUEST_HEADER_COOKIE_KEY,
                NetWorkConstant.REQUEST_HEADER_COOKIE_VALUE
            )
            .addHeader(NetWorkConstant.REQUEST_HEADER_TOKEN_KEY, setTokenHeaderValue())
            .addHeader(NetWorkConstant.REQUEST_HEADER_X_REQUESTID_KEY, setXRequestidValue())
            .build()

        return@Interceptor chain.proceed(request)
    }

    //cache配置拦截器
    private var cacheInterceptor = Interceptor { chain: Interceptor.Chain ->
        //通过 CacheControl 控制缓存数据
        val cacheBuilder = CacheControl.Builder()
        //这个是控制缓存的最大生命时间
        cacheBuilder.maxAge(0, TimeUnit.SECONDS)
        //这个是控制缓存的过时时间
        cacheBuilder.maxStale(365, TimeUnit.DAYS)
        val cacheControl = cacheBuilder.build()

        //设置拦截器
        var request = chain.request()
        if (!NetUtils.isNetworkAvailable(mContext)) {
            request = request.newBuilder().cacheControl(cacheControl).build()
        }
        val originalResponse = chain.proceed(request)
        if (NetUtils.isNetworkAvailable(mContext)) {
            val maxAge = 0 //read from cache
            return@Interceptor originalResponse.newBuilder()
                .removeHeader(NetWorkConstant.PRAGMA)
                .header(NetWorkConstant.Cache_Control, "public ,max-age=$maxAge")
                .build()
        } else {
            val maxStale = 60 * 60 * 24 * 28 //tolerate 4-weeks stale
            return@Interceptor originalResponse.newBuilder()
                .removeHeader(NetWorkConstant.PRAGMA)
                .header(
                    NetWorkConstant.Cache_Control,
                    "poublic, only-if-cached, max-stale=$maxStale"
                )
                .build()
        }
    }

    // Log信息拦截器
    private var loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            LogUtils.sf(message)
        }
    })

    //初始化OkHttpUtils
    internal fun initOkHttpUtils() {
        /*================== common ==================*/

        //这里可以选择拦截级别  BODY = 请求方法 + 响应行 + 请求头 + 请求体
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        //cache
        val httpCacheDir = File(mContext.cacheDir, NetWorkConstant.RESPONSE)
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        val cache = Cache(httpCacheDir, cacheSize.toLong())

        //cookie
        val cookieJar: ClearableCookieJar =
            PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(mContext))

        //读取证书，信任此证书
        val sslParams: SSLParams = HttpsUtils.getSslSocketFactory(
            arrayOf(mContext.getAssets().open(NetWorkConstant.CA_NAME)),
            null,
            null)

        //OkHttpClient
        mClient = OkHttpClient.Builder()
            .connectTimeout(NetWorkConstant.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NetWorkConstant.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NetWorkConstant.WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(tokenHeaderInterceptor)
            .addInterceptor(cacheInterceptor)
            .addInterceptor(loggingInterceptor)
            .cache(cache)
//            .hostnameVerifier { hostname, session ->
//                return@hostnameVerifier HttpsURLConnection.getDefaultHostnameVerifier().verify(NetWorkConstant.HOSTNAME, session)
//            }
            .sslSocketFactory(sslParams.sSLSocketFactory!!, sslParams.trustManager!!)
            .cookieJar(cookieJar)
            .build()
    }

    //通过ContentProvider将SQLite中的token取出
    private fun setTokenHeaderValue(): String {
        val uri = Uri.parse(NetWorkConstant.ACCESS_CONTENT_PROVIDER_URI)
        val cursor = mContext.contentResolver?.query(uri, null, null, null, null)
        var token = ""
        if (cursor?.moveToFirst() == true) {
            do {
                // 遍历Cursor对象，取出数据并打印
                token = cursor.getString(cursor.getColumnIndex(NetWorkConstant.TOKEN))
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return token
    }

    private fun setXRequestidValue(): String {
        return UUID.randomUUID().toString()
    }
}
