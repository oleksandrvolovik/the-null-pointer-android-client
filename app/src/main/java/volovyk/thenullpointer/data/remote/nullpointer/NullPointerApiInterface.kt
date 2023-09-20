package volovyk.thenullpointer.data.remote.nullpointer

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface NullPointerApiInterface {
    @Multipart
    @POST(".")
    fun postFile(@Part file: MultipartBody.Part): Call<String>

    @POST("{filename}")
    fun deleteFile(@Path("filename") filename: String, @Body body: RequestBody): Call<Unit>
}