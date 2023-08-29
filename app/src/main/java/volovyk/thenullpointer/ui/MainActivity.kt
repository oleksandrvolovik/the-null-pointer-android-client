package volovyk.thenullpointer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import volovyk.thenullpointer.ui.theme.TestComposeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestComposeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestComposeAppTheme {
        Greeting("Android")
    }
}

//@AndroidEntryPoint
//class MainActivity : AppCompatActivity() {
//
//    private val viewModel: MainViewModel by viewModels()
//    private lateinit var binding: ActivityMainBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val fileListAdapter = FileListAdapter(
//            onItemClick = {
//                val uri = Uri.parse(it.link)
//                val intent = Intent(Intent.ACTION_VIEW, uri)
//                startActivity(intent)
//            },
//            onItemLongClick = {
//
//            },
//            onItemShareButtonClick = {
//                val sendIntent: Intent = Intent().apply {
//                    action = Intent.ACTION_SEND
//                    putExtra(Intent.EXTRA_TEXT, it.link)
//                    type = "text/plain"
//                }
//
//                val shareIntent = Intent.createChooser(sendIntent, null)
//                startActivity(shareIntent)
//            }
//        )
//
//        binding.fileRecyclerView.adapter = fileListAdapter
//
//        binding.fileUploadButton.setOnClickListener {
//            val mimetypes = arrayOf("*/*")
//            openDocumentLauncher.launch(mimetypes)
//        }
//
//        lifecycleScope.launch {
//            viewModel.uiState
//                .map { it.files }
//                .distinctUntilChanged()
//                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
//                .collect { files ->
//                    fileListAdapter.submitList(files)
//                }
//        }
//
//        val fileUploadStateListAdapter = FileUploadStateListAdapter(
//            onItemClick = {
//                if (it !is FileUploadState.InProgress) {
//                    viewModel.fileUploadResultShown(it)
//                }
//            }
//        )
//        val fileUploadStateRecyclerView = binding.fileUploadStateList
//        fileUploadStateRecyclerView.adapter = fileUploadStateListAdapter
//
//        lifecycleScope.launch {
//            viewModel.uiState
//                .map { it.fileUploadState }
//                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
//                .collect { fileUploadState ->
//                    Timber.d("New file upload state: $fileUploadState")
//                    fileUploadStateListAdapter.submitList(fileUploadState)
//                }
//        }
//    }
//
//    private val openDocumentLauncher = registerForActivityResult(
//        ActivityResultContracts.OpenMultipleDocuments()
//    ) { files: List<Uri> ->
//        files.forEach { fileUri ->
//            val file = fileUri.path?.let { File(it) }
//            val fileSize = fileUri.length(contentResolver)
//            val fileInputStream = contentResolver.openInputStream(fileUri)
//            val mediaType = contentResolver.getType(fileUri)?.let { MediaType.parse(it) }
//
//            if (file != null && fileInputStream != null && mediaType != null) {
//                viewModel.uploadFile(
//                    file.name,
//                    fileSize,
//                    fileInputStream,
//                    mediaType
//                )
//            }
//        }
//    }
//}