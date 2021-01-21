package com.fgms.mobile.network


class BaseConfigBean {

    /**
     * HttpClientOption : {"BaseAddress":"http://192.168.50.212:8081/wisx-server-mgmt/","TimeOut":20,"ServerCertCustomValid":true}
     */
    var HttpClientOption: HttpClientOptionBean? = null

    class HttpClientOptionBean {
        /**
         * BaseAddress : http://192.168.50.212:8081/wisx-server-mgmt/
         * TimeOut : 20
         * ServerCertCustomValid : true
         */
        var BaseAddress: String = ""
        var TimeOut = 0L
        var ServerCertCustomValid = false
        var caName = ""
    }
}