package labs.bridge.sys

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_request.*
import kotlinx.android.synthetic.main.content_request.*
import labs.bridge.sys.helper.PreferenceHelper.defaultPrefs
import labs.bridge.sys.models.RequestData
import labs.bridge.sys.models.UserData
import labs.bridge.sys.network.MyApi
import labs.bridge.sys.network.RetrofitClient
import labs.bridge.sys.helper.PreferenceHelper.get

class RequestActivity : AppCompatActivity() {
    internal lateinit var backend: MyApi
    internal  lateinit var compositeDisposable: CompositeDisposable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)
        setSupportActionBar(toolbar)
        val people = intent.extras.getSerializable("Request") as? RequestData
        var numPickerVal = 0
        var secondsPickerVal = 0

        val retrofit = RetrofitClient.instance
        backend = retrofit.create(MyApi::class.java)
        compositeDisposable = CompositeDisposable()

        val prefs = defaultPrefs(this)
            val value: String? = prefs["USERDATA"] //getter
            val gson = Gson()
            GsonBuilder().setPrettyPrinting().create()
            val jsonparser =  JsonParser()
            val json = jsonparser.parse(value)
            Log.d("JSONDATA", json.toString())
            val user = gson.fromJson(json, UserData::class.java)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if(people!!.Service == "Blood"){
            titlesad.text = people.Service.trim()
            secondary.text = people.BloodGroup.trim()
            location.text = people.Location.trim()
            val sb = StringBuilder()
            sb.append(people.ResBloodBottleCount).append("/").append(people.TotalBloodBottleCount_Requested).append(" Blood Bottles Receieved")
           prim.text =sb.toString()
            sec.visibility= View.INVISIBLE
            number_picker2.visibility = View.INVISIBLE
            text2.visibility = View.INVISIBLE
            text1.text = "Bottle Count"
            number_picker1.minValue = 1
            number_picker1.maxValue = people.TotalBloodBottleCount_Requested-people.ResBloodBottleCount
            number_picker1.wrapSelectorWheel = false


            // Set number picker value changed listener
            number_picker1.setOnValueChangedListener { picker, oldVal, newVal ->
                numPickerVal= picker.value

            }

        }
        else {
            Toast.makeText(this," "+people.Id,Toast.LENGTH_SHORT).show()
            titlesad.text = people.Service.trim()
            secondary.text = people.FoodTime.trim()
            location.text = people.Location.trim()
            val sb = StringBuilder()
            sb.append(people.ResVegCount).append("/").append(people.TotalVegCount_Requested).append(" Veg Food Receieved")
           prim.text =sb.toString()

            val sb1 = StringBuilder()
            sb1.append(people.ResNonVegCount).append("/").append(people.TotalNonVegCount_Requested).append("Non Veg Food Receieved")
           sec.text =sb1.toString()
            sec.visibility= View.VISIBLE
            number_picker2.visibility = View.VISIBLE
            number_picker1.minValue = 0
            number_picker1.maxValue = people.TotalVegCount_Requested-people.ResVegCount
            number_picker2.minValue = 0
            number_picker2.maxValue = people.TotalNonVegCount_Requested-people.ResNonVegCount
            number_picker1.wrapSelectorWheel = false
            text1.text = "Veg Count"
            text2.text = "Non Veg Count"
            text2.visibility = View.VISIBLE


            // Set number picker value changed listener
            number_picker1.setOnValueChangedListener { picker, oldVal, newVal ->
                numPickerVal= picker.value

            }
            number_picker2.wrapSelectorWheel = false


            // Set number picker value changed listener
            number_picker2.setOnValueChangedListener { picker, oldVal, newVal ->
                secondsPickerVal = picker.value

            }
        }


        submit.setOnClickListener {
            if(people!!.Service == "Blood"){

                if(user!=null)
                callBackendBlood(people.Id, user.UserId, numPickerVal)

            }
            else {
                callBackendFood(people.Id, user.UserId,secondsPickerVal,numPickerVal)
            }
        }

    }

    private fun callBackendFood(FoodReqId: Int, UserId: Int, NonVegCount:Int,VegCount :Int) {
        compositeDisposable.add(backend.postFoodRequest(FoodReqId,UserId,NonVegCount,VegCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result -> Log.v("REQUEST FOOD", "" + result.Id)

                    Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()
                    finish()

                },
                        { error -> Log.e("REQUEST FOOD", error.message)
                            Toast.makeText(this,error.message,Toast.LENGTH_SHORT).show()
                        })
        )
    }

    private fun callBackendBlood(BloodReqId: Int, UserId: Int, BloodBottleCount:Int) {
        compositeDisposable.add(backend.postBloodRequest(BloodReqId,UserId,BloodBottleCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result -> Log.v("REQUEST BLOOD", "" + result.Id)

                    Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()
                    finish()


                },
                        { error -> Log.e("REQUEST BLOOD", error.message)
                            Toast.makeText(this,error.message,Toast.LENGTH_SHORT).show()
                        })
        )
    }


}
