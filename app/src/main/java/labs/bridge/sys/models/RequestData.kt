package labs.bridge.sys.models

import java.io.Serializable

class RequestData : Serializable  {
    var  Id :Int =0
    var  Service : String = ""
    var  ReqDate : String = ""
    var  Location : String = ""
    var  TotalVegCount_Requested:Int=0
    var  TotalNonVegCount_Requested:Int=0
    var  FoodTime : String = ""
    var  BloodGroup : String = ""
    var  TotalBloodBottleCount_Requested:Int=0
    var  RespondedDate : String = ""
    var  ResVegCount:Int=0
    var  ResNonVegCount :Int=0
    var  ResBloodBottleCount:Int=0
}