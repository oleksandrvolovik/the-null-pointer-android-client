package volovyk.thenullpointer.ui.fileuploadstatelist

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import volovyk.thenullpointer.data.remote.entity.FileUploadState
import java.util.Date

@Composable
fun FileUploadStateList(
    fileUploadStates: List<FileUploadState>,
    onItemClick: (FileUploadState) -> Unit
) {
    LazyColumn(reverseLayout = true) {
        items(fileUploadStates) { fileUploadState ->
            FileUploadStateListItem(
                fileUploadState, onItemClick
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun FileUploadStateListPreview() {
    FileUploadStateList(
        fileUploadStates = listOf(
            FileUploadState.Success("file123.xyz", "", "", Date()),
            FileUploadState.InProgress("file456.xyz", 67),
            FileUploadState.Failure("file789.xyz", "Something failed!", null)
        ),
        onItemClick = {}
    )
}