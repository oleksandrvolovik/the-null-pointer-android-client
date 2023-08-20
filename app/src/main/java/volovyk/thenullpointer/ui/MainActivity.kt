package volovyk.thenullpointer.ui

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.MediaType
import timber.log.Timber
import volovyk.thenullpointer.R
import volovyk.thenullpointer.data.remote.entity.FileUploadState
import volovyk.thenullpointer.databinding.ActivityMainBinding
import java.io.File
import java.io.FileNotFoundException

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fileListAdapter = FileListAdapter(
            onItemClick = {
                val uri = Uri.parse(it.link)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            },
            onItemLongClick = {

            },
            onItemShareButtonClick = {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, it.link)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        )

        binding.fileRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = fileListAdapter
        }

        binding.fileUploadButton.setOnClickListener {
            val mimetypes = arrayOf("*/*")
            openDocumentLauncher.launch(mimetypes)
        }

        lifecycleScope.launch {
            viewModel.uiState
                .map { it.files }
                .distinctUntilChanged()
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { files ->
                    fileListAdapter.submitList(files)
                }
        }

        lifecycleScope.launch {
            viewModel.uiState
                .map { it.fileUploadState }
                .distinctUntilChanged()
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { fileUploadState ->
                    when (fileUploadState) {
                        is FileUploadState.Success -> {
                            Timber.d("File uploaded successfully!")
                            binding.fileUploadProgressBar.isVisible = false
                            Toast.makeText(
                                this@MainActivity,
                                R.string.file_uploaded_successfully,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is FileUploadState.InProgress -> {
                            Timber.d("File upload in progress: ${fileUploadState.progress}")
                            binding.fileUploadProgressBar.isVisible = true
                            binding.fileUploadProgressBar.progress = fileUploadState.progress
                        }

                        is FileUploadState.Failure -> {
                            Timber.d("File upload failed! ${fileUploadState.message}")
                            binding.fileUploadProgressBar.isVisible = false
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.file_upload_failure, fileUploadState.message),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {}
                    }
                }
        }
    }

    private val openDocumentLauncher = registerForActivityResult<Array<String>, Uri>(
        ActivityResultContracts.OpenDocument()
    ) { fileUri: Uri? ->
        fileUri?.let {
            val file = fileUri.path?.let { File(it) }
            val fileSize = fileUri.length(contentResolver)
            val fileInputStream = contentResolver.openInputStream(fileUri)
            val mediaType = contentResolver.getType(fileUri)?.let { MediaType.parse(it) }

            if (file != null && fileInputStream != null && mediaType != null) {
                viewModel.uploadFile(
                    file.name,
                    fileSize,
                    fileInputStream,
                    mediaType
                )
            }
        }
    }

    private fun Uri.length(contentResolver: ContentResolver): Long {

        val assetFileDescriptor = try {
            contentResolver.openAssetFileDescriptor(this, "r")
        } catch (e: FileNotFoundException) {
            null
        }
        // uses ParcelFileDescriptor#getStatSize underneath if failed
        val length = assetFileDescriptor?.use { it.length } ?: -1L
        if (length != -1L) {
            return length
        }

        // if "content://" uri scheme, try contentResolver table
        if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            return contentResolver.query(this, arrayOf(OpenableColumns.SIZE), null, null, null)
                ?.use { cursor ->
                    // maybe shouldn't trust ContentResolver for size: https://stackoverflow.com/questions/48302972/content-resolver-returns-wrong-size
                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                    if (sizeIndex == -1) {
                        return@use -1L
                    }
                    cursor.moveToFirst()
                    return try {
                        cursor.getLong(sizeIndex)
                    } catch (_: Throwable) {
                        -1L
                    }
                } ?: -1L
        } else {
            return -1L
        }
    }

}