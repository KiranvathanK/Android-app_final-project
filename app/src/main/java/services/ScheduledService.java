package services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

public class ScheduledService extends IntentService {
    boolean shashi = true;
    static int previous, current;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private Context context;

    protected LocationManager locationManager;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1   ; //10*1 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 1; // 1 second

    // Must create a default constructor
    public ScheduledService() {
        super("scheduled-service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // This describes what will happen when service is triggered
        context = ScheduledService.this;

         //   delayTime();

    }
    }

