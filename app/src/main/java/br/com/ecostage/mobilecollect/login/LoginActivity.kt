package br.com.ecostage.mobilecollect.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import br.com.ecostage.mobilecollect.R
import br.com.ecostage.mobilecollect.map.MapActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*


/**
 * A sign in  screen that offers sign in via email/password and google.
 */
class LoginActivity : AppCompatActivity(), LoginView {

    // TODO: Inject LoginActivity with dagger.
    private var presenter: LoginPresenter? =  null
    private val RC_SIGN_IN = 9001
    private val TAG = "LoginActivity"


    override fun onStart() {
        super.onStart()
        presenter?.onStart()
    }

    override fun onStop() {
        super.onStop()
        presenter?.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presenter = LoginPresenterImpl(this, this, getString(R.string.default_web_client_id))
        presenter?.onCreate()

//        email_sign_in_button.clearFocus()

        email_sign_in_button.setOnClickListener { attemptSignInWithEmail() }
        google_sign_in_button.setOnClickListener { attemptSignInWithGoogle() }
        setGooglePlusButtonText(google_sign_in_button, getString(R.string.action_sign_in_with_google))
        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptSignInWithEmail()
                return@OnEditorActionListener true
            }
            false
        })
    }

    protected fun setGooglePlusButtonText(signInButton: SignInButton, buttonText: String) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (i in 0..signInButton.childCount - 1) {
            val v = signInButton.getChildAt(i)

            if (v is TextView) {
                v.text = buttonText
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                val account = result.signInAccount
                firebaseAuthWithGoogle(account)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {

        if (acct == null) {
            Log.e(TAG, "firebaseAuthWithGoogle: acct is null")
            return

        }

        Log.d(TAG, "firebaseAuthWithGoogle: ${acct.id}")

        showProgress(true)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        presenter?.signInWithCredential(credential)
    }

    override fun showProgress() {
        showProgress(true)
    }

    override fun hideProgress() {
        showProgress(false)
    }

    override fun navigateToHome() {
        val intent = Intent(this@LoginActivity, MapActivity::class.java)
        this@LoginActivity.startActivity(intent)
        finish()
    }

    override fun setUsernameError() {
        // Reset errors.
        email.error = null
        email.error = getString(R.string.error_invalid_email)
        requestFocus(email)
    }

    override fun setPasswordError() {
        // Reset errors.
        password.error = null
        password.error = getString(R.string.error_invalid_password)
        requestFocus(password)
    }

    override fun showSignInWithFailure() {
        showToast(getString(R.string.sign_in_with_google_failure_message))
    }

    override fun showSignInWithEmailFailure() {
        showToast(getString(R.string.sign_in_with_email_failure_message))
    }

    private fun showToast(message: String?) {
        hideProgress()
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
                .show()
    }

    private fun requestFocus(focusView: View?) {
        // There was an error; don't attempt login and focus the first
        // form field with an error.
        focusView?.requestFocus()
    }

    private fun attemptSignInWithGoogle() {
        presenter?.validateGoogleCredentials()
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptSignInWithEmail() {
        presenter?.validateCredentials(email.text.toString(), password.text.toString())
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            login_form.visibility = if (show) View.GONE else View.VISIBLE
            login_form.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 0 else 1).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            login_form.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_progress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            login_progress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }
}
