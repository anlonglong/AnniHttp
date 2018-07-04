package com.aillen.annihttp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.aillen.annhttp.net.BaseHttpOutput
import com.aillen.annhttp.net.http.HttpListener
import com.aillen.annhttp.net.http.HttpMethod
import com.aillen.annhttp.net.http.anni.AnniHttp
import com.aillen.annhttp.net.http.anni.AnniRequest
import okhttp3.Call
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //这句可以放在自定义的Application中
        AnniRequest.initContext(this.applicationContext)
        val hashMapOf =
                hashMapOf("method" to "GetDistricts",
                        "version" to "cyou_1.0.0",
                        "token" to "Y|66150061c090fc07427ba9b9558960",
                        "districtPid" to "1")

        val myRequest = AnniRequest()
        myRequest.setUrl("http://115.159.71.171/api.cyoubike.com/")
        myRequest.setPostBodyMap(hashMapOf)
        AnniHttp.getAnniHttp().setAnniRequest(myRequest).executeHttpRequest(HttpMethod.POST,object : HttpListener<Entity>{
            override fun onFailure(call: Call?, e: IOException?) {

            }

            override fun onResponse(call: Call?, response: Response?, data: Entity?) {
                println("call = [${call}], response = [${response}], data = [${data}]")
            }
        })

    }

    data class Entity(val desc:String,
                      val code:Int,
                      val version:Version) : BaseHttpOutput() {
        data class Version(val versionLast:String,
                           val versionPath:String,
                           val versionDesc:String,
                           val forceUpdate:Boolean
        )
    }
}
