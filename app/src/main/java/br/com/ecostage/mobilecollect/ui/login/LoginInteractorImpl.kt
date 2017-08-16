package br.com.ecostage.mobilecollect.ui.login

import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth

class LoginInteractorImpl(val emailSignInListener: LoginInteractor.OnEmailSignInFinishedListener,
                          val googleSignInListener: LoginInteractor.OnSignInWithGoogleFinishedListener,
                          var loginActivity: LoginActivity,
                          var googleApiToken: String)
    : LoginInteractor, GoogleApiClient.OnConnectionFailedListener {

    lateinit private var mAuthListener: FirebaseAuth.AuthStateListener

    private val RC_SIGN_IN = 9001

    private var mGoogleApiClient: GoogleApiClient? = null
    private val mAuth = FirebaseAuth.getInstance()

    override fun startListener() {
        mAuth.addAuthStateListener(mAuthListener)
    }

    override fun stopListener() {
        mAuth.removeAuthStateListener(mAuthListener)
    }

    override fun createListener() {
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                emailSignInListener.onSuccess()
            }
        }

        setupSignInWithGoogle()

    }

    override fun signInWithGoogle(listener: LoginInteractor.OnSignInWithGoogleFinishedListener) {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        loginActivity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun signInWithCredential(credential: AuthCredential, listener: LoginInteractor.OnSignInWithGoogleFinishedListener) {
        mAuth?.signInWithCredential(credential)
                ?.addOnFailureListener {
                    if (it is FirebaseNetworkException)
                        listener.onConnectionFailedWithGoogle()
                    else
                        listener.onFailureWithGoogle()
                }
    }

    private fun setupSignInWithGoogle() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(googleApiToken)
                .requestEmail()
                .build()


        mGoogleApiClient = GoogleApiClient.Builder(loginActivity)
                .enableAutoManage(loginActivity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

    }

    override fun signInWithEmail(username: String, password: String, listener: LoginInteractor.OnEmailSignInFinishedListener) {
        if (!isEmailValid(username)) {
            listener.onUsernameError()
        } else if (!isPasswordValid(password)) {
            listener.onPasswordError()
        } else {

            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnFailureListener { listener.onFailureWithEmail() }
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        googleSignInListener.onConnectionFailedWithGoogle()
    }

    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length > 6
    }
}