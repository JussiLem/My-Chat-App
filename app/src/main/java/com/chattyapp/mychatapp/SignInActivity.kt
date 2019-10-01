package com.chattyapp.mychatapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chattyapp.mychatapp.data.User
import com.chattyapp.mychatapp.util.EmailValidator
import com.chattyapp.timber.Timber
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_google.*

class SignInActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val emailValidator = EmailValidator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google)
        Timber.tag(TAG)
        Timber.d { "Activity Created" }
        // Button listeners
        signInButton.setOnClickListener(this)
        signOutButton.setOnClickListener(this)
//        disconnectButton.setOnClickListener(this)

        // configure google sign in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
    }

    public override fun onStart() {
        super.onStart()
        auth.currentUser?.let {
            onAuthSuccess(it)
        }
    }

    private fun onAuthSuccess(user: FirebaseUser) {
        // Write new user
        when (emailValidator.afterTextChanged(user.email)) {
            false -> {
                Toast.makeText(
                    baseContext, "Failed to parse the email address",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                val username = usernameFromEmail(user.email.toString())
                writeNewUser(user.uid, username, user.email.toString(), user.photoUrl.toString())
            }
        }
        
        // Go to MainActivity
        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        finish()
    }

      private fun usernameFromEmail(email: String): String {
        return if (email.contains("@")) {
            email.split("@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        } else {
            email
        }
    }


    private fun writeNewUser(userId: String, name: String, email: String, photoUrl: String) {
        database.child("users").child(userId).setValue(User(name, email, photoUrl))
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Timber.w(e) { "Google sign in failed" }
                Snackbar.make(main_layout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                // [START_EXCLUDE]
                updateUI(null)
                // [END_EXCLUDE]
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Timber.d{ "firebaseAuthWithGoogle:${acct.id}" }
        // [START_EXCLUDE silent]
        setProgressDialog(true)
        // [END_EXCLUDE]

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d{ "signInWithCredential:success" }
                    val user = auth.currentUser

                    auth.currentUser?.let {
                        onAuthSuccess(it)
                    }
                    user?.providerData?.forEach {
                        Timber.d{ "${it.photoUrl}" }
                    }

                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w { "signInWithCredential:failure ${task.exception}" }
                    Snackbar.make(main_layout, "Authentication Failed.", Snackbar.LENGTH_SHORT)
                        .show()
                    updateUI(null)
                }

                // [START_EXCLUDE]
                setProgressDialog(false)
                // [END_EXCLUDE]
            }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        // Firebase sign out
        auth.signOut()

        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this) {
            updateUI(null)
        }
    }

    private fun revokeAccess() {
        // Firebase sign out
        auth.signOut()

        // Google revoke access
        googleSignInClient.revokeAccess().addOnCompleteListener(this) {
            updateUI(null)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        setProgressDialog(false)
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            status.text = getString(R.string.google_status_fmt, user.email)
            detail.text = getString(R.string.firebase_status_fmt, user.uid)

            signOutButton.visibility = View.GONE
            signOutAndDisconnect.visibility = View.VISIBLE
        } else {
            status.setText(R.string.signed_out)
            detail.text = null

            signInButton.visibility = View.VISIBLE
            signOutAndDisconnect.visibility = View.GONE
        }
    }

    private fun setProgressDialog(show: Boolean) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(R.layout.layout_loading_dialog)
        val dialog: Dialog = builder.create()
        when (show) {
            true -> dialog.show()
            else -> dialog.dismiss()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.signInButton -> signIn()
            R.id.signOutButton -> signOut()
//            R.id.disconnectButton -> revokeAccess()
        }
    }

    companion object {
        private const val TAG = "SignInActivity"
        private const val RC_SIGN_IN = 9001
    }
}
