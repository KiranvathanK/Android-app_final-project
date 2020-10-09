package shashi.com.driving_style.logics;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.io.ByteArrayOutputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import shashi.com.driving_style.R;
import shashi.com.driving_style.interfaces.OnDateSetInterface;
import shashi.com.driving_style.models.User;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class S {


    public static String URL = "http://academic2018.pmapindia.com/driving_style/index.php/";
    public static String IMAGE_URL = "http://academic2018.pmapindia.com/driving_style/Images/";

    public static final int CAMERA = 2;
    public static final int GALLERY = 3;
    public static final int IMAGE_SIZE = 1000;

    public static void LogD(String msg) {
        Log.d("Shashi", "-----------------------------------------------------------------");
        Log.d("Shashi", msg);
        Log.d("Shashi", "-----------------------------------------------------------------");
    }

    public static void LogE(String msg) {
        Log.d("Shashi", "-----------------------------------------------------------------");
        Log.d("Shashi", msg);
        Log.d("Shashi", "-----------------------------------------------------------------");
    }

    public static boolean analyseResponse(Response<?> response) {
        if (response == null) {
            LogE(" <<<<<< Response: NULL >>>>>>");
            return false;
        }

        if (response.body() == null) {
            //Toast.makeText(MainApplication.getContext(), response.message(), Toast.LENGTH_SHORT).show();
            LogE(" <<<<<< Response: BODY NULL >>>>>>");
            return false;
        }

        return true;
    }

    public static void displayNetworkErrorMessage(Context context, View view, Throwable t) {

        if (view == null) {

            if (t instanceof ConnectException)
                Toast.makeText(context, "Connection Cannot Be Establish To The Server.", Toast.LENGTH_SHORT).show();
            else if (t instanceof SocketTimeoutException)
                Toast.makeText(context, "Server Timed Out", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
        } else {

            if (t instanceof ConnectException)
                Toast.makeText(context, "Connection Cannot Be Establish To The Server.", Toast.LENGTH_LONG).show();

            else if (t instanceof SocketTimeoutException)
                Toast.makeText(context, "Server Timed Out", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();

        }


    }

    //TO SEND SMS
    public static void sendSMS(String number, String msg) {
        LogD("number : " + number + " msg : " + msg);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, msg, null, null);
    }


    //Convert byte array to bitmap
    public static Bitmap getBitmapFromByteArray(byte[] byteArray) {
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bmp;
    }

    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] byte_arr = stream.toByteArray();
        return Base64.encodeBytes(byte_arr);
    }

    //Convert bitmap to byte array
    public static byte[] getByteArrayFromBitmap(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    //RESIZE THEE IMAGE
    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static String getDate() {
        String str = "";
        Calendar c = Calendar.getInstance();
        str = prefixZeroIfSingleDigit(c.get(Calendar.DATE)) + "/" + prefixZeroIfSingleDigit((c.get(Calendar.MONTH) + 1)) + "/" + c.get(Calendar.YEAR);
        return str;
    }

    public static String getTime() {
        String str = "";
        Calendar c = Calendar.getInstance();
        str = prefixZeroIfSingleDigit(c.get(Calendar.HOUR_OF_DAY)) + ":" + prefixZeroIfSingleDigit((c.get(Calendar.MINUTE)));
        return str;
    }

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("hh:mm a");
        String strDate = mdformat.format(calendar.getTime());
        return strDate;
    }

    public static String convertToCustomDateFormat(int y, int monthMinusOneValue, int d) {
        return prefixZeroIfSingleDigit(y) + "-" + prefixZeroIfSingleDigit(monthMinusOneValue + 1) + "-" + prefixZeroIfSingleDigit(d);
    }

    public static String prefixZeroIfSingleDigit(int intVal) {
        Integer i = new Integer(intVal);
        String val = i.toString();
        return val.length() == 1 ? "0" + val : val;
    }

    //SET SPINNER ADAPTER
    public static void setSpinnerAdapter(Context context, Spinner sp, List<String> list) {
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context, R.layout.ao_simple_spinner_item, list);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter1);
    }

    //SET SPINNER ADAPTER
    public static void setSpinnerAdapter(Context context, Spinner sp, String[] list) {
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context, R.layout.ao_simple_spinner_item, list);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter1);
    }

    public static void ShowSuccessDialog(final Context context, final String tittle, final String msg) {
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(tittle);
        pDialog.setContentText(msg);
        pDialog.setCancelable(false);
        pDialog.show();
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if (sweetAlertDialog.isShowing()) sweetAlertDialog.dismiss();
                ((AppCompatActivity) context).finish();
            }
        });
    }

    public static void ShowSuccessDialogAndBeHere(final Context context, final String tittle, final String msg) {
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(tittle);
        pDialog.setContentText(msg);
        pDialog.setCancelable(false);
        pDialog.show();
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if (sweetAlertDialog.isShowing()) sweetAlertDialog.dismiss();

            }
        });
    }

    public static void ShowErrorDialogAndExit(final Context context, final String tittle, final String msg) {
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        pDialog.setTitleText(tittle);
        pDialog.setContentText(msg);
        pDialog.setCancelable(false);
        pDialog.show();
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if (sweetAlertDialog.isShowing()) sweetAlertDialog.dismiss();
                ((AppCompatActivity) context).finish();
            }
        });
    }

    public static void ShowErrorDialogAndBeHere(final Context context, final String tittle, final String msg) {
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        pDialog.setTitleText(tittle);
        pDialog.setContentText(msg);
        pDialog.setCancelable(false);
        pDialog.show();
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if (sweetAlertDialog.isShowing()) sweetAlertDialog.dismiss();
            }
        });
    }

    public static boolean isValidEditText(EditText editText, String errorMessage) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError("Please Enter " + errorMessage);
            return false;
        }
        return true;
    }


    public static boolean isValidMobileNumber(EditText editText, String errorMessage) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError("Please Enter Mobile Number");
            return false;
        }
        if (editText.getText().toString().trim().length() != 10) {
            editText.setError("Please Enter Valid Mobile Number");
            return false;
        }
        return true;
    }

    public static boolean isValidGSTN(EditText editText, String errorMessage) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError("Please Enter GSTN");
            return false;
        }
        if (editText.getText().toString().trim().length() != 15) {
            editText.setError("Please Enter Valid GSTN");
            return false;
        }
        return true;
    }

    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                return "";
            }
            return telephonyManager.getDeviceId() + "";
        }
        return "";
    }

    public static Bitmap StringToBitmap(String input) {
        Bitmap bitmap = null;
        try {
            byte[] decodedByte = Base64.decode(input);

            bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        } catch (Exception e) {

        }
        return bitmap;
    }

    public static void setDateDefault(Context context, final EditText txt_date, int day, boolean canSetMax, boolean canSetMin) {
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, day);
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        if (txt_date.getText().toString().trim().length() > 0) {
            String a[] = txt_date.getText().toString().trim().split("-");
            mYear = Integer.parseInt(a[0]);
            mMonth = Integer.parseInt(a[1]) - 1;
            mDay = Integer.parseInt(a[2]);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        //c.set(year, month, day);
                        S.LogD(year + " " + month + " " + day);
                        String fromDate = S.convertToCustomDateFormat(year, month, day);
                        txt_date.setText(fromDate);
                        txt_date.setError(null);


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.updateDate(mYear, mMonth, mDay);
        if (canSetMax) {
            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        } else if (canSetMin) {
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        }


        datePickerDialog.show();

    }

    public static void setDateDefault(Context context, final TextView txt_date, int day, int maxDays, boolean canSetMax, boolean canSetMin) {
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, day);
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        if (txt_date.getText().toString().trim().length() > 0) {
            String a[] = txt_date.getText().toString().trim().split("-");
            mYear = Integer.parseInt(a[0]);
            mMonth = Integer.parseInt(a[1]) - 1;
            mDay = Integer.parseInt(a[2]);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        //c.set(year, month, day);
                        S.LogD(year + " " + month + " " + day);
                        String fromDate = S.convertToCustomDateFormat(year, month, day);
                        txt_date.setText(fromDate);
                        txt_date.setError(null);


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.updateDate(mYear, mMonth, mDay);
        final Calendar cMax = Calendar.getInstance();
        cMax.add(Calendar.DATE, maxDays);
        if (canSetMax) {
            Toast.makeText(context, "maxDays", Toast.LENGTH_LONG).show();
            datePickerDialog.getDatePicker().setMaxDate(cMax.getTimeInMillis());
        }
        if (canSetMin) {
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        }

        datePickerDialog.show();

    }

    public static String getDateYYMMDD() {
        String str = "";
        Calendar c = Calendar.getInstance();
        str = prefixZeroIfSingleDigit(c.get(Calendar.YEAR)) + "-" + prefixZeroIfSingleDigit((c.get(Calendar.MONTH) + 1)) + "-" + prefixZeroIfSingleDigit((c.get(Calendar.DATE)));
        return str;
    }


    public static void setDateDefault(Context context, final String tittle, final TextView txt_date1, final TextView txt_date2, int day, boolean canSetMax, boolean canSetMin, final OnDateSetInterface onDateSetInterface) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        if (txt_date1.getText().toString().trim().length() > 0) {
            String a[] = txt_date1.getText().toString().trim().split("-");
            mYear = Integer.parseInt(a[0]);
            mMonth = Integer.parseInt(a[1]) - 1;
            mDay = Integer.parseInt(a[2]);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        //c.set(year, month, day);
                        S.LogD(year + " " + month + " " + day);
                        String date = S.convertToCustomDateFormat(year, month, day);
                        txt_date1.setText(date);
                        txt_date1.setError(null);
                        onDateSetInterface.OnDateSet(date);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.updateDate(mYear, mMonth, mDay);
        try {
            if (canSetMax && (!canSetMin)) {
                if (txt_date2.getText().toString().trim().length() > 0) {
                    datePickerDialog.getDatePicker().setMaxDate(getInstance(txt_date2).getTimeInMillis());
                }

            }
            if (canSetMin) {
                if (txt_date2.getText().toString().trim().length() > 0) {
                    datePickerDialog.getDatePicker().setMinDate(getInstance(txt_date2).getTimeInMillis() - 2000);
                }
            }
            if (canSetMax && canSetMin) {
                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        S.LogD("DATE FOR UPDATE " + mYear + "-" + mMonth + "-" + mDay);

        datePickerDialog.setTitle(tittle);
        datePickerDialog.show();

    }

    public static Calendar getInstance(TextView txt_date1) {
        final Calendar c = Calendar.getInstance();
        if (txt_date1.getText().toString().trim().length() > 0) {
            String a[] = txt_date1.getText().toString().trim().split("-");
            c.set(Calendar.YEAR, Integer.parseInt(a[0]));
            c.set(Calendar.MONTH, Integer.parseInt(a[1]) - 1);
            c.set(Calendar.DATE, Integer.parseInt(a[2]));
        }
        return c;
    }


    public static String prefixZeroIfSingleDigit(String val) {
        return val.length() == 1 ? "0" + val : val;
    }

    public static boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9_-]+\\.+[a-zA-Z0-9_-]+");
    }

    public static int DATE_POS = 2;
    public static int MONTH_POS = 1;
    public static int YEAR_POS = 0;

    public static String[] getDMY(String str) {
        S.LogD("DATE : " + str);
        String[] dmy = {"", "", ""};
        if (str == null) {
            return dmy;
        }
        if (str.trim().isEmpty()) {
            return dmy;
        }
       /* if(str.trim().length()<11){
            return dmy;
        }*/
        try {
            if (str.contains("-")) {
                dmy[0] = S.prefixZeroIfSingleDigit(str.split("-")[S.DATE_POS]);
                dmy[1] = S.getMonthName(Integer.parseInt(str.split("-")[S.MONTH_POS])).substring(0, 3).toUpperCase();
                dmy[2] = str.split("-")[S.YEAR_POS].substring(0, 4);
                return dmy;
            } else if (str.contains("/")) {
                dmy[0] = S.prefixZeroIfSingleDigit(str.split("/")[S.DATE_POS]);
                dmy[1] = S.getMonthName(Integer.parseInt(str.split("/")[S.MONTH_POS])).substring(0, 3).toUpperCase();
                dmy[2] = str.split("/")[S.YEAR_POS].substring(0, 4);
                return dmy;
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return dmy;
        }
        return dmy;

    }

    public static String[] getDDMM(String str) {
        S.LogD("DATE : " + str);
        String[] dmy = {"", "", ""};
        if (str == null) {
            return dmy;
        }
        if (str.trim().isEmpty()) {
            return dmy;
        }
       /* if (str.trim().length() < 11) {
            return dmy;
        }*/
        try {
            if (str.contains("-")) {
                dmy[0] = S.prefixZeroIfSingleDigit(str.split("-")[S.DATE_POS]);
                dmy[1] = S.prefixZeroIfSingleDigit(str.split("-")[S.MONTH_POS]);
                dmy[2] = str.split("-")[2].substring(0, 4);
                return dmy;
            } else if (str.contains("/")) {
                dmy[0] = S.prefixZeroIfSingleDigit(str.split("/")[S.DATE_POS]);
                dmy[1] = S.prefixZeroIfSingleDigit(str.split("/")[S.MONTH_POS]);
                dmy[2] = str.split("/")[S.YEAR_POS].substring(0, 4);
                return dmy;
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return dmy;
        }
        return dmy;

    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null)
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getMonthName(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    public static void ShowUnderstandingAlert(Context context, String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "I Got It",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public static void hasOnlyLetters(final EditText editText) {
        editText.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if (cs.equals("")) { // for backspace
                            return cs;
                        }
                        if (cs.toString().matches("[a-zA-Z ]+")) {
                            return cs;
                        }
                        return "";
                    }
                }
        });
    }

    public static void hasOnlyAlphNumeric(final EditText editText) {
        editText.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if (cs.equals("")) { // for backspace
                            return cs;
                        }
                        if (cs.toString().matches("[a-zA-Z0-9. ]+")) {
                            return cs;
                        }
                        return "";
                    }
                }
        });
    }

    public static void hasOnlyAlphNumericWithSpecialChars(final EditText editText) {
        editText.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if (cs.equals("")) { // for backspace
                            return cs;
                        }
                        if (cs.toString().matches("[a-zA-Z0-9.-@^%!`~#$%^&*()+/<> ]+")) {
                            return cs;
                        }
                        return "";
                    }
                }
        });
    }

    //SHARED PREFERENCES TO STORE LOGIN CREDETIONALS
    public static boolean isUserLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(S.PREFS_LOGIN, Context.MODE_PRIVATE);

        if (prefs.getString("user_id", null) == null) return false;
        else
            return true;
    }

    public static final String PREFS_LOGIN = "PREFS_LOGIN";

    public static void saveUserLoginData(Context context, User u) {
        SharedPreferences.Editor editor = context.getSharedPreferences(S.PREFS_LOGIN, Context.MODE_PRIVATE).edit();
        editor.putString("user_id", u.user_id);
        editor.putString("user_name", u.user_name);
        editor.putString("user_mob", u.user_mob);
        editor.putString("user_blood", u.user_blood);
        editor.putString("enum_1", u.enum_1);
        editor.putString("enum_2", u.enum_2);
        editor.putString("enum_3", u.enum_3);
        editor.putString("user_addr", u.user_addr);
        editor.putString("spelization", u.spelization);
        editor.putString("facilites", u.facilites);
        editor.putString("lat", u.lat);
        editor.putString("lang", u.lang);
        editor.putString("user_type", u.user_type);
        //Toast.makeText(context,"HIIIIIIIIII"+ S.getUserDetails(context).user_id, Toast.LENGTH_LONG).show();
        editor.apply();
    }

    public static User getUserDetails(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(S.PREFS_LOGIN, Context.MODE_PRIVATE);
        User u = new User();
        u.user_id = prefs.getString("user_id", null);
        u.user_name = prefs.getString("user_name", null);
        u.user_mob = prefs.getString("user_mob", null);
        u.user_blood = prefs.getString("user_blood", null);
        u.enum_1 = prefs.getString("enum_1", null);
        u.enum_2 = prefs.getString("enum_2", null);
        u.user_addr = prefs.getString("user_addr", null);
        u.enum_3 = prefs.getString("enum_3", null);
        u.spelization = prefs.getString("spelization", null);
        u.facilites = prefs.getString("facilites", null);
        u.lat = prefs.getString("lat", null);
        u.lang = prefs.getString("lang", null);
        u.user_type = prefs.getString("user_type", null);
        return u;
    }


    public static void userLogout(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(S.PREFS_LOGIN, Context.MODE_PRIVATE).edit();
        editor.putString("user_id", null);
        editor.putString("user_name", null);
        editor.putString("user_mob", null);
        editor.putString("user_blood", null);
        editor.putString("enum_1", null);
        editor.putString("enum_2", null);
        editor.putString("enum_3", null);
        editor.putString("spelization", null);
        editor.putString("facilites", null);
        editor.putString("lat", null);
        editor.putString("lang", null);
        editor.putString("user_type", null);
        editor.putString("user_addr", null);
        editor.clear();
        editor.commit();
    }

    private double[] GetMinMaxLatLng(String lat_str, String lng_str) {


        double values[] = {0.0, 0.0, 0.0, 0.0};
        if (lat_str != null && lng_str != null) {
            double lat = Double.parseDouble(lat_str);
            double lng = Double.parseDouble(lng_str);
            if (lat_str.trim().length() > 0 && lng_str.trim().length() > 0) {
                double kmInLongitudeDegree = 111.320 * Math.cos(lat / 180.0 * Math.PI);
                double deltaLat = C.PROXIMITY_RADIUS_NEAR / 111.1;
                double deltaLong = C.PROXIMITY_RADIUS_NEAR / kmInLongitudeDegree;

                values[0] = lat - deltaLat;
                values[1] = lat + deltaLat;
                values[2] = lng - deltaLong;
                values[3] = lng + deltaLong;

//                lat1 = values[0] + "";
//                lat2 = values[1] + "";
//                lang1 = values[2] + "";
//                lang2 = values[3] + "";
            }
            // getNews();
        }
        return values;

    }

    private static FusedLocationProviderClient client;

    public static double[] getLocation(Context context) {

        final double values[] = {0.0, 0.0};
        client = LocationServices.getFusedLocationProviderClient(context);

        if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            client.getLastLocation().addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if (location != null) {

                        values[0] = location.getLatitude();
                        values[1] = location.getLongitude();

                        Log.d("ADebugTag", "Value: " + location.getLatitude() + "");
                        Log.d("ADebugTag", "Value: " + location.getLongitude() + "");
                    }
                }
            });
        }
        return values;
    }

    public static final String ON_OFF = "ON_OFF";

    public static void saveOn_Off(Context context, String on_off) {
        SharedPreferences.Editor editor = context.getSharedPreferences(S.ON_OFF, Context.MODE_PRIVATE).edit();
        editor.putString("on_off", on_off);
        editor.apply();
    }


    public static String getOn_Off(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(S.ON_OFF, Context.MODE_PRIVATE);
        String url = prefs.getString("on_off", null);
        return url;

    }

    public static void clearOn_Off(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(S.ON_OFF, Context.MODE_PRIVATE).edit();
        editor.putString("on_off", null);
        editor.clear();
        editor.commit();
    }
}