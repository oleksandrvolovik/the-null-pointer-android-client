package volovyk.thenullpointer.ui.filelist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import volovyk.thenullpointer.R
import volovyk.thenullpointer.data.entity.UploadedFile
import volovyk.thenullpointer.ui.getDaysDifference
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileListItem(
    file: UploadedFile,
    onShareButtonClick: (UploadedFile) -> Unit,
    onDeleteButtonClick: (UploadedFile) -> Unit
) {
    var contextMenuExpanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .combinedClickable(
                onClick = {},
                onLongClick = { contextMenuExpanded = true }
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        DropdownMenu(
            expanded = contextMenuExpanded,
            onDismissRequest = { contextMenuExpanded = false }
        ) {
            DropdownMenuItem(
                text = {  Text("Delete") },
                onClick = {
                    onDeleteButtonClick(file)
                    contextMenuExpanded = false
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                Text(
                    text = file.name,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(id = R.string.uploaded_at, file.uploadedAt),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(
                        R.string.expires_in,
                        Date().getDaysDifference(file.expiresAt).toString()
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(onClick = { onShareButtonClick(file) }) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_share),
                    contentDescription = stringResource(id = R.string.share_file),
                    tint = LocalContentColor.current
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun FileListItemPreview() {
    FileListItem(
        file = UploadedFile(
            "file123.xyz", null, "", Date(), Date()
        ),
        onShareButtonClick = {},
        onDeleteButtonClick = {}
    )
}