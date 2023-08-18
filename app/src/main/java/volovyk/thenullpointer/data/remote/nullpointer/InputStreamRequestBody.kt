package volovyk.thenullpointer.data.remote.nullpointer

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.Okio
import java.io.IOException
import java.io.InputStream

class InputStreamRequestBody(
    private val inputStream: InputStream,
    private val mediaType: MediaType
) : RequestBody() {

    override fun contentType(): MediaType {
        return mediaType
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return -1
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        sink.writeAll(Okio.source(inputStream))
    }

}