package volovyk.thenullpointer.ui.fileuploadstatelist

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import volovyk.thenullpointer.R
import volovyk.thenullpointer.data.entity.FileUploadState
import volovyk.thenullpointer.ui.theme.AppTheme
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileUploadStateListItem(
    fileUploadState: FileUploadState,
    onClick: (FileUploadState) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        onClick = { onClick(fileUploadState) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            when (fileUploadState) {
                is FileUploadState.InProgress -> {
                    Text(
                        text = stringResource(
                            id = R.string.uploading_file,
                            fileUploadState.filename
                        ),
                        fontSize = 16.sp,
                        color = LocalContentColor.current
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
                            color = LocalContentColor.current
                        )
                    }
                }

                is FileUploadState.Success -> {
                    Text(
                        text = stringResource(
                            id = if (fileUploadState.token != null) {
                                R.string.file_uploaded_successfully
                            } else {
                                R.string.file_uploaded_already_exists
                            },
                            fileUploadState.filename
                        ),
                        fontSize = 16.sp,
                        color = Color.Green
                    )
                }

                is FileUploadState.Failure -> {
                    Text(
                        text = stringResource(
                            id = R.string.file_upload_failure,
                            fileUploadState.filename,
                            fileUploadState.message ?: stringResource(R.string.common_error_message)
                        ),
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
    AppTheme {
        Column {
            FileUploadStateListItem(
                modifier = Modifier.padding(8.dp),
                fileUploadState = FileUploadState.Success("file123.xyz", "", "", Date()),
                onClick = {}
            )
            FileUploadStateListItem(
                modifier = Modifier.padding(8.dp),
                fileUploadState = FileUploadState.InProgress("file456.xyz", 67),
                onClick = {}
            )
            FileUploadStateListItem(
                modifier = Modifier.padding(8.dp),
                fileUploadState = FileUploadState.Failure("file789.xyz", "Something failed!", null),
                onClick = {}
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun FileUploadStateListItemPreviewDarkTheme() {
    FileUploadStateListItemPreview()
}