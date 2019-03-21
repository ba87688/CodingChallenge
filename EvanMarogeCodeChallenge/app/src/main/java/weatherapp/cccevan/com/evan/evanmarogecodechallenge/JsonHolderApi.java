package weatherapp.cccevan.com.evan.evanmarogecodechallenge;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import weatherapp.cccevan.com.evan.evanmarogecodechallenge.models.Font;
import weatherapp.cccevan.com.evan.evanmarogecodechallenge.models.Information;

public interface JsonHolderApi {
    //we can get the json objects which is a list of font objects.
    // A list of font objects will be returned
    //place the relative url in a @get annotation
    @GET("fonts/all")
    Call<List<Font>> getFonts();



    @POST("makeText")
    Call<Information> createPost(@Body Information information);

//    @FormUrlEncoded
//    @POST("makeText")
//    Call<Information> createPost(
//            @Field("appid") String appid,
//            @Field("fontFamilyName") String fontFamilyName,
//            @Field("bold") boolean bold,
//            @Field("italic") boolean italic,
//            @Field("textTyped") String textTyped,
//            @Field("url") String url
//    );

//    @GET("fonts/proxima_nova_reg_it.otf")
//    Call<ResponseBody> downloadFile();
}
