package volovyk.thenullpointer.ui.filelist

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import volovyk.thenullpointer.data.entity.UploadedFile
import java.util.Date

@Composable
fun FileList(
    files: List<UploadedFile>,
    onItemShareButtonClick: (UploadedFile) -> Unit,
    onItemDeleteButtonClick: (UploadedFile) -> Unit
) {
    LazyColumn {
        items(files) { file ->
            FileListItem(
                file = file,
                onShareButtonClick = onItemShareButtonClick,
                onDeleteButtonClick = onItemDeleteButtonClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FileListPreview() {
    FileList(
        files = listOf(
            UploadedFile("File1.pdf", null, "link", Date(), Date()),
            UploadedFile("File2.docx", null, "link", Date(), Date()),
            UploadedFile("File3.jpg", null, "link", Date(), Date()),
            UploadedFile("File4.avi", null, "link", Date(), Date())
        ),
        onItemShareButtonClick = {},
        onItemDeleteButtonClick = {}
    )
}

