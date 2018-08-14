package labs.bridge.sys.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import labs.bridge.sys.LoginActivity
import labs.bridge.sys.R
import labs.bridge.sys.RequestActivity
import labs.bridge.sys.models.RequestData

class UserAdapter(internal var context: Context,internal var requestList :List<RequestData>) :RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return when (viewType) {
            0 -> BloodViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.blood_layout,parent,false))
            else ->FoodViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.food_layout,parent,false))
        }

    }

    override fun getItemCount(): Int {
       return requestList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(requestList[position].Service == "Blood")
            0
        else
            1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(requestList[position].Service == "Blood")
        { (holder as BloodViewHolder)
            // Blood
            holder.ReqId = requestList[position].Id
            holder.txt_title.text = requestList[position].Service
            holder.txt_bg.text = requestList[position].BloodGroup
            holder.txt_location.text = requestList[position].Location
            val sb = StringBuilder()
            sb.append(requestList[position].ResBloodBottleCount).append("/").append(requestList[position].TotalBloodBottleCount_Requested)
            holder.txt_blood_data.text =sb.toString()
            holder.txt_expiration.text = requestList[position].ReqDate
            holder.layout.setOnClickListener {
              if(requestList[position].ResBloodBottleCount!=requestList[position].TotalBloodBottleCount_Requested)
              {showFoodPopup(requestList[position])}
                else{
                  Toast.makeText(context,"All request met!! Thanks",Toast.LENGTH_LONG).show()
              }


            }
        }
        else if(requestList[position].Service == "Food")
        {
            (holder as FoodViewHolder)
            holder.ReqId = requestList[position].Id
            holder.txt_title.text = requestList[position].Service
            holder.txt_time.text = requestList[position].FoodTime

            holder.txt_location.text = requestList[position].Location
            val sb = StringBuilder()
            sb.append(requestList[position].ResVegCount).append("/").append(requestList[position].TotalVegCount_Requested)
            holder.txt_food_dataveg.text =sb.toString()

            val sb1 = StringBuilder()
            sb1.append(requestList[position].ResNonVegCount).append("/").append(requestList[position].TotalNonVegCount_Requested)
            holder.txt_food_datanonveg.text =sb1.toString()

            holder.txt_expiration.text = requestList[position].ReqDate
            holder.layout.setOnClickListener {
                if(requestList[position].ResVegCount!=requestList[position].TotalVegCount_Requested||requestList[position].ResNonVegCount!=requestList[position].TotalNonVegCount_Requested)
                {
                showFoodPopup(requestList[position])}
                else{
                    Toast.makeText(context,"All request met!! Thanks",Toast.LENGTH_LONG).show()

                }

            }
         }

    }

    private fun showFoodPopup(requestData: RequestData) {
//        val intent = Intent(this, LoginActivity::class.java)
//        intent.putExtra("Request", requestData)
//        startActivity(intent)
        val intent = Intent(context, RequestActivity::class.java)
        intent.putExtra("Request", requestData)
        context.startActivity(intent)

    }


}