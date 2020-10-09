package shashi.com.driving_style.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import shashi.com.driving_style.R;
import shashi.com.driving_style.adapters.RidesResultsAdapter;
import shashi.com.driving_style.api.Api;
import shashi.com.driving_style.api.WebServices;
import shashi.com.driving_style.logics.S;
import shashi.com.driving_style.models.AddResultInput;
import shashi.com.driving_style.models.GetLogResult;
import shashi.com.driving_style.models.UserRegisterResult;
import shashi.com.smart_bus_tracking.models.Results;


public class ResultsActivity extends AppCompatActivity {
    private Context context;

    private SweetAlertDialog pDialog;
    private RecyclerView recyclerView;
    private RidesResultsAdapter mDeptAdapter;
    private List<Results> res = new ArrayList<>();
    String place, ss_lat, ss_lng;
    Button drive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        context = ResultsActivity.this;

        setTitle("Previous Rides");

        drive = (Button) findViewById(R.id.drive);


        Intent intent = getIntent();
        place = intent.getStringExtra("place");
        ss_lat = intent.getStringExtra("ss_lat");
        ss_lng = intent.getStringExtra("ss_lng");


        drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HomeActivity.class);
                intent.putExtra("ss_lat", ss_lat);
                intent.putExtra("ss_lng", ss_lng);
                intent.putExtra("place", place);
                startActivity(intent);
            }
        });


        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading...");


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mDeptAdapter = new RidesResultsAdapter(context, res);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        //  LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, resId);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mDeptAdapter);

        //getLogs();

        getTimeTaken();


    }


//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menus, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            S.userLogout(context);
//            Intent intent = new Intent(context, SplashActivity.class);
//            startActivity(intent);
//            finish();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void getLogs() {

        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);


        //CALL NOW
        webServices.getLogs()
                .enqueue(new Callback<GetLogResult>() {
                    @Override
                    public void onResponse(Call<GetLogResult> call, Response<GetLogResult> response) {
                        if (!S.analyseResponse(response)) {
//

                            Toast.makeText(context, "Null", Toast.LENGTH_LONG).show();
                            return;
                        }
                        GetLogResult result = response.body();
                        if (result.is_success) {
                            res = result.place;
                            mDeptAdapter = new RidesResultsAdapter(context, res);
                            recyclerView.setAdapter(mDeptAdapter);

                        } else {
//
                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<GetLogResult> call, Throwable t) {
                        S.displayNetworkErrorMessage(getApplicationContext(), null, t);
                        t.printStackTrace();
//

                    }
                });
    }

    private void getTimeTaken() {

        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);

        //PREPARE INPUT/REQUEST PARAMETERS
        AddResultInput userLoginInput = new AddResultInput(


                place

        );


        S.hideSoftKeyboard(ResultsActivity.this);
        //CALL NOW
        pDialog.show();
        webServices.timetaken(userLoginInput)
                .enqueue(new Callback<GetLogResult>() {
                    @Override
                    public void onResponse(Call<GetLogResult> call, Response<GetLogResult> response) {
                        if (pDialog.isShowing()) pDialog.dismiss();
                        if (!S.analyseResponse(response)) {

                            Toast.makeText(context, "Null", Toast.LENGTH_LONG).show();
                            return;
                        }
                        GetLogResult result = response.body();
                        if (result.is_success) {
                            //ShowSuccessDialog(context, "Success", result.msg);

                            //  dilogS(result.place, result.time_taken, result.humbs_count, result.avg_speed);
                            res = result.place;
                            mDeptAdapter = new RidesResultsAdapter(context, res);
                            recyclerView.setAdapter(mDeptAdapter);

                        } else {

                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("ss_lat", ss_lat);
                            intent.putExtra("ss_lng", ss_lng);
                            intent.putExtra("place", place);
                            startActivity(intent);

                            //Toast.makeText(context, "", Toast.LENGTH_LONG).show();
                        }

                    }


                    @Override
                    public void onFailure(Call<GetLogResult> call, Throwable t) {
                        S.displayNetworkErrorMessage(getApplicationContext(), null, t);
                        t.printStackTrace();
                        if (pDialog.isShowing()) pDialog.dismiss();

                    }
                });
    }

}

