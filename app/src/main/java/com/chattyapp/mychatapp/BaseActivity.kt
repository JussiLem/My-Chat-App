package com.chattyapp.mychatapp

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

open class BaseActivity : AppCompatActivity() {

    private var progressDialog: ProgressDialog? = null

    fun getUid(): String? {

        return FirebaseAuth.getInstance().currentUser?.uid
    }

    fun showProgressDialog() {
        if (progressDialog == null) {
            val pd = ProgressDialog(this)
            pd.setCancelable(false)
            pd.setMessage("Loading...")

            progressDialog = pd
        }

        progressDialog?.show()
    }

    fun hideProgressDialog() {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }
}
