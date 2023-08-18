package volovyk.thenullpointer.data.remote.nullpointer

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface NullPointerApiInterface {
    @Multipart
    @POST(".")
    fun postFile(@Part file: MultipartBody.Part): Call<String>
}