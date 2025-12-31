package dev.baharudin.ui.util

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Extension functions for showing Toast messages.
 */

/**
 * Show a short Toast message.
 */
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/**
 * Remember a function to show Toast messages.
 * Use this in Composables to avoid recreating the function on every recomposition.
 */
@Composable
fun rememberToastHandler(): (String) -> Unit {
    val context = LocalContext.current
    return remember { { message: String -> context.showToast(message) } }
}

