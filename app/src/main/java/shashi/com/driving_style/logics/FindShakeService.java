package shashi.com.driving_style.logics;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import shashi.com.driving_style.activities.UserHomeActivity;

public class FindShakeService extends IntentService implements SensorListener {

    public FindShakeService() {
        super("FindShakeService");
    }

    private static final int FORCE_THRESHOLD = 900;
    private static final int TIME_THRESHOLD = 100;
    private static final int SHAKE_TIMEOUT = 400;
    private static final int SHAKE_DURATION = 1000;
    public static int SHAKE_COUNT = 0;
    public static final int SHAKE_COUNTT = 3;

    private SensorManager mSensorMgr;
    private float mLastX = -1.0f, mLastY = -1.0f, mLastZ = -1.0f;
    private long mLastTime;
    private Context mContext;
    private int mShakeCount = 0;
    private long mLastShake;
    private long mLastForce;

    private static final int SHAKE_THRESHOLD = 800;
    SensorManager sensorMgr;
    private Context context;

    @Override
    protected void onHandleIntent(Intent intent) {
        context = FindShakeService.this;

        sensorMgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorMgr.registerListener(this,
                SensorManager.SENSOR_ACCELEROMETER,
                SensorManager.SENSOR_DELAY_GAME);
        PrimeThread p = new PrimeThread();
        p.start();
    }

    class PrimeThread extends Thread {

        public void run() {
            sensorMgr.registerListener(FindShakeService.this,
                    SensorManager.SENSOR_ACCELEROMETER,
                    SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public void onSensorChanged(int sensor, float[] values) {
        {
            if (sensor != SensorManager.SENSOR_ACCELEROMETER) return;
            long now = System.currentTimeMillis();

            if ((now - mLastForce) > SHAKE_TIMEOUT) {
                mShakeCount = 0;
            }

            if ((now - mLastTime) > TIME_THRESHOLD) {
                long diff = now - mLastTime;
                float speed = Math.abs(values[SensorManager.DATA_X] + values[SensorManager.DATA_Y] + values[SensorManager.DATA_Z] - mLastX - mLastY - mLastZ) / diff * 10000;
                if (speed > FORCE_THRESHOLD) {

                    if ((++mShakeCount >= SHAKE_COUNTT) && (now - mLastShake > SHAKE_DURATION)) {
                        mLastShake = now;
                        mShakeCount = 0;
                        SHAKE_COUNT=SHAKE_COUNT+1;

                        foundShake();
                    }
                    mLastForce = now;
                }
                mLastTime = now;
                mLastX = values[SensorManager.DATA_X];
                mLastY = values[SensorManager.DATA_Y];
                mLastZ = values[SensorManager.DATA_Z];
            }

        }
    }

    @Override
    public void onAccuracyChanged(int sensor, int accuracy) {

    }

    public void foundShake() {
      Toast.makeText(context, "Mobile Falls Down", Toast.LENGTH_LONG).show();
     //   if (S.getUserDetails(context).on_off) {
//            MediaPlayer mp = MediaPlayer.create(this, S.raw.alert);
//            mp.setLooping(true);
//            mp.start();
        if (S.getOn_Off(context)!=null) {
            Toast.makeText(context, "Mobile Falls Down", Toast.LENGTH_LONG).show();
            if (S.getOn_Off(context).equals("YES")) {

                Intent intent = new Intent(this, UserHomeActivity.class);
                intent.putExtra("S","SHAKE");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } else {

            }
        }

        }
    }


