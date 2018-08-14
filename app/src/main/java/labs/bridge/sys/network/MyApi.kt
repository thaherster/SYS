package labs.bridge.sys.network

import io.reactivex.Observable
import labs.bridge.sys.models.*
import retrofit2.http.*

interface MyApi {


    @GET("User/GetUsers")
    fun getUsers(): Observable<List<UserDataFull>>

    @GET("Service/RequestsBasedOnLocation")
    fun getRequests(@Query("UserId") UserId: Int)
            : Observable<List<RequestData>>

    @FormUrlEncoded
    @POST("User/PostUser")
    fun registrationUser(@Field("CategoryId") CategoryId: Int,
                         @Field("Name") Name: String,
                         @Field("Email") Email: String,
                         @Field("PhoneNo") PhoneNo: String,
                         @Field("Password") Password: String,
                         @Field("UserTypeId") UserTypeId: Int,
                         @Field("Location") Location: String): Observable<UserData>
    @FormUrlEncoded
    @POST("User/LoginApp")
    fun loginUser(
                         @Field("UserName") Email: String,
                         @Field("Password") Password: String,
                         @Field("UserTypeId") UserTypeId: Int): Observable<UserData>

    @FormUrlEncoded
    @POST("Service/PostBloodResponse")
    fun postBloodRequest(
            @Field("BloodReqId") BloodReqId: Int,
            @Field("UserId") UserId: Int,
            @Field("BloodBottleCount") BloodBottleCount: Int): Observable<BloodResponse>

    @FormUrlEncoded
    @POST("Service/PostFoodResponse")
    fun postFoodRequest(
            @Field("FoodReqId") FoodReqId: Int,
            @Field("UserId") UserId: Int,
            @Field("NonVegCount") NonVegCount: Int,
            @Field("VegCount") VegCount: Int): Observable<FoodResponse>
}