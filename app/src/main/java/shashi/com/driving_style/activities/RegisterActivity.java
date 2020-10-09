package shashi.com.driving_style.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import shashi.com.driving_style.R;
import shashi.com.driving_style.api.Api;
import shashi.com.driving_style.api.WebServices;
import shashi.com.driving_style.custom_views.EditTextRoboto_Light;
import shashi.com.driving_style.logics.S;
import shashi.com.driving_style.models.UserRegisterInput;
import shashi.com.driving_style.models.UserRegisterResult;

public class RegisterActivity extends AppCompatActivity {


    @BindView(R.id.edt_fpass)
    EditTextRoboto_Light edt_fpass;


    @BindView(R.id.addr)
    EditTextRoboto_Light addr;


    @BindView(R.id.name)
    EditTextRoboto_Light name;
    @BindView(R.id.id)
    EditTextRoboto_Light id;


    @BindView(R.id.btn_submit)
    Button btn_submit;

    @BindView(R.id.ph)
    EditTextRoboto_Light ph;

    private Context context;
    SweetAlertDialog pDialog;
    String type;

    @SuppressLint("ClickableViewAccessibility")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = RegisterActivity.this;
        ButterKnife.bind(this);

        Intent intent = getIntent();
        type = intent.getStringExtra("T");

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Processing...");
        pDialog.setCancelable(false);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
    }

    public void validation() {


        if (!S.isValidEditText(name, "Name")) return;
        if (!S.isValidEditText(edt_fpass, "Password")) return;

        if (ph.getText().toString().trim().length() < 10) {
            Toast.makeText(context, "Enter 10 digit mobile number", Toast.LENGTH_LONG).show();
            return;
        }

        userRegister();
    }


    private void userRegister() {

        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);

        //PREPARE INPUT/REQUEST PARAMETERS
        UserRegisterInput userLoginInput = new UserRegisterInput(

                name.getText().toString().trim(),
                id.getText().toString().trim(), edt_fpass.getText().toString().trim(),
                ph.getText().toString().trim(),
                addr.getText().toString().trim(),
                type

        );


        S.hideSoftKeyboard(RegisterActivity.this);
        //CALL NOW
        pDialog.show();
        webServices.UserRegister(userLoginInput)
                .enqueue(new Callback<UserRegisterResult>() {
                    @Override
                    public void onResponse(Call<UserRegisterResult> call, Response<UserRegisterResult> response) {
                        if (pDialog.isShowing()) pDialog.dismiss();
                        if (!S.analyseResponse(response)) {

                            Toast.makeText(context, "Null", Toast.LENGTH_LONG).show();
                            return;
                        }
                        UserRegisterResult result = response.body();
                        if (result.is_success) {

                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.putExtra("S", "KRS");
                            intent.putExtra("Hos",type);

                            startActivity(intent);
                            finish();

                        } else {

                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                        }

                    }


                    @Override
                    public void onFailure(Call<UserRegisterResult> call, Throwable t) {
                        S.displayNetworkErrorMessage(getApplicationContext(), null, t);
                        t.printStackTrace();
                        if (pDialog.isShowing()) pDialog.dismiss();
                        btn_submit.setEnabled(true);

                    }
                });
    }
}
