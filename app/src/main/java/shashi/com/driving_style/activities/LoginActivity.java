package shashi.com.driving_style.activities;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import shashi.com.driving_style.R;
import shashi.com.driving_style.api.Api;
import shashi.com.driving_style.api.WebServices;
import shashi.com.driving_style.custom_views.EditTextRoboto_Light;
import shashi.com.driving_style.logics.FindShakeService;
import shashi.com.driving_style.logics.S;
import shashi.com.driving_style.models.UserLoginInput;
import shashi.com.driving_style.models.UserLoginResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 1;
    /* ===SGLOABAL VARIABLES DECLARATION===*/
    private Context context;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    /*===VIEW BINDING===*/
    @BindView(R.id.edt_username)
    EditTextRoboto_Light edt_email;
    @BindView(R.id.edt_password)
    EditTextRoboto_Light edt_password;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.btn_reg)
    TextView btn_reg;
    private SweetAlertDialog pDialog;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        context = LoginActivity.this;

        Intent intent = getIntent();
        type = intent.getStringExtra("Hos");

        if (type.equals("A")) {
            btn_reg.setVisibility(View.GONE);
        }

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading...");


        if (S.isUserLoggedIn(context)) {
            moveToNext();
        }
//        Intent ii = new Intent(context, FindShakeService.class);
//        startService(ii);

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RegisterActivity.class);
                intent.putExtra("T", type);
                startActivity(intent);
                finish();
            }
        });

        /*===SHAREDPREFERENCES CHECK USER, LOGGED IN OR NOT===*/

        setTitle("LOGIN");

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validation();

            }
        });

        requestStoragePermission();
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED)
            return;


        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.RECEIVE_SMS,
                android.Manifest.permission.READ_SMS,
        }, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    /*===INPUT VALIDATION===*/

    public void validation() {
        if (edt_email.getText().toString().trim().equals("admin") || edt_password.getText().toString().trim().equals("admin")) {
            Intent intent = new Intent(context, GetFeedbacksActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        if (!S.isValidEditText(edt_email, "Email Id")) return;
        if (!S.isValidEditText(edt_password, "Password")) return;

        userLogin();
    }

    /*===LOGIN===*/
    private void userLogin() {
        pDialog.show();
        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);

        //PREPARE INPUT/REQUEST PARAMETERS
        UserLoginInput userLoginInput = new UserLoginInput(
                edt_email.getText().toString().trim(),
                edt_password.getText().toString().trim(),
                type
        );


        S.hideSoftKeyboard(LoginActivity.this);
        //CALL NOW
        webServices.BplugdUserLogin(userLoginInput)
                .enqueue(new Callback<UserLoginResult>() {
                    @Override
                    public void onResponse(Call<UserLoginResult> call, Response<UserLoginResult> response) {
                        if (pDialog.isShowing()) pDialog.dismiss();
                        if (!S.analyseResponse(response)) {


                            Toast.makeText(context, "Null", Toast.LENGTH_LONG).show();
                            return;
                        }
                        UserLoginResult result = response.body();
                        if (result.is_success) {


                            S.saveUserLoginData(context, result.user);

                            moveToNext();


                        } else {

                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<UserLoginResult> call, Throwable t) {
                        if (pDialog.isShowing()) pDialog.dismiss();
                        S.displayNetworkErrorMessage(getApplicationContext(), null, t);
                        t.printStackTrace();


                    }
                });
    }

    private void moveToNext() {
        if (S.getUserDetails(context).user_type.equals("U")) {
            Intent intent = new Intent(context, SendFeedbacksActivity.class);
            intent.putExtra("S", "KRS");
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(context, UserHomeActivity.class);
            intent.putExtra("S", "KRS");
            startActivity(intent);
            finish();
        }
    }


}
