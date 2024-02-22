package volovyk.thenullpointer.ui.filelist

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import volovyk.thenullpointer.data.entity.UploadedFile
import java.util.Date

@Composable
fun FileList(
    modifier: Modifier = Modifier,
    files: List<UploadedFile>,
    onItemClick: (UploadedFile) -> Unit,
    onItemShareButtonClick: (UploadedFile) -> Unit,
    onItemOpenButtonClick: (UploadedFile) -> Unit,
    onItemDeleteButtonClick: (UploadedFile) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(files) { file ->
            FileListItem(
                file = file,
                onClick = onItemClick,
                onShareButtonClick = onItemShareButtonClick,
                onOpenButtonClick = onItemOpenButtonClick,
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
        onItemClick = {},
        onItemShareButtonClick = {},
        onItemOpenButtonClick = {},
        onItemDeleteButtonClick = {}
    )
}

