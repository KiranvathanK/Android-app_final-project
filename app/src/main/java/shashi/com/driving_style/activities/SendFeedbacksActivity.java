package shashi.com.driving_style.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import shashi.com.driving_style.R;
import shashi.com.driving_style.api.Api;
import shashi.com.driving_style.api.WebServices;
import shashi.com.driving_style.logics.S;
import shashi.com.driving_style.models.AddResultInput;
import shashi.com.driving_style.models.UserRegisterResult;

import static shashi.com.driving_style.logics.S.ShowSuccessDialog;

public class SendFeedbacksActivity extends AppCompatActivity {
    EditText t, rc, h;
    SearchableSpinner sp_s, sp_d;
    Button sub, vv;
    private Context context;
    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedbacks);
        context = SendFeedbacksActivity.this;
        setTitle("Post Feedbacks");
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Processing...");
        pDialog.setCancelable(false);




        t = (EditText) findViewById(R.id.t);
        rc = (EditText) findViewById(R.id.rc);
        h = (EditText) findViewById(R.id.h);
        sp_s = (SearchableSpinner) findViewById(R.id.sp_s);
        sp_d = (SearchableSpinner) findViewById(R.id.sp_d);
        sub = (Button) findViewById(R.id.sub);
        vv = (Button) findViewById(R.id.vv);

        if (S.getUserDetails(context).user_type.equals("D")){
            vv.setVisibility(View.GONE);
        }

        vv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GetFeedbacksActivity.class);
                startActivity(intent);
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (t.getText().toString().trim().isEmpty() && rc.getText().toString().trim().isEmpty() && h.getText().toString().trim().isEmpty()) {

                    Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_LONG).show();
                    return;

                }

                if (sp_d.getSelectedItem().toString().equals("Select Destination") && sp_s.getSelectedItem().toString().equals("Select Source")) {
                    Toast.makeText(context, "Please select both source and destination", Toast.LENGTH_LONG).show();
                    return;

                }

                sendFeedback();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            S.userLogout(context);
            Intent intent = new Intent(context, SplashActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendFeedback() {

        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);

        //PREPARE INPUT/REQUEST PARAMETERS
        AddResultInput userLoginInput = new AddResultInput(

                S.getUserDetails(context).user_id,
                S.getUserDetails(context).user_name,
                sp_s.getSelectedItem().toString().trim(),
                sp_d.getSelectedItem().toString().trim(),
                h.getText().toString().trim(),
                t.getText().toString().trim(),
                rc.getText().toString().trim()

        );


        S.hideSoftKeyboard(SendFeedbacksActivity.this);
        //CALL NOW
        pDialog.show();
        webServices.fee(userLoginInput)
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
                            ShowSuccessDialog(context, "Success", result.msg);
                        } else {

                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                        }

                    }


                    @Override
                    public void onFailure(Call<UserRegisterResult> call, Throwable t) {
                        S.displayNetworkErrorMessage(getApplicationContext(), null, t);
                        t.printStackTrace();
                        if (pDialog.isShowing()) pDialog.dismiss();

                    }
                });
    }
}
