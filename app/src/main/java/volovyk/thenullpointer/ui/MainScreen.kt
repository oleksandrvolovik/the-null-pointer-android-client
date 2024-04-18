package volovyk.thenullpointer.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import volovyk.thenullpointer.data.entity.UploadedFile
import volovyk.thenullpointer.data.entity.FileUploadState
import volovyk.thenullpointer.ui.filelist.FileList
import volovyk.thenullpointer.ui.fileuploadstatelist.FileUploadStateList
import volovyk.thenullpointer.ui.theme.AppTheme
import java.util.Date

@Composable
fun MainScreen(
    uiState: MainUiState,
    onFileClick: (UploadedFile) -> Unit,
    onShareButtonClick: (UploadedFile) -> Unit,
    onOpenButtonClick: (UploadedFile) -> Unit,
    onDeleteButtonClick: (UploadedFile) -> Unit,
    onUploadFileFabClick: () -> Unit,
    onFileUploadStateClick: (FileUploadState) -> Unit
) {
    Scaffold(
        bottomBar = {
            FileUploadStateList(
                fileUploadStates = uiState.fileUploadState,
                onItemClick = onFileUploadStateClick
            )
        },
        floatingActionButton = {
            UploadFileFab(
                onClick = onUploadFileFabClick
            )
        }
    ) { padding ->
        FileList(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth(),
            files = uiState.files,
            onItemClick = onFileClick,
            onItemShareButtonClick = onShareButtonClick,
            onItemOpenButtonClick = onOpenButtonClick,
            onItemDeleteButtonClick = onDeleteButtonClick
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
                    UploadedFile("File1.pdf", null, "link1", Date(), Date()),
                    UploadedFile("File2.docx", null, "link2", Date(), Date()),
                    UploadedFile("File3.jpg", null, "link3", Date(), Date()),
                    UploadedFile("File4.avi", null, "link4", Date(), Date())
                ), fileUploadState = listOf(
                    FileUploadState.Success("file5.mp4", "", "", Date()),
                    FileUploadState.InProgress("file6.mp3", 67),
                    FileUploadState.Failure("file7.png", "Something went wrong!", null)
                )
            ), {}, {}, {}, {}, {}, {}
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun MainScreenPreviewDarkTheme() {
    MainScreenPreview()
}