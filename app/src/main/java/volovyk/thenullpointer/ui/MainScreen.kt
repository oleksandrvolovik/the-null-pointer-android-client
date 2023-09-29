package volovyk.thenullpointer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import volovyk.thenullpointer.data.entity.UploadedFile
import volovyk.thenullpointer.data.remote.entity.FileUploadState
import volovyk.thenullpointer.ui.filelist.FileList
import volovyk.thenullpointer.ui.fileuploadstatelist.FileUploadStateList
import volovyk.thenullpointer.ui.theme.AppTheme
import java.util.Date

@Composable
fun MainScreen(
    uiState: MainUiState,
    onFileClick: (UploadedFile) -> Unit,
    onShareButtonClick: (UploadedFile) -> Unit,
    onDeleteButtonClick: (UploadedFile) -> Unit,
    onUploadFileFabClick: () -> Unit,
    onFileUploadStateClick: (FileUploadState) -> Unit
) {
    Column(verticalArrangement = Arrangement.SpaceBetween) {
        Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)) {
            // Display files
            Box(modifier = Modifier.weight(1f)) {
                FileList(
                    files = uiState.files,
                    onItemClick = onFileClick,
                    onItemShareButtonClick = onShareButtonClick,
                    onItemDeleteButtonClick = onDeleteButtonClick
                )
            }

            // Display "Upload file" FAB
            UploadFileFab(
                onClick = onUploadFileFabClick
            )
        }

        // Display file upload states
        FileUploadStateList(
            fileUploadStates = uiState.fileUploadState,
            onItemClick = onFileUploadStateClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    AppTheme {
        MainScreen(
            MainUiState(
                files = listOf(
                    UploadedFile("File1.pdf", null, "link", Date(), Date()),
                    UploadedFile("File2.docx", null, "link", Date(), Date()),
                    UploadedFile("File3.jpg", null, "link", Date(), Date()),
                    UploadedFile("File4.avi", null, "link", Date(), Date())
                ), fileUploadState = listOf(
                    FileUploadState.Success("file5.mp4", "", "", Date()),
                    FileUploadState.InProgress("file6.mp3", 67),
                    FileUploadState.Failure("file7.png", "Something went wrong!", null)
                )
            ), {}, {}, {}, {}, {}
        )
    }
}