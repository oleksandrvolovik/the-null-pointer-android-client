package volovyk.thenullpointer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType
import timber.log.Timber
import volovyk.thenullpointer.data.FileRepository
import volovyk.thenullpointer.data.entity.UploadedFile
import volovyk.thenullpointer.data.remote.entity.FileUploadState
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

data class MainUiState(
    val files: List<UploadedFile> = emptyList(),
    val fileUploadState: List<FileUploadState> = emptyList()
)

@HiltViewModel
class MainViewModel @Inject constructor(private val fileRepository: FileRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    private val fileUploadStateMap = mutableMapOf<String, FileUploadState>()

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
                    Timber.d("New file upload state: $fileUploadState")
                    fileUploadStateMap[filename] = fileUploadState
                    _uiState.update { it.copy(fileUploadState = fileUploadStateMap.values.toList()) }
                }
        }
    }

    fun deleteFile(file: UploadedFile) {
        viewModelScope.launch {
            try {
                fileRepository.deleteFile(file)
            } catch (e: IOException) {
                Timber.e(e)
            }
        }
    }

    fun fileUploadResultShown(fileUploadState: FileUploadState) {
        fileUploadStateMap.remove(fileUploadState.filename)
        _uiState.update { it.copy(fileUploadState = fileUploadStateMap.values.toList()) }
    }
}