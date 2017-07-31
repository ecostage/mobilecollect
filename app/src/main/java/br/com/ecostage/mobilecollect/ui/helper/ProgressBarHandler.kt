package br.com.ecostage.mobilecollect.ui.helper

import android.annotation.TargetApi
import android.os.Build
import android.view.View
import android.widget.ProgressBar

/**
 * Handles progress bar and container form animations.
 *
 * Created by andremaia on 7/26/17.
 */
class ProgressBarHandler {

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    fun showProgress(show: Boolean, container: View, progressBar: ProgressBar) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        container.visibility = if (show) View.GONE else View.VISIBLE
    }
}