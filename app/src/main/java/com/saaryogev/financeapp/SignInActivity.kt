package com.saaryogev.financeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

/**
 * Activity to demonstrate basic retrieval of the Google user's ID, email address, and basic
 * profile.
 */
class SignInActivity : AppCompatActivity(), View.OnClickListener {
    private var mGoogleSignInClient: GoogleSignInClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Button listeners
        findViewById<View>(R.id.sign_in_button).setOnClickListener(this)
        findViewById<View>(R.id.sign_out_button).setOnClickListener(this)
        findViewById<View>(R.id.disconnect_button).setOnClickListener(this)

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("378599466850-sgi76u1aobsaagoav2278cbqd879pgl5.apps.googleusercontent.com")
                .build()
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        // [END build_client]

        // [START customize_button]
        // Set the dimensions of the sign-in button.
        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT)
        // [END customize_button]
    }

    public override fun onStart() {
        super.onStart()

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            startActivity(Intent(this, AddExpenseActivity::class.java).apply {
                putExtra(
                    "account",
                    account
                )
            })
        }
        // [END on_start_sign_in]
    }

    // [START onActivityResult]
    public override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    // [END onActivityResult]
    // [START handleSignInResult]
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)
            if (account != null) {
                startActivity(Intent(this, AddExpenseActivity::class.java).apply {
                    putExtra(
                        "account",
                        account
                    )
                })
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(
                TAG,
                "signInResult:failed code=" + e.statusCode
            )
        }
    }

    // [END handleSignInResult]
    // [START signIn]
    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // [END signIn]
    // [START signOut]
    private fun signOut() {
        mGoogleSignInClient!!.signOut()
            .addOnCompleteListener(this) {}
    }

    // [END signOut]
    // [START revokeAccess]
    private fun revokeAccess() {
        mGoogleSignInClient!!.revokeAccess()
            .addOnCompleteListener(this) {}
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.sign_in_button -> signIn()
            R.id.sign_out_button -> signOut()
            R.id.disconnect_button -> revokeAccess()
        }
    }

    companion object {
        private const val TAG = "SignInActivity"
        private const val RC_SIGN_IN = 9001
    }
}