package volovyk.thenullpointer.ui.filelist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import volovyk.thenullpointer.data.entity.UploadedFile
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(8.dp),
    spaceBetweenItems: Dp = 8.dp,
    files: List<UploadedFile>,
    onItemClick: (UploadedFile) -> Unit,
    onItemShareButtonClick: (UploadedFile) -> Unit,
    onItemOpenButtonClick: (UploadedFile) -> Unit,
    onItemDeleteButtonClick: (UploadedFile) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(spaceBetweenItems)
    ) {
        items(files, key = { it.link }) { file ->
            FileListItem(
                modifier = Modifier.animateItemPlacement(),
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
            UploadedFile("File1.pdf", null, "link1", Date(), Date()),
            UploadedFile("File2.docx", null, "link2", Date(), Date()),
            UploadedFile("File3.jpg", null, "link3", Date(), Date()),
            UploadedFile("File4.avi", null, "link4", Date(), Date())
        ),
        onItemClick = {},
        onItemShareButtonClick = {},
        onItemOpenButtonClick = {},
        onItemDeleteButtonClick = {}
    )
}

