package volovyk.thenullpointer.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.MediaType
import volovyk.thenullpointer.databinding.ActivityMainBinding
import java.io.File

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
    }

    private val openDocumentLauncher = registerForActivityResult<Array<String>, Uri>(
        ActivityResultContracts.OpenDocument()
    ) { fileUri: Uri? ->
        fileUri?.let {
            val file = fileUri.path?.let { File(it) }
            val fileInputStream = contentResolver.openInputStream(fileUri)
            val mediaType = contentResolver.getType(fileUri)?.let { MediaType.parse(it) }

            if (file != null && fileInputStream != null && mediaType != null) {
                viewModel.uploadFile(
                    file.name,
                    fileInputStream,
                    mediaType
                )
            }
        }
    }

}