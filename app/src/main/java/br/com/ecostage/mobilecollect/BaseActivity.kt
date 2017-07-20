package br.com.ecostage.mobilecollect

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ScrollView


/**
 * Base activity with helpful methods for all activities.
 *
 * Created by andremaia on 7/20/17.
 */
abstract class BaseActivity : AppCompatActivity() {

    fun hideSoftKeyboard(activity: BaseActivity) {
        if (activity.currentFocus == null || activity.currentFocus.windowToken == null) {
            return
        }

        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }

    fun setupKeyboardUI(view: View) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, _ ->
                if (v !is ScrollView) {
                    hideSoftKeyboard(this@BaseActivity)
                }
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            (0..view.childCount - 1)
                    .map { view.getChildAt(it) }
                    .forEach { setupKeyboardUI(it) }
        }
    }

}