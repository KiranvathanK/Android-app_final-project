package shashi.com.driving_style.api;


import shashi.com.driving_style.models.AddResultInput;
import shashi.com.driving_style.models.DeleteLogsInput;
import shashi.com.driving_style.models.GetLogResult;
import shashi.com.driving_style.models.UserLoginInput;
import shashi.com.driving_style.models.UserLoginResult;
import shashi.com.driving_style.models.UserRegisterInput;
import shashi.com.driving_style.models.UserRegisterResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface WebServices {

    @POST("UserLogin_c/userLogin")
    Call<UserLoginResult> BplugdUserLogin(@Body UserLoginInput input);

    @POST("UserRegister_c/userRegister")
    Call<UserRegisterResult> UserRegister(@Body UserRegisterInput input);

    @POST("UserRegister_c/result")
    Call<UserRegisterResult> addres(@Body AddResultInput input);

    @POST("UserLogin_c/GetResults")
    Call<GetLogResult> timetaken(@Body AddResultInput input);

    @POST("UserLogin_c/updateFeedback")
    Call<GetLogResult> dell(@Body DeleteLogsInput input);

    @POST("UserLogin_c/DelResults")
    Call<GetLogResult> de(@Body DeleteLogsInput input);

    @POST("UserLogin_c/deleteFeedback")
    Call<GetLogResult> delll(@Body DeleteLogsInput input);

    @POST("UserLogin_c/AddFeedback")
    Call<UserRegisterResult> fee(@Body AddResultInput input);

    @POST("UserLogin_c/GetResults")
    Call<GetLogResult> getLogs();

    @POST("UserLogin_c/getFeedback")
    Call<GetLogResult> getFed();
}
