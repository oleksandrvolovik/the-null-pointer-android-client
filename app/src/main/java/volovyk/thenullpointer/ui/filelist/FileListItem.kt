package volovyk.thenullpointer.ui.filelist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import volovyk.thenullpointer.util.getDaysDifference
import java.text.DateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileListItem(
    file: UploadedFile,
    onClick: (UploadedFile) -> Unit,
    onShareButtonClick: (UploadedFile) -> Unit,
    onOpenButtonClick: (UploadedFile) -> Unit,
    onDeleteButtonClick: (UploadedFile) -> Unit,
    modifier: Modifier = Modifier
) {
    var contextMenuExpanded by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .combinedClickable(
                onClick = { onClick(file) },
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
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(R.string.open_in_browser))
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(R.drawable.ic_open),
                            contentDescription = stringResource(R.string.open_in_browser)
                        )
                    }
                },
                onClick = {
                    onOpenButtonClick(file)
                    contextMenuExpanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(R.string.delete))
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete)
                        )
                    }
                },
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
                val dateFormat = remember {
                    DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault())
                }
                Text(
                    text = stringResource(
                        id = R.string.uploaded_at,
                        dateFormat.format(file.uploadedAt)
                    ),
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
        onClick = {},
        onShareButtonClick = {},
        onOpenButtonClick = {},
        onDeleteButtonClick = {}
    )
}