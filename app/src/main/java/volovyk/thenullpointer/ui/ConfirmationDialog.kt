package volovyk.thenullpointer.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import volovyk.thenullpointer.R

@Composable
fun ConfirmationDialog(
    shown: Boolean,
    title: String,
    description: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit = {}
) {
    if (shown) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = {
                Text(title)
            },
            text = {
                Text(description)
            },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirm()
                    }) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onDismiss()
                    }) {
                    Text(stringResource(R.string.dismiss))
                }
            }
        )
    }
}