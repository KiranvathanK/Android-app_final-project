package shashi.com.driving_style.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import services.GPSTracker;
import shashi.com.driving_style.R;
import shashi.com.driving_style.logics.FindShakeService;
import shashi.com.driving_style.logics.S;

public class HomeActivity extends Activity implements SensorEventListener, GpsStatus.Listener {

    private SensorManager sensorManager;
    private SharedPreferences sp;
    private Sensor compass;
    private ImageView image;
    private TextView compassAngle, hd, lati, longi, gval;
    private float currentDegree = 0f;
    private Chronometer time;
    private String deg;
    private LocationManager locManager;
    private LocationListener locListener = new myLocationListener();
    static final Double EARTH_RADIUS = 63711.00;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    Sensor proximitySensor, gyroscopeSensor, rotationVectorSensor;
    SensorEventListener proximitySensorListener, gyroscopeSensorListener, rvListener;
    double lat_old = 0.0;
    double lon_old = 0.0;
    double lat_new;
    double lon_new;
    double speed = 0.0;
    private Handler myHandler = new Handler();
    long timeInMillies = 0L;
    long timeSwap = 0L;
    private long startTime, finaltime;
    JSONObject obj;
    String who;
    String l, r;
    int condi = 0;
    private Context context;
    LocationManager locationManager;
    public static double avg_speed = 0.00, min_speed = 100.0, max_speed = 0.00;
    public static String speeds, s_time, e_time, t_time, s_lat, s_lng, d_lng, d_lat, place, c_lat, c_lng;
    TextView speed_textView6, textView3, dlat_textView6, dlongi_textView6;
    double totalDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = HomeActivity.this;

        Intent i = getIntent();

        d_lat = i.getStringExtra("ss_lat");
        d_lng = i.getStringExtra("ss_lng");
        place = i.getStringExtra("place");

        image = (ImageView) findViewById(R.id.imageViewCompass);
        speed_textView6 = (TextView) findViewById(R.id.speed_textView6);
        compassAngle = (TextView) findViewById(R.id.angle);
        textView3 = (TextView) findViewById(R.id.textView3);
        time = (Chronometer) findViewById(R.id.time);
        lati = (TextView) findViewById(R.id.lat_textView6);
        longi = (TextView) findViewById(R.id.longi_textView6);
        gval = (TextView) findViewById(R.id.gval_textView6);
        dlongi_textView6 = (TextView) findViewById(R.id.dlongi_textView6);
        dlat_textView6 = (TextView) findViewById(R.id.dlat_textView6);

        time.setText("00:00:00");
        dlat_textView6.setText("Destination Latitude : " + d_lat);
        dlongi_textView6.setText("Destination Longitude : " + d_lng);
        //dlongi_textView6.setText("00:00:00");


        s_time = S.getTime();

        Log.d("Time", s_time);

        startTime = SystemClock.uptimeMillis();
        myHandler.postDelayed(updateTimerMethod, 0);
        sp = getSharedPreferences("user", MODE_PRIVATE);
        //spp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        compass = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        hd = (TextView) findViewById(R.id.hd_textView2);
        location();

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000,
                10, locationListenerGPS);
        isLocationEnabled();

        GPSTracker gps = new GPSTracker(context);

        if (gps.canGetLocation()) {
            s_lat = Double.toString(gps.getLatitude());
            s_lng = Double.toString(gps.getLongitude());
            c_lat = Double.toString(gps.getLatitude());
            c_lng = Double.toString(gps.getLongitude());

            lati.setText("Latitude : " + s_lat + "");
            longi.setText("Longitude " + s_lng + "");

            //  currentLatLng = new LatLng(gps.getLatitude(), gps.getLongitude());
        } else {

            gps.showSettingsAlert();
            //  finish();
        }

        LatLng latLngA = new LatLng(Double.parseDouble(s_lat), Double.parseDouble(s_lng));
        LatLng latLngB = new LatLng(Double.parseDouble(d_lat), Double.parseDouble(d_lng));

        Location locationA = new Location("point A");
        locationA.setLatitude(latLngA.latitude);
        locationA.setLongitude(latLngA.longitude);
        Location locationB = new Location("point B");
        locationB.setLatitude(latLngB.latitude);
        locationB.setLongitude(latLngB.longitude);

        totalDistance = locationA.distanceTo(locationB);
        Log.d("totalDistances", totalDistance + "");


        RotateAnimation ra = new RotateAnimation(currentDegree, -90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        // how long the animation will take place
        ra.setDuration(2100);
        // set the animation after the end of the reservation status
        ra.setFillAfter(true);
        // Start the animation
        image.startAnimation(ra);
        currentDegree = -55;

        RotateAnimation raa = new RotateAnimation(currentDegree, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        // how long the animation will take place
        raa.setDuration(2100);
        // set the animation after the end of the reservation status
        raa.setFillAfter(true);
        // Start the animation
        image.startAnimation(raa);
        currentDegree = 0;

        image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //LoadUrl l1 = new LoadUrl();
                //l1.execute(url);

                e_time = S.getTime();

                Intent i = new Intent(HomeActivity.this, MainActivity.class);
                i.putExtra("speeds", speeds + "");
                i.putExtra("min_speed", min_speed + "");
                i.putExtra("max_speed", max_speed + "");
                i.putExtra("avg_speed", ((Double.parseDouble(speeds) / max_speed) / 2) + "");
                i.putExtra("e_time", e_time);
                i.putExtra("s_time", s_time);
                i.putExtra("s_lat", s_lat);
                i.putExtra("s_lng", s_lng);
                i.putExtra("d_lat", d_lat);
                i.putExtra("d_lng", d_lng);
                i.putExtra("c_lat", c_lat);
                i.putExtra("c_lng", c_lng);
                i.putExtra("place", place);
                i.putExtra("totalDistance", totalDistance+"");
                i.putExtra("humps", textView3.getText().toString().trim());
                i.putExtra("time", time.getText().toString().trim());
                startActivity(i);

            }
        });
        if (compass != null) {
            sensorManager.registerListener(this, compass, SensorManager.SENSOR_DELAY_UI);
        }
        if (rotationVectorSensor != null) {
            // Register it
            sensorManager.registerListener(rvListener, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }


        // Rotation vector sensor Create a listener
        rvListener = new SensorEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                // More code goes here
                float[] rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(rotationMatrix, sensorEvent.values);

                // Remap coordinate system
                float[] remappedRotationMatrix = new float[16];
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Z,
                        remappedRotationMatrix);

                // Convert to orientations
                float[] orientations = new float[3];
                SensorManager.getOrientation(remappedRotationMatrix, orientations);
                for (int i = 0; i < 3; i++) {
                    orientations[i] = (float) (Math.toDegrees(orientations[i]));
                }
                if (orientations[2] > 45) {
                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                } else if (orientations[2] < -45) {
                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                } else if (Math.abs(orientations[2]) < 10) {
                    getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        textView3.setText(FindShakeService.SHAKE_COUNT + "");
    }

    private Runnable updateTimerMethod = new Runnable() {
        public void run() {
            timeInMillies = SystemClock.uptimeMillis() - startTime;
            finaltime = timeSwap + timeInMillies;

            int seconds = (int) (finaltime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (finaltime % 1000);
            time.setText("" + minutes + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%03d", milliseconds));
            myHandler.postDelayed(this, 0);
        }

    };

    public void location() {

        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        Log.v("Debug", "in on create.. 2");
        if (gps_enabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
            Log.v("Debug", "Enabled..");
        }
        if (network_enabled) {
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
            Log.v("Debug", "Disabled..");
        }
        Log.v("Debug", "in on create..3");
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, compass, SensorManager.SENSOR_DELAY_UI);
        // Register the listener
        sensorManager.registerListener(gyroscopeSensorListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_UI);
        // Register it, specifying the polling interval in
        // microseconds
        sensorManager.registerListener(proximitySensorListener, proximitySensor, 2 * 1000 * 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float degree = Math.round(event.values[0]);
        //deg = Float.toString(degree);
        if (degree > 0 && degree <= 30) {
            deg = "North";
        } else if (degree > 30 && degree <= 70) {
            deg = "NorthEast";
        } else if (degree > 70 && degree <= 115) {
            deg = "East";
        } else if (degree > 115 && degree <= 165) {
            deg = "SouthEast";
        } else if (degree > 165 && degree <= 210) {
            deg = "South";
        } else if (degree > 210 && degree <= 250) {
            deg = "SouthWest";
        } else if (degree > 250 && degree <= 300) {
            deg = "West";
        } else if (degree > 300 && degree <= 359) {
            deg = "West";
        }
        compassAngle.setText("" + deg);
        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(currentDegree, -degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        // how long the animation will take place
        ra.setDuration(2100);
        // set the animation after the end of the reservation status
        ra.setFillAfter(true);
        // Start the animation
        image.startAnimation(ra);
        currentDegree = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onGpsStatusChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    private class myLocationListener implements LocationListener {
        double lat_old = 0.0;
        double lon_old = 0.0;
        double lat_new;
        double lon_new;
        double time = 10;
        double speed = 0.0;

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            Log.v("Debug", "in onLocation changed..");
            if (location != null) {
                locManager.removeUpdates(locListener);
                //String Speed = "Device Speed: " +location.getSpeed();
                lat_new = location.getLongitude();
                lon_new = location.getLatitude();
                speeds = ((location.getSpeed() * 3600) / 1000) + "";
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                double distance = CalculationByDistance(lat_new, lon_new, lat_old, lon_old);
                speed = distance / time;

                // Toast.makeText(getApplicationContext(), latitude + longitude + "\n Speed : " + speeds + "", Toast.LENGTH_LONG).show();


                Toast toast = Toast.makeText(getApplicationContext(),
                        "New Lat " + latitude + " \n" + "New Lng " + longitude + "\n Speed : " + speeds + "", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();


                double dd = Double.parseDouble(speeds);

                speed_textView6.setText("Speed : " + String.format("%.2f", dd) + " Km/h");
                textView3.setText(FindShakeService.SHAKE_COUNT + "");
                speeds = speeds + "";


                lati.setText("Latitude : " + String.format("%.6f", latitude) + "");
                longi.setText("Longitude " + String.format("%.6f", longitude) + "");
                //Z    speed_textView6.setText("Speed "+speedd+"");


                if (((location.getSpeed() * 3600) / 1000) <= min_speed) {
                    min_speed = ((location.getSpeed() * 3600) / 1000);
                }

                if (((location.getSpeed() * 3600) / 1000) >= max_speed) {
                    max_speed = ((location.getSpeed() * 3600) / 1000);
                }


                lat_old = lat_new;
                lon_old = lon_new;
            }


        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        // TODO Auto-generated method stub
        public double CalculationByDistance(double lat1, double lon1, double lat2, double lon2) {
            double Radius = EARTH_RADIUS;
            double dLat = Math.toRadians(lat2 - lat1);
            double dLon = Math.toRadians(lon2 - lon1);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                            Math.sin(dLon / 2) * Math.sin(dLon / 2);
            double c = 2 * Math.asin(Math.sqrt(a));
            return Radius * c;
        }
    }


    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            c_lat = latitude + "";
            c_lng = longitude + "";
            double sss = ((location.getSpeed() * 3600) / 1000);
            String msg = "New Lat: " + latitude + "New Lng: " + longitude + " Speed : " + sss + "";
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
//
//            Toast toast = Toast.makeText(getApplicationContext(),
//                    msg, Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
//            toast.show();

            lati.setText("Latitude : " + String.format("%.6f", latitude) + "");
            longi.setText("Longitude " + String.format("%.6f", longitude) + "");


            speed_textView6.setText("Speed : " + String.format("%.2f", sss) + " Km/h");
            textView3.setText(FindShakeService.SHAKE_COUNT + "");
            speeds = sss + "";
            if (sss <= min_speed) {
                min_speed = sss;
            }

            if (sss >= max_speed) {
                max_speed = sss;
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void isLocationEnabled() {

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Enable Location");
            alertDialog.setMessage("Your locations setting is not enabled. Please enabled it in settings menu.");
            alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = alertDialog.create();
            alert.show();
        } else {
//            AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
//            alertDialog.setTitle("Confirm Location");
//            alertDialog.setMessage("Your Location is enabled, please enjoy");
//            alertDialog.setNegativeButton("Back to interface",new DialogInterface.OnClickListener(){
//                public void onClick(DialogInterface dialog, int which){
//                    dialog.cancel();
//                }
//            });
//            AlertDialog alert=alertDialog.create();
//            alert.show();
        }
    }
}
