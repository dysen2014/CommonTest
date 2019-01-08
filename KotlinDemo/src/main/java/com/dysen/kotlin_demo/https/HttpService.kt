package com.dysen.kotlin_demo.https

/**
 * Created by benny on 5/20/17.
 */
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url


/**
 * Created by benny on 5/20/17.
 */
object HttpService {

    val service by lazy {
        val retrofit = retrofit2.Retrofit.Builder()
                .baseUrl("http://www.imooc.com")
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build()

        retrofit.create(com.dysen.kotlin_demo.https.Service::class.java)
    }

}

interface Service {

    @GET
    fun getLogo(@Url fileUrl: String): retrofit2.Call<ResponseBody>

    @POST
    fun getDatas(@Url url: String): Call<ResponseBody>
}