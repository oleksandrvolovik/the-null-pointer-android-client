package volovyk.thenullpointer.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import volovyk.thenullpointer.R
import volovyk.thenullpointer.data.remote.entity.FileUploadState
import volovyk.thenullpointer.ui.theme.AppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val uiState by viewModel.uiState.collectAsState()
                    MainScreen(
                        uiState = uiState,
                        onShareButtonClick = {
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, it.link)
                                type = "text/plain"
                            }

                            val shareIntent = Intent.createChooser(sendIntent, null)
                            startActivity(shareIntent)
                        },
                        onUploadFileFabClick = {
                            val mimetypes = arrayOf("*/*")
                            openDocumentLauncher.launch(mimetypes)
                        },
                        onFileUploadStateClick = {
                            if (it !is FileUploadState.InProgress) {
                                viewModel.fileUploadResultShown(it)
                            }
                        }
                    )
                }
            }
        }
    }

    private val openDocumentLauncher = registerForActivityResult(
        ActivityResultContracts.OpenMultipleDocuments()
    ) { files: List<Uri> ->
        files.forEach { fileUri ->
            val filename = fileUri.getFileName(contentResolver)
            val fileSize = fileUri.length(contentResolver)
            val fileInputStream = contentResolver.openInputStream(fileUri)
            val mediaType = contentResolver.getType(fileUri)?.let { MediaType.parse(it) }

            if (filename != null && fileInputStream != null && mediaType != null) {
                if (filename !in viewModel.uiState.value.fileUploadState.map { it.filename }) {
                    viewModel.uploadFile(
                        filename,
                        fileSize,
                        fileInputStream,
                        mediaType
                    )
                } else {
                    Toast.makeText(this, R.string.file_already_being_uploaded, Toast.LENGTH_SHORT)
                }
            }
        }
    }
}