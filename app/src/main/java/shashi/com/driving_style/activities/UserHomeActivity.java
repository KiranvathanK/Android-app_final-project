package shashi.com.driving_style.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import services.GPSTracker;
import shashi.com.driving_style.R;
import shashi.com.driving_style.api.Api;
import shashi.com.driving_style.api.WebServices;
import shashi.com.driving_style.logics.S;
import shashi.com.driving_style.models.AddResultInput;
import shashi.com.driving_style.models.UserRegisterResult;

import static shashi.com.driving_style.logics.FindShakeService.SHAKE_COUNT;

public class UserHomeActivity extends AppCompatActivity {
    private Context context;
    @BindView(R.id.bues)
    SearchableSpinner sp_bues;


    @BindView(R.id.subb)
    Button subb;

    private SweetAlertDialog pDialog;

    public String lat_str, lng_str;

    String place, ss_lat, ss_lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        ButterKnife.bind(this);
        context = UserHomeActivity.this;

        Intent ii = new Intent(context, shashi.com.driving_style.logics.FindShakeService.class);
        startService(ii);


        GPSTracker gps = new GPSTracker(context);
        if (gps.canGetLocation()) {
            String latitude = Double.toString(gps.getLatitude());
            String longitude = Double.toString(gps.getLongitude());

            lat_str = latitude + "";
            lng_str = longitude + "";

            Toast.makeText(context, "Lat==>" + latitude + " Lng==>" + longitude + "", Toast.LENGTH_LONG).show();

        } else {
            gps.showSettingsAlert();
            Toast.makeText(context, "Please Enable GPS....", Toast.LENGTH_LONG).show();
            finish();

        }

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading...");


        subb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sp_bues.getSelectedItem().toString().equals("Select Destination")) {
                    Toast.makeText(context, "Please Select Destination", Toast.LENGTH_LONG).show();
                    return;
                }
                place = sp_bues.getSelectedItem().toString();

                if (place.equals("Bagalkot")) {
                    ss_lat = "16.181700";
                    ss_lng = "75.695801";
                }
                if (place.equals("Ballari")) {
                    ss_lat = "15.139393";
                    ss_lng = "76.921440";
                }
                if (place.equals("Belagavi")) {
                    ss_lat = "15.8497";
                    ss_lng = "74.4977";
                }
                if (place.equals("Bengaluru Rural")) {
                    ss_lat = "13.2847";
                    ss_lng = "77.6078";
                }
                if (place.equals("Bengaluru Urban")) {
                    ss_lat = "12.9700";
                    ss_lng = "77.6536";
                }
                if (place.equals("Bidar")) {
                    ss_lat = "17.9149";
                    ss_lng = "77.5046";
                }
                if (place.equals("Chamarajanagar")) {
                    ss_lat = "12.0526";
                    ss_lng = "77.2865";
                }
                if (place.equals("Chikballapur ")) {
                    ss_lat = "13.5229";
                    ss_lng = "77.83675";
                }
                if (place.equals("Chikkamagaluru ")) {
                    ss_lat = "13.3153";
                    ss_lng = "75.7754";
                }
                if (place.equals("Chitradurga")) {
                    ss_lat = "14.1823";
                    ss_lng = "76.5488";
                }
                if (place.equals("Dakshina Kannada")) {
                    ss_lat = "12.8438";
                    ss_lng = "75.2479";
                }
                if (place.equals("Davanagere")) {
                    ss_lat = "14.4644";
                    ss_lng = "75.9218";
                }
                if (place.equals("Dharwad")) {
                    ss_lat = "15.4589";
                    ss_lng = "75.0078";
                }
                if (place.equals("Gadag")) {
                    ss_lat = "15.4026";
                    ss_lng = "75.6208";
                }
                if (place.equals("Hassan")) {
                    ss_lat = "13.0068";
                    ss_lng = "76.0996";
                }
                if (place.equals("Haveri")) {
                    ss_lat = "14.6610";
                    ss_lng = "75.4345";
                }
                if (place.equals("Kalaburagi (Gulbarga)")) {
                    ss_lat = "17.3297";
                    ss_lng = "76.8343";
                }
                if (place.equals("Kodagu ")) {
                    ss_lat = "12.3375";
                    ss_lng = "75.8069";
                }
                if (place.equals("Kolar")) {
                    ss_lat = "13.1770";
                    ss_lng = "78.2020";
                }
                if (place.equals("Koppal")) {
                    ss_lat = "15.6219";
                    ss_lng = "76.1784";
                }
                if (place.equals("Mandya")) {
                    ss_lat = "12.5644";
                    ss_lng = "76.7337";
                }
                if (place.equals("Mysuru")) {
                    ss_lat = "12.2958";
                    ss_lng = "76.6394";
                }
                if (place.equals("Raichur")) {
                    ss_lat = "16.2120";
                    ss_lng = "77.3439";
                }
                if (place.equals("Ramanagara")) {
                    ss_lat = "12.7094";
                    ss_lng = "77.2807";
                }
                if (place.equals("Shivamogga")) {
                    ss_lat = "13.9299";
                    ss_lng = "75.5681";
                }
                if (place.equals("Tumakuru")) {
                    ss_lat = "13.3392";
                    ss_lng = "77.1140";
                }
                if (place.equals("Udupi")) {
                    ss_lat = "13.3409";
                    ss_lng = "74.7421";
                }
                if (place.equals("Vijayapura")) {
                    ss_lat = "16.8302";
                    ss_lng = "75.7100";
                }
                if (place.equals("Yadgiri")) {
                    ss_lat = "16.7602";
                    ss_lng = "77.1428";
                }
                Intent intent = new Intent(context, HomeActivity.class);
                intent.putExtra("place", place);
                intent.putExtra("ss_lat", ss_lat);
                intent.putExtra("ss_lng", ss_lng);
                startActivity(intent);
                SHAKE_COUNT = 0;
               // getTimeTaken();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item5:
                S.userLogout(context);
//                int pid = android.os.Process.myPid();
////                android.os.Process.killProcess(pid);

                Intent intent=new Intent(context,SplashActivity.class);
                startActivity(intent);
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    private void getTimeTaken() {
//
//        Retrofit retrofit = Api.getRetrofitBuilder(this);
//        WebServices webServices = retrofit.create(WebServices.class);
//
//        //PREPARE INPUT/REQUEST PARAMETERS
//        AddResultInput userLoginInput = new AddResultInput(
//
//
//                place
//
//        );
//
//
//        S.hideSoftKeyboard(UserHomeActivity.this);
//        //CALL NOW
//        pDialog.show();
//        webServices.timetaken(userLoginInput)
//                .enqueue(new Callback<UserRegisterResult>() {
//                    @Override
//                    public void onResponse(Call<UserRegisterResult> call, Response<UserRegisterResult> response) {
//                        if (pDialog.isShowing()) pDialog.dismiss();
//                        if (!S.analyseResponse(response)) {
//
//                            Toast.makeText(context, "Null", Toast.LENGTH_LONG).show();
//                            return;
//                        }
//                        UserRegisterResult result = response.body();
//                        if (result.is_success) {
//                            //ShowSuccessDialog(context, "Success", result.msg);
//
//                            dilogS(result.place, result.time_taken, result.humbs_count, result.avg_speed);
//
//                        } else {
//
//                            Intent intent = new Intent(context, HomeActivity.class);
//                            intent.putExtra("ss_lat", ss_lat);
//                            intent.putExtra("ss_lng", ss_lng);
//                            intent.putExtra("place", place);
//                            startActivity(intent);
//
//                            //Toast.makeText(context, "", Toast.LENGTH_LONG).show();
//                        }
//
//                    }
//
//
//                    @Override
//                    public void onFailure(Call<UserRegisterResult> call, Throwable t) {
//                        S.displayNetworkErrorMessage(getApplicationContext(), null, t);
//                        t.printStackTrace();
//                        if (pDialog.isShowing()) pDialog.dismiss();
//
//                    }
//                });
//    }

    public void dilogS(String onee, String twoo, String threee, String fourr) {
        final android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.sales_invoice_dec, null);
        dialogBuilder.setView(dialogView);


        final TextView txt_one = (TextView) dialogView.findViewById(R.id.txt_one);
        txt_one.setText("Place Name");
        final TextView one = (TextView) dialogView.findViewById(R.id.one);
        one.setText(": " + onee);
        final TextView txt_two = (TextView) dialogView.findViewById(R.id.txt_two);
        txt_two.setText("Time Taken");
        final TextView two = (TextView) dialogView.findViewById(R.id.two);
        two.setText(": " + twoo+" Minutes");
        final TextView txt_three = (TextView) dialogView.findViewById(R.id.txt_three);
        txt_three.setText("Humps Count");
        final TextView three = (TextView) dialogView.findViewById(R.id.three);
        three.setText(": " + threee);
        final TextView txt_four = (TextView) dialogView.findViewById(R.id.txt_four);
        txt_four.setText("Avg Speed ");
        final TextView four = (TextView) dialogView.findViewById(R.id.four);
        four.setText(": " + fourr+"Km/h");


        final Button btn_create = (Button) dialogView.findViewById(R.id.btn_create);
        final Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);

        final AlertDialog b = dialogBuilder.create();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.cancel();
            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeActivity.class);
                intent.putExtra("ss_lat", ss_lat);
                intent.putExtra("ss_lng", ss_lng);
                intent.putExtra("place", place);
                startActivity(intent);
                b.cancel();
                // class_duration = edt_group_name.getText().toString().trim();
                //uploadAttandance();
            }
        });

        b.show();

    }

}
