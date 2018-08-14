package labs.bridge.sys

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import labs.bridge.sys.helper.PreferenceHelper.defaultPrefs
import labs.bridge.sys.helper.PreferenceHelper.set
import labs.bridge.sys.network.MyApi
import labs.bridge.sys.network.RetrofitClient
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {
    internal lateinit var backend: MyApi
    internal  lateinit var compositeDisposable: CompositeDisposable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        Log.i("SYS_SIGNUP", " On Create")

        val retrofit = RetrofitClient.instance
        backend = retrofit.create(MyApi::class.java)
        compositeDisposable = CompositeDisposable()
        signup.setOnClickListener {

            attemptSignUp()

        }

        signin.setOnClickListener{
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }



    private fun attemptSignUp() {


        // Reset errors.
        emailup.error = null
        passwordup.error = null

        // Store values at the time of the login attempt.
        val nameStr = nameup.text.toString()
        val emailStr = emailup.text.toString()
        val phoneStr = phoneup.text.toString()
        val locationStr = locationup.text.toString()
        val passwordStr = passwordup.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.


        // Check for a valid fields.
        if (TextUtils.isEmpty(nameStr)) {
            nameup.error = getString(R.string.error_field_required)
            focusView = nameup
            cancel = true
        } else  if (TextUtils.isEmpty(emailStr)) {
            emailup.error = getString(R.string.error_field_required)
            focusView = emailup
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            emailup.error = getString(R.string.error_invalid_email)
            focusView = emailup
            cancel = true
        }else  if (TextUtils.isEmpty(phoneStr)) {
            phoneup.error = getString(R.string.error_field_required)
            focusView = phoneup
            cancel = true
        }
        else  if (TextUtils.isEmpty(locationStr)) {
            locationup.error = getString(R.string.error_field_required)
            focusView = locationup
            cancel = true
        }
        else if (TextUtils.isEmpty(passwordStr) || !isPasswordValid(passwordStr)) {
            passwordup.error = getString(R.string.error_invalid_password)
            focusView = passwordup
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user signup attempt.
            showProgress(true)
            callBackend(nameStr,emailStr,phoneStr,locationStr,passwordStr)

        }
    }

    private fun callBackend(name: String, email: String, phone: String, location: String,password: String) {


        compositeDisposable.add(backend.registrationUser(2,name,email,phone,password,1,location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result -> Log.v("REGISTRATION", "" + result.Name)
                    showProgress(false)

                    val prefs = defaultPrefs(this)
                    prefs["USERDATA"] = result
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                },
                        { error -> Log.e("REGISTRATION", error.message)
                            showProgress(false)
                            Toast.makeText(this,error.message,Toast.LENGTH_SHORT).show()
                        })
        )
////// not sure
//        compositeDisposable.dispose()
    }



//    private fun isEmailValid(email: String): Boolean {
//        //TODO: Replace this with your own logic
//        return emailup.contains("@")
//    }

    private  fun isEmailValid(email: String): Boolean {
        return Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return passwordup.length() > 4
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            signup_form.visibility = if (show) View.GONE else View.VISIBLE
            signup_form.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 0 else 1).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            signup_form.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            signup_progress.visibility = if (show) View.VISIBLE else View.GONE
            signup_progress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            signup_progress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            signup_progress.visibility = if (show) View.VISIBLE else View.GONE
            signup_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

}



