package volovyk.thenullpointer.ui.fileuploadstatelist

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
import volovyk.thenullpointer.data.entity.FileUploadState
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileUploadStateList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(8.dp),
    spaceBetweenItems: Dp = 8.dp,
    fileUploadStates: List<FileUploadState>,
    onItemClick: (FileUploadState) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        reverseLayout = true,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(spaceBetweenItems)
    ) {
        items(fileUploadStates, key = { it.filename }) { fileUploadState ->
            FileUploadStateListItem(
                modifier = Modifier.animateItemPlacement(),
                fileUploadState = fileUploadState,
                onClick = onItemClick
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
            FileUploadState.Failure("file789.xyz", null)
        ),
        onItemClick = {}
    )
}