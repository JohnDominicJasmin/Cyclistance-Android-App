package com.myapp.cyclistance.feature_authentication.domain.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.myapp.cyclistance.R


class AuthResult :
    ActivityResultContract<Int, Task<GoogleSignInAccount>?>() {


    override fun createIntent(context: Context, input: Int): Intent {

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestServerAuthCode(context.getString(R.string.default_web_client_id), false)
            .requestEmail()
            .requestProfile()
            .build()

        val signInClient = GoogleSignIn.getClient(context, googleSignInOptions)
        return signInClient.signInIntent.putExtra("Input", input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Task<GoogleSignInAccount>? {
        return when (resultCode) {
            Activity.RESULT_OK -> GoogleSignIn.getSignedInAccountFromIntent(intent)
            else -> null
        }
    }

}