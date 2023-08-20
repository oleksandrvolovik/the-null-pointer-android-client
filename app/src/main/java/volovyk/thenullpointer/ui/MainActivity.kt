package volovyk.thenullpointer.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.MediaType
import timber.log.Timber
import volovyk.thenullpointer.R
import volovyk.thenullpointer.data.remote.entity.FileUploadState
import volovyk.thenullpointer.databinding.ActivityMainBinding
import volovyk.thenullpointer.databinding.FileUploadSnackbarBinding
import java.io.File

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var fileUploadSnackBarBinding: FileUploadSnackbarBinding

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

        fileUploadSnackBarBinding = FileUploadSnackbarBinding.inflate(layoutInflater)

        // create an instance of the snackbar
        val fileUploadSnackbar = setupCustomSnackbar(binding.root, fileUploadSnackBarBinding.root)

        lifecycleScope.launch {
            viewModel.uiState
                .map { it.fileUploadState }
                .distinctUntilChanged()
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { fileUploadState ->
                    showFileUploadState(fileUploadState, fileUploadSnackbar)
                }
        }
    }

    private fun showFileUploadState(
        fileUploadState: FileUploadState?,
        fileUploadSnackbar: Snackbar
    ) {
        when (fileUploadState) {
            is FileUploadState.Success -> {
                Timber.d("File ${fileUploadState.filename} uploaded successfully!")
                Toast.makeText(
                    this@MainActivity,
                    getString(
                        R.string.file_uploaded_successfully,
                        fileUploadState.filename
                    ),
                    Toast.LENGTH_SHORT
                ).show()
            }

            is FileUploadState.InProgress -> {
                Timber.d("File ${fileUploadState.filename} upload in progress: ${fileUploadState.progress}")
                when (fileUploadState.progress) {
                    0 -> {
                        fileUploadSnackBarBinding.uploadingFileNameTextView.text =
                            getString(R.string.uploading_file, fileUploadState.filename)
                        fileUploadSnackbar.show()
                    }

                    100 -> {
                        fileUploadSnackbar.dismiss()
                    }

                    else -> {}
                }
                fileUploadSnackBarBinding.fileUploadProgressBar.progress =
                    fileUploadState.progress
            }

            is FileUploadState.Failure -> {
                Timber.d("File ${fileUploadState.filename} upload failed! ${fileUploadState.message}")
                Toast.makeText(
                    this@MainActivity,
                    getString(
                        R.string.file_upload_failure,
                        fileUploadState.filename,
                        fileUploadState.message
                    ),
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {}
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
}