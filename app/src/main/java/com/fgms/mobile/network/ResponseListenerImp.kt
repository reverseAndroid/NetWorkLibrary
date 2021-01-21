package com.fgms.mobile.network

interface ResponseListenerImp<T> {

    fun onFail(errorMessage: String)

    fun onComplete()

    fun onSuccess(data: T)
}