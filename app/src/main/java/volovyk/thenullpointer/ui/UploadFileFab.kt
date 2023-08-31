package volovyk.thenullpointer.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import volovyk.thenullpointer.R

@Composable
fun UploadFileFab(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(id = R.string.upload_file),
            tint = Color.White
        )
    }
}

@Composable
@Preview(showBackground = true)
fun MyFloatingActionButtonPreview() {
    UploadFileFab(onClick = { /* Handle FAB click */ })
}