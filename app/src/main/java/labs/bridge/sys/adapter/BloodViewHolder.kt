package labs.bridge.sys.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.blood_layout.view.*

class BloodViewHolder (itemView:View) : RecyclerView.ViewHolder(itemView)
{


    var ReqId:Int =0
    val layout = itemView.layout
    val txt_title = itemView.txt_title
    val txt_bg = itemView.txt_bg
    val txt_location = itemView.txt_location
    val txt_expiration = itemView.txt_expiration
    val txt_blood_data = itemView.txt_blood_data



}