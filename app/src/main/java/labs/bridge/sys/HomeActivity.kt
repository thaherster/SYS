package labs.bridge.sys

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import labs.bridge.sys.adapter.UserAdapter
import labs.bridge.sys.helper.PreferenceHelper.defaultPrefs
import labs.bridge.sys.helper.PreferenceHelper.get
import labs.bridge.sys.helper.PreferenceHelper.set
import labs.bridge.sys.models.RequestData
import labs.bridge.sys.models.UserData
import labs.bridge.sys.network.MyApi
import labs.bridge.sys.network.RetrofitClient

class HomeActivity : AppCompatActivity() {
    internal lateinit var backend: MyApi
    internal  lateinit var compositeDisposable: CompositeDisposable
    private var user: UserData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        compositeDisposable = CompositeDisposable()
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        Log.i("SYS_HOME", " On Create")

        val prefs = defaultPrefs(this)
        val value: String? = prefs["USERDATA"] //getter
        val gson = Gson()
        GsonBuilder().setPrettyPrinting().create()
        val jsonparser = JsonParser()

            val json = jsonparser.parse(value)
            user = gson.fromJson(json, UserData::class.java)
//
//        fab.setOnClickListener {
//            ////
////            val prefs = defaultPrefs(this)
////            val value: String? = prefs["USERDATA"] //getter
////            val gson = Gson()
////            GsonBuilder().setPrettyPrinting().create()
////            val jsonparser =  JsonParser()
////            val json = jsonparser.parse(value)
////            Log.d("JSONDATA", json.toString())
////            val user = gson.fromJson(json, UserData::class.java)
////
////
////            Snackbar.make(view, "User name :"+ user!!.Name, Snackbar.LENGTH_LONG)
////                    .setAction("Action", null).show()
//
//            val xx = UserData()
//            prefs["USERDATA"] = xx
//
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
        //init API
        val retrofit = RetrofitClient.instance
        backend = retrofit.create(MyApi::class.java)

        //view
        recycler_users.setHasFixedSize(true)
        recycler_users.layoutManager = LinearLayoutManager(this)




    }

    override fun onResume() {
        super.onResume()
        fetchData(user!!.UserId)
    }

    private fun fetchData(userId: Int) {

      Log.i("SYS_GET", " $userId")

    compositeDisposable.add(backend.getRequests(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{requests->displayData(requests)}
                )
//// not sure
//        compositeDisposable.dispose()


    }

    private fun displayData(users: List<RequestData>?) {
        val adapter = UserAdapter(this,users!!)
        recycler_users.adapter = adapter

    }

//// not sure
//    override fun onDestroy() {
//        super.onDestroy()
//        compositeDisposable.dispose()
//
//    }

}




