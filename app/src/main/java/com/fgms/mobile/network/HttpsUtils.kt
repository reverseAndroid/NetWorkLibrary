package com.fgms.mobile.network

import java.io.IOException
import java.io.InputStream
import java.lang.AssertionError
import java.security.*
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*

object HttpsUtils {

    class SSLParams {
        var sSLSocketFactory: SSLSocketFactory? = null
        var trustManager: X509TrustManager? = null
    }

    fun getSslSocketFactory(certificates: Array<InputStream>, bksFile: InputStream?, password: String?): SSLParams {
        val sslParams = SSLParams()
        return try {
            val trustManagers: Array<TrustManager> = prepareTrustManager(*certificates)
            val keyManagers: Array<KeyManager>? = prepareKeyManager(bksFile, password)
            val sslContext: SSLContext = SSLContext.getInstance("TLS")
            val trustManager: X509TrustManager?
            trustManager = MyTrustManager(chooseTrustManager(trustManagers))
            sslContext.init(keyManagers, arrayOf<TrustManager>(trustManager), null)
            sslParams.sSLSocketFactory = sslContext.getSocketFactory()
            sslParams.trustManager = trustManager
            sslParams
        } catch (e: NoSuchAlgorithmException) {
            throw AssertionError(e)
        } catch (e: KeyManagementException) {
            throw AssertionError(e)
        } catch (e: KeyStoreException) {
            throw AssertionError(e)
        }
    }

    private fun prepareTrustManager(vararg certificates: InputStream): Array<TrustManager> {
        try {
            val certificateFactory: CertificateFactory = CertificateFactory.getInstance("X.509")
            val keyStore: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null)
            var index = 0
            for (certificate in certificates) {
                val certificateAlias = Integer.toString(index++)
                keyStore.setCertificateEntry(
                    certificateAlias,
                    certificateFactory.generateCertificate(certificate)
                )
                try {
                    certificate.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            val trustManagerFactory: TrustManagerFactory? = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory?.init(keyStore)
            return trustManagerFactory?.getTrustManagers()!!
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyArray()
    }

    private fun prepareKeyManager(bksFile: InputStream?, password: String?): Array<KeyManager>? {
        try {
            if (bksFile == null || password == null) return null
            val clientKeyStore: KeyStore = KeyStore.getInstance("BKS")
            clientKeyStore.load(bksFile, password.toCharArray())
            val keyManagerFactory: KeyManagerFactory =
                KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
            keyManagerFactory.init(clientKeyStore, password.toCharArray())
            return keyManagerFactory.getKeyManagers()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnrecoverableKeyException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun chooseTrustManager(trustManagers: Array<TrustManager>): X509TrustManager? {
        for (trustManager in trustManagers) {
            if (trustManager is X509TrustManager) {
                return trustManager
            }
        }
        return null
    }


    private class MyTrustManager(localTrustManager: X509TrustManager?) : X509TrustManager {
        private val defaultTrustManager: X509TrustManager
        private val localTrustManager: X509TrustManager?
        override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {

        }

        override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
            try {
                defaultTrustManager.checkServerTrusted(chain, authType)
            } catch (ce: CertificateException) {
                localTrustManager?.checkServerTrusted(chain, authType)
            }
        }

        override fun getAcceptedIssuers(): Array<X509Certificate?> {
            return arrayOfNulls(0)
        }

        init {
            val var4: TrustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            var4.init(null as KeyStore?)
            defaultTrustManager = chooseTrustManager(var4.trustManagers)!!
            this.localTrustManager = localTrustManager
        }
    }
}