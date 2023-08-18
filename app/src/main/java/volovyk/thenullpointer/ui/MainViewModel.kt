package volovyk.thenullpointer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.MediaType
import volovyk.thenullpointer.data.FileRepository
import volovyk.thenullpointer.data.entity.UploadedFile
import java.io.InputStream
import javax.inject.Inject

data class MainUiState(
    val files: List<UploadedFile> = emptyList()
)

@HiltViewModel
class MainViewModel @Inject constructor(private val fileRepository: FileRepository) : ViewModel() {

    val uiState: StateFlow<MainUiState> =
        fileRepository.getFilesFlow()
            .map { MainUiState(files = it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MainUiState()
            )

    fun uploadFile(filename: String, inputStream: InputStream, mediaType: MediaType) {
        viewModelScope.launch {
            fileRepository.uploadFile(filename, inputStream, mediaType)
        }
    }
}