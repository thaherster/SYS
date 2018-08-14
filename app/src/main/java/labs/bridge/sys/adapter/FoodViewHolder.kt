package labs.bridge.sys.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.food_layout.view.*

class FoodViewHolder (itemView:View) : RecyclerView.ViewHolder(itemView)
{


    var ReqId:Int =0

    val layout = itemView.layout
    val txt_title = itemView.txt_title
    val txt_time = itemView.txt_time
    val txt_location = itemView.txt_location
    val txt_expiration = itemView.txt_expiration
    val txt_food_dataveg = itemView.txt_food_dataveg
    val txt_food_datanonveg = itemView.txt_food_datanonveg



}