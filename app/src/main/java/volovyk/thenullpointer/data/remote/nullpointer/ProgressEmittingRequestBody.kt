package volovyk.thenullpointer.data.remote.nullpointer

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.InputStream

class ProgressEmittingRequestBody(
    private val fileSize: Long,
    private val inputStream: InputStream,
    private val mediaType: MediaType
) : RequestBody() {

    private val _progress = MutableStateFlow(MIN_PROGRESS)
    val progress: StateFlow<Int> = _progress

    override fun contentType(): MediaType = mediaType

    override fun contentLength(): Long = fileSize

    override fun writeTo(sink: BufferedSink) {

        val buffer = ByteArray(BUFFER_SIZE)
        var uploaded: Long = 0

        try {
            while (true) {

                val read = inputStream.read(buffer)
                if (read == -1) break

                uploaded += read
                sink.write(buffer, 0, read)

                val progress = (((uploaded / fileSize.toDouble())) * 100).toInt()
                _progress.update { progress }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream.close()
        }
    }

    companion object {
        const val BUFFER_SIZE = 1024
        const val MIN_PROGRESS = 0
        const val MAX_PROGRESS = 100
    }
}