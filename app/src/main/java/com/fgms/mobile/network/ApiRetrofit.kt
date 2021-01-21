package com.fgms.mobile.network

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.SocketTimeoutException


/**
 * 2020/09/18 xujiaxuan
 *
 *单例化ApiRetrofit，暴露出设置token方法和发送请求方法还有创建被观察的接口方法，以供外部调用
 *
 * */
object ApiRetrofit : OkHttpUtils() {

    private lateinit var mBaseConfigBean: String

    private lateinit var mBeanClass: BaseConfigBean

    private var mMaxRetry: Int = 0

    private var mCurrentRetry: Int = 0

    private val mGson = GsonBuilder().setLenient().create()

    private lateinit var mBaseUrl: String

    //由于单例不能带参数，所以自定义一个初始化方法赋值
    fun initApiRetrofit(context: Context, baseConfigBean: String, beanClass: BaseConfigBean, maxRetry: Int) {
        mContext = context
        mBaseConfigBean = baseConfigBean
        mBeanClass = beanClass
        val bean = Gson().fromJson(mBaseConfigBean, mBeanClass.javaClass)
        NetWorkConstant.CONNECT_TIMEOUT = bean.HttpClientOption?.TimeOut!!
        NetWorkConstant.READ_TIMEOUT = bean.HttpClientOption?.TimeOut!!
        NetWorkConstant.WRITE_TIMEOUT = bean.HttpClientOption?.TimeOut!!
        NetWorkConstant.CA_NAME=bean.HttpClientOption?.caName!!
        mMaxRetry = maxRetry
        mBaseUrl = Gson().fromJson(mBaseConfigBean, mBeanClass.javaClass).HttpClientOption!!.BaseAddress!!
        initOkHttpUtils()
    }

    fun <T> request(observable: Observable<T>, listenerImp: ResponseListenerImp<T>) {
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<T> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(data: T) {
                    listenerImp.onSuccess(data)
                }

                override fun onError(e: Throwable) {
                    //判断是否是超时或者连接异常，如果是那就再次访问并判断次数，最大次数mMaxRetry由用户在初始化时定义
                    if ((e is SocketTimeoutException || e is ConnectException) && mCurrentRetry < mMaxRetry) {
                        mCurrentRetry++
                        request(observable, listenerImp)
                    } else {
                        e.printStackTrace()
                        listenerImp.onFail(e.message!!)
                    }
                }

                override fun onComplete() {
                    listenerImp.onComplete()
                }
            })
    }

    fun <I> createApi(api: Class<I>): I {
        val retrofit = Retrofit.Builder()
            .baseUrl(mBaseUrl)
            .client(getClient())
            .addConverterFactory(GsonConverterFactory.create(mGson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            //针对返回值是字符串不是json的工厂模式
//            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return retrofit.create(api)
    }
}
