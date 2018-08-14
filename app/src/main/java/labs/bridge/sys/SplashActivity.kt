package labs.bridge.sys

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import labs.bridge.sys.helper.PreferenceHelper.defaultPrefs
import labs.bridge.sys.helper.PreferenceHelper.get
import labs.bridge.sys.models.UserData

class SplashActivity : AppCompatActivity() {

    private var mDelayHandler: Handler? = null
    private val splashDelay: Long = 2000 //2 seconds

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {

            val prefs = defaultPrefs(this)
            val value: String? = prefs["USERDATA"] //getter
            val gson = Gson()
            GsonBuilder().setPrettyPrinting().create()
            val jsonparser = JsonParser()
            if (value == null) {


//            val user = gson.fromJson(json, UserData::class.java)
//                if(user ==null)
//                {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val json = jsonparser.parse(value)
                val user = gson.fromJson(json, UserData::class.java)
                if (user.UserId>0) {
                    Log.d("JSONDATA", json.toString())
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            else {
                Log.d("JSONDATA", json.toString())
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
            }
        }


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Log.i("SYS_SPLASH", " On Create")

        //Initialize the Handler
        mDelayHandler = Handler()

        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, splashDelay)
    }

    public override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }


}
