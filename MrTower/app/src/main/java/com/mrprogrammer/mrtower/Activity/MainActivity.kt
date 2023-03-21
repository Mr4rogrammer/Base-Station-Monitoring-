package com.mrprogrammer.mrtower.Activity


import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.mrprogrammer.mrtower.Adapter.AppTourViewPageAdapter
import com.mrprogrammer.mrtower.R
import com.mrprogrammer.mrtower.Utils.CommonFunctions
import com.mrprogrammer.mrtower.Utils.Const
import com.mrprogrammer.mrtower.Utils.LocalSharedPreferences
import com.mrprogrammer.mrtower.databinding.ActivityMainBinding
import com.mrprogrammer.shop.MrToast.MrToast
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var root: ActivityMainBinding
    private var mGoogleSignClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 123
    private var mAuth: FirebaseAuth? = null
    private var reference: DatabaseReference? = null


    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val i = Intent(this, BaseActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        root = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(root.root)
        mAuth = FirebaseAuth.getInstance()
        initRadioButton()
        initViewPager()
        initTerms()
        initGoogleIcon()
        createRequest()
        root.login.setOnClickListener {
            root.progressCircular.visibility = View.VISIBLE
            loginWithGoogle()
        }
    }

    private fun createRequest() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignClient = GoogleSignIn.getClient(this, gso)
    }

    fun loginWithGoogle() {
        val sign = mGoogleSignClient!!.signInIntent
        startActivityForResult(sign, RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                root.progressCircular.visibility = View.INVISIBLE
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mAuth!!.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = mAuth!!.currentUser
                val clearedEmailString: String? = CommonFunctions.FirebaseClearString(user!!.email)
                reference = FirebaseDatabase.getInstance().getReference("Userdata")
                try {
                    if (clearedEmailString != null) {
                        reference?.child(clearedEmailString)?.child("Username")
                            ?.setValue(user.displayName)
                        reference?.child(clearedEmailString)?.child("Email")?.setValue(user.email)
                        reference?.child(clearedEmailString)?.child("Imageurl")
                            ?.setValue(Objects.requireNonNull(user.photoUrl).toString())
                        try {
                            FirebaseMessaging.getInstance().subscribeToTopic("All")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    saveUserLocallyAndChangeActivity(user)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                root.progressCircular.visibility = View.INVISIBLE
                MrToast.error(this, task.exception.toString())
            }
        }
    }

    private fun saveUserLocallyAndChangeActivity(user: FirebaseUser) {
        LocalSharedPreferences.saveUserLocally(
            this,
            user.displayName,
            user.email,
            user.photoUrl.toString()
        )
        root.progressCircular.visibility = View.INVISIBLE
        startActivity(Intent(this, BaseActivity::class.java))
        CommonFunctions.activityAnimation(this, true)
        finish()
    }

    private fun initGoogleIcon() {
        Glide.with(this).load(Const.googleLogo).into(root.googleLogo)
    }

    private fun initTerms() {
        root.terms.text = Html.fromHtml(Const.terms)
    }

    private fun initRadioButton() {
        removeChecked()
    }

    fun initViewPager() {
        root.appTourPager.adapter = AppTourViewPageAdapter(supportFragmentManager, lifecycle)
        root.appTourPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                removeChecked()
                when (position) {
                    0 -> {
                        root.first.isChecked = true
                    }
                    1 -> {
                        root.second.isChecked = true
                    }
                    2 -> {
                        root.third.isChecked = true
                    }
                    3 -> {
                        root.four.isChecked = true
                    }
                }
            }
        })
    }

    private fun removeChecked() {
        root.first.isEnabled = false
        root.second.isEnabled = false
        root.third.isEnabled = false
        root.four.isEnabled = false
    }
}