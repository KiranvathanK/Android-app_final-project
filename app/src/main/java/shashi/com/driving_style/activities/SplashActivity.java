package shashi.com.driving_style.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import shashi.com.driving_style.R;
import shashi.com.driving_style.logics.S;

public class SplashActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        context = SplashActivity.this;

       // startService(new Intent(this, GPSTracker.class));

        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(4 * 1000);

                    if (S.isUserLoggedIn(context)) {

                        moveToNext();
                    } else {
                        Intent i = new Intent(SplashActivity.this, PreLoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                } catch (Exception e)

                {

                }
            }
        };
        background.start();

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

