package volovyk.thenullpointer.ui.fileuploadstatelist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import volovyk.thenullpointer.data.remote.entity.FileUploadState
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileUploadStateListItem(
    fileUploadState: FileUploadState,
    onClick: (FileUploadState) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        shape = MaterialTheme.shapes.medium,
        onClick = { onClick(fileUploadState) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            when (fileUploadState) {
                is FileUploadState.InProgress -> {
                    Text(
                        text = "Uploading: ${fileUploadState.filename}",
                        fontSize = 16.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LinearProgressIndicator(
                            progress = fileUploadState.progress / 100f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(4.dp)
                        )

                        Text(
                            text = "${fileUploadState.progress}%",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                }

                is FileUploadState.Success -> {
                    Text(
                        text = "File uploaded successfully: ${fileUploadState.filename}",
                        fontSize = 16.sp,
                        color = Color.Green
                    )
                }

                is FileUploadState.Failure -> {
                    Text(
                        text = "File upload failed: ${fileUploadState.filename}, ${fileUploadState.message}",
                        fontSize = 16.sp,
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun FileUploadStateListItemPreview() {
    Column {
        FileUploadStateListItem(
            fileUploadState = FileUploadState.Success("file123.xyz", "", "", Date()),
            onClick = {}
        )
        FileUploadStateListItem(
            fileUploadState = FileUploadState.InProgress("file456.xyz", 67),
            onClick = {}
        )
        FileUploadStateListItem(
            fileUploadState = FileUploadState.Failure("file789.xyz", "Something failed!", null),
            onClick = {}
        )
    }
}