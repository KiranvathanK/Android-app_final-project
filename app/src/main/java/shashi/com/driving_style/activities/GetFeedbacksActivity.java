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
import shashi.com.driving_style.adapters.UserLogsAdapter;
import shashi.com.driving_style.api.Api;
import shashi.com.driving_style.api.WebServices;
import shashi.com.driving_style.logics.S;
import shashi.com.driving_style.models.GetLogResult;
import shashi.com.driving_style.models.Logs;

public class GetFeedbacksActivity extends AppCompatActivity {
    private Context context;
    Button resb;
    private SweetAlertDialog pDialog;
    private RecyclerView recyclerView;
    private UserLogsAdapter mDeptAdapter;
    private List<Logs> res = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_feedbacks);
        context = GetFeedbacksActivity.this;
        resb = (Button) findViewById(R.id.res);


        if (S.isUserLoggedIn(context)) {
            resb.setVisibility(View.GONE);
        }


        resb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Results2Activity.class);
                startActivity(intent);
            }
        });

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading...");


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mDeptAdapter = new UserLogsAdapter(context, res);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        //  LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, resId);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mDeptAdapter);

        getLogs();
    }

    private void getLogs() {
        pDialog.show();
        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);


        //CALL NOW
        webServices.getFed()
                .enqueue(new Callback<GetLogResult>() {
                    @Override
                    public void onResponse(Call<GetLogResult> call, Response<GetLogResult> response) {
                        if (pDialog.isShowing()) pDialog.dismiss();
                        if (!S.analyseResponse(response)) {
//

                            Toast.makeText(context, "Null", Toast.LENGTH_LONG).show();
                            return;
                        }
                        GetLogResult result = response.body();
                        if (result.is_success) {
                            res = result.feedbacks;
                            mDeptAdapter = new UserLogsAdapter(context, res);
                            recyclerView.setAdapter(mDeptAdapter);

                        } else {
//
                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<GetLogResult> call, Throwable t) {
                        S.displayNetworkErrorMessage(getApplicationContext(), null, t);
                        if (pDialog.isShowing()) pDialog.dismiss();
                        t.printStackTrace();
//

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
}
