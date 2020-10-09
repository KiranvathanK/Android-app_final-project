package shashi.com.driving_style.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

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

public class MainActivity extends AppCompatActivity {
    TextView test, date, st, et, tot, fuel, litres, avg, max, mil, samples, co2, s_lat, s_lng, d_lat, d_lng, textView7, humps, co;
    Button send, del, sensors, maps;
    int Result;
    SimpleDateFormat simpledateformat, simpletimeformat;
    Calendar calander;
    String Date1, time, d_latt, d_lngg, clat, clng, totalDistance;

    private Context context;
    SweetAlertDialog pDialog;

    String as, mxs, plac, slt, sln, dlat, dlng, tt, hump;
    double distanceTravelled;
    double fuelreq, fuelconsumed;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        final Intent intent = getIntent();


        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Processing...");
        pDialog.setCancelable(false);
        s_lat = (TextView) findViewById(R.id.s_lat);
        s_lng = (TextView) findViewById(R.id.s_lng);
        d_lat = (TextView) findViewById(R.id.d_lat);
        d_lng = (TextView) findViewById(R.id.d_lng);
        textView7 = (TextView) findViewById(R.id.textView7);


        test = (TextView) findViewById(R.id.test_textView1);
        co = (TextView) findViewById(R.id.co);
        humps = (TextView) findViewById(R.id.humps);
        date = (TextView) findViewById(R.id.date_textView2);
        st = (TextView) findViewById(R.id.start_textView4);
        et = (TextView) findViewById(R.id.end_textView6);
        tot = (TextView) findViewById(R.id.total_textView8);
        fuel = (TextView) findViewById(R.id.fueleco_textView10);
        litres = (TextView) findViewById(R.id.liters_textView12);
        avg = (TextView) findViewById(R.id.avg_textView14);
        max = (TextView) findViewById(R.id.max_textView16);
        mil = (TextView) findViewById(R.id.mileage_textView18);
        samples = (TextView) findViewById(R.id.samples_textView20);


        co2 = (TextView) findViewById(R.id.co2__textView22);
        send = (Button) findViewById(R.id.send_button1);
        del = (Button) findViewById(R.id.del_button2);
        sensors = (Button) findViewById(R.id.sens_button3);
        maps = (Button) findViewById(R.id.maps_button4);
        //time and date formats
        calander = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd-MM-yyyy");
        simpletimeformat = new SimpleDateFormat("HH:mm:ss");
        Date1 = simpledateformat.format(calander.getTime());
        time = simpletimeformat.format(calander.getTime());
        date.setText(": " + S.getDate());
        et.setText(time);
        //random number generation

//        i.putExtra("speeds", speeds);
//        i.putExtra("min_speed", min_speed);
//        i.putExtra("max_speed", max_speed);
//        i.putExtra("avg_speed", (min_speed / max_speed) / 2);
//        i.putExtra("e_time", S.getTime());
//        i.putExtra("s_time", s_time);
//        i.putExtra("s_lat", s_lat);
//        i.putExtra("s_lng", s_lng);

        st.setText(" : " + intent.getStringExtra("s_time"));
        et.setText(" : " + intent.getStringExtra("e_time"));

        avg.setText(" : " + String.format("%.2f", Double.parseDouble(intent.getStringExtra("avg_speed"))) + " Km/h");
        as = intent.getStringExtra("avg_speed");

        max.setText(" : " + String.format("%.2f", Double.parseDouble(intent.getStringExtra("max_speed"))) + " Km/h");
        mxs = intent.getStringExtra("max_speed");
        double msp = Double.parseDouble(intent.getStringExtra("max_speed"));
        //   fuel.setText(" : " + String.format("%.2f", (msp * 25 / 100)) + " " + " Ltr/Km");

        samples.setText(" : " + String.format("%.2f", (msp * 25 / 100) + 1) + " " + " Km/hr");

        mil.setText(" : " + String.format("%.2f", Double.parseDouble(intent.getStringExtra("min_speed"))) + " Km/h");
        s_lat.setText(" : " + intent.getStringExtra("s_lat"));
        slt = intent.getStringExtra("s_lat");

        s_lng.setText(" : " + intent.getStringExtra("s_lng"));
        sln = intent.getStringExtra("s_lng");

        clat = intent.getStringExtra("c_lat");
        clng = intent.getStringExtra("c_lng");
        totalDistance = intent.getStringExtra("totalDistance");
        Log.d("totalDistancee", totalDistance + "");

        d_latt = intent.getStringExtra("d_lat");
        d_lngg = intent.getStringExtra("d_lng");
        plac = intent.getStringExtra("place");

        d_lat.setText(" : " + intent.getStringExtra("d_lat"));
        d_lng.setText(" : " + intent.getStringExtra("d_lng"));
        textView7.setText(" : " + intent.getStringExtra("time"));
        tt = intent.getStringExtra("time");

        humps.setText(" : " + intent.getStringExtra("humps"));
        hump = intent.getStringExtra("humps");


        Random r1 = new Random();
        int Low = 10;
        int High = 100;
        Result = r1.nextInt(High - Low) + Low;
        final Handler h = new Handler();
        final Runnable r = new Runnable() {

            @Override
            public void run() {
                Random r1 = new Random();
                int Low = 10;
                int High = 100;
                Result = r1.nextInt(High - Low) + Low;
                final Handler h = new Handler();
                test.setText("" + Result);
                h.postDelayed(this, 3000); //ms
            }
        };
        h.postDelayed(r, 1000); // one second in ms

        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                i.putExtra("d_lat", d_latt);
                i.putExtra("d_lng", d_lngg);
                startActivity(i);
            }
        });
        sensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, PiChartActivity.class);
                i.putExtra("AVGS", intent.getStringExtra("avg_speed"));
                i.putExtra("MXS", intent.getStringExtra("max_speed"));
                i.putExtra("MNS", intent.getStringExtra("min_speed"));
                i.putExtra("HMPS", intent.getStringExtra("humps"));
                startActivity(i);
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SplashActivity.class);
                intent.putExtra("S", "KRS");
                startActivity(intent);
                finish();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addResults();
            }
        });


        LatLng latLngA = new LatLng(Double.parseDouble(slt), Double.parseDouble(sln));
        LatLng latLngB = new LatLng(Double.parseDouble(clat), Double.parseDouble(clng));

        Location locationA = new Location("point A");
        locationA.setLatitude(latLngA.latitude);
        locationA.setLongitude(latLngA.longitude);
        Location locationB = new Location("point B");
        locationB.setLatitude(latLngB.latitude);
        locationB.setLongitude(latLngB.longitude);


        fuelreq = Double.parseDouble(totalDistance) * 0.066;
        Log.d("fuelreq", fuelreq + "");

        distanceTravelled = locationA.distanceTo(locationB) / 1000;

        Log.d("fueldist", distanceTravelled + "");

        fuelconsumed = distanceTravelled * 0.066;
        Log.d("fuelconsumed", fuelconsumed + "");

        fuel.setText(" : " + String.format("%.5f", fuelconsumed) + " " + " Ltr/Km");
        co.setText(" : " + String.format("%.4f", fuelconsumed * 2.14) + " " + " Kg/Ltr");
    }

    private void addResults() {

        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);

        //PREPARE INPUT/REQUEST PARAMETERS
        AddResultInput userLoginInput = new AddResultInput(


                S.getUserDetails(context).user_id,
                plac,
                slt,
                sln,
                d_latt,
                d_lngg,
                hump,
                S.getTime(),
                as,
                mxs,
                S.getDateYYMMDD()

        );


        S.hideSoftKeyboard(MainActivity.this);
        //CALL NOW
        pDialog.show();
        webServices.addres(userLoginInput)
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
