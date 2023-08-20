package volovyk.thenullpointer.ui

import android.content.ContentResolver
import android.graphics.Color
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.io.FileNotFoundException

fun Uri.length(contentResolver: ContentResolver): Long {

    val assetFileDescriptor = try {
        contentResolver.openAssetFileDescriptor(this, "r")
    } catch (e: FileNotFoundException) {
        null
    }
    // uses ParcelFileDescriptor#getStatSize underneath if failed
    val length = assetFileDescriptor?.use { it.length } ?: -1L
    if (length != -1L) {
        return length
    }

    // if "content://" uri scheme, try contentResolver table
    if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
        return contentResolver.query(this, arrayOf(OpenableColumns.SIZE), null, null, null)
            ?.use { cursor ->
                // maybe shouldn't trust ContentResolver for size: https://stackoverflow.com/questions/48302972/content-resolver-returns-wrong-size
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (sizeIndex == -1) {
                    return@use -1L
                }
                cursor.moveToFirst()
                return try {
                    cursor.getLong(sizeIndex)
                } catch (_: Throwable) {
                    -1L
                }
            } ?: -1L
    } else {
        return -1L
    }
}

fun setupCustomSnackbar(parentView: View, customView: View): Snackbar {
    val snackbar = Snackbar.make(parentView, "", Snackbar.LENGTH_INDEFINITE)

    // set the background of the default snackbar as transparent
    snackbar.view.setBackgroundColor(Color.TRANSPARENT)

    // now change the layout of the snackbar
    val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

    // set padding of the all corners as 0
    snackbarLayout.setPadding(0, 0, 0, 0)

    // add the custom snack bar layout to snackbar layout
    snackbarLayout.addView(customView, 0)
    return snackbar
}