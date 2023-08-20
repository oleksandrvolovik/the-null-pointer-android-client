package volovyk.thenullpointer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType
import volovyk.thenullpointer.data.FileRepository
import volovyk.thenullpointer.data.entity.UploadedFile
import volovyk.thenullpointer.data.remote.entity.FileUploadState
import java.io.InputStream
import javax.inject.Inject

data class MainUiState(
    val files: List<UploadedFile> = emptyList(),
    val fileUploadState: FileUploadState? = null
)

@HiltViewModel
class MainViewModel @Inject constructor(private val fileRepository: FileRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        viewModelScope.launch {
            fileRepository.getFilesFlow().collect { files ->
                _uiState.update {
                    it.copy(files = files)
                }
            }
        }
    }

    fun uploadFile(
        filename: String,
        fileSize: Long,
        inputStream: InputStream,
        mediaType: MediaType
    ) {
        viewModelScope.launch {
            fileRepository.uploadFile(filename, fileSize, inputStream, mediaType)
                .collect { fileUploadState ->
                    _uiState.update {
                        it.copy(fileUploadState = fileUploadState)
                    }
                }
        }
    }
}