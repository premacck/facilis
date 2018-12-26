package com.prembros.facilis.dialog

import androidx.fragment.app.DialogFragment
import com.prembros.facilis.util.doAfterDelay
import org.jetbrains.anko.support.v4.runOnUiThread

/**
 * Base class for all the dialog fragments in the app
 * Contains the functionality parts common through all the fragments.
 */
abstract class BaseDialogFragment : DialogFragment() {

    @Suppress("DeferredResultUnused")
    fun smartDismiss(afterDismissAction: () -> Unit) {
        dismiss()
        doAfterDelay(280) { runOnUiThread { afterDismissAction() } }
    }

    /**
     * Function for child dialog fragment to notify the activity whether they are ready to exit or not
     * @return false when the fragment needs to do it's own actions when back is pressed, true otherwise
     */
    open fun onBackPressed(): Boolean {
        return true
    }
}