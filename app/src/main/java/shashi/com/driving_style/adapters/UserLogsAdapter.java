package shashi.com.driving_style.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import shashi.com.driving_style.R;
import shashi.com.driving_style.activities.GetFeedbacksActivity;
import shashi.com.driving_style.api.Api;
import shashi.com.driving_style.api.WebServices;
import shashi.com.driving_style.logics.S;
import shashi.com.driving_style.models.DeleteLogsInput;
import shashi.com.driving_style.models.GetLogResult;
import shashi.com.driving_style.models.Logs;

public class UserLogsAdapter extends RecyclerView.Adapter<UserLogsAdapter.MyViewHolder> {
    private SweetAlertDialog pDialog;
    List<Logs> papers = new ArrayList<>();
    private Context context;

    String address; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
    String city;
    String state;
    String country;
    String postalCode;
    String knownName;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView source, dest, humps, time, avgspeed, mxspeed, date, del,dell;
        public LinearLayout row_details;

        public MyViewHolder(View view) {
            super(view);

            source = (TextView) view.findViewById(R.id.source);
            dest = (TextView) view.findViewById(R.id.dest);
            humps = (TextView) view.findViewById(R.id.humps);
            time = (TextView) view.findViewById(R.id.time);
            avgspeed = (TextView) view.findViewById(R.id.avgspeed);
            mxspeed = (TextView) view.findViewById(R.id.mxspeed);
            date = (TextView) view.findViewById(R.id.date);
            del = (TextView) view.findViewById(R.id.del);
            dell = (TextView) view.findViewById(R.id.dell);
            row_details = (LinearLayout) view.findViewById(R.id.row);
        }
    }

    public UserLogsAdapter(Context context, List<Logs> pp) {
        this.papers = pp;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.logs_row, parent, false);

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading...");
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Logs Papers = papers.get(position);

//        Geocoder geocoder;
//
//        List<Address> addresses;
//        geocoder = new Geocoder(context, Locale.getDefault());
//
//        try {
//            addresses = geocoder.getFromLocation(Double.parseDouble(Papers.getSource_lat()), Double.parseDouble(Papers.getSource_lng()), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//            city = addresses.get(0).getLocality();
//            state = addresses.get(0).getAdminArea();
//            country = addresses.get(0).getCountryName();
//            postalCode = addresses.get(0).getPostalCode();
//            knownName = addresses.get(0).getFeatureName();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if (!S.isUserLoggedIn(context)) {
            if (Papers.getStatus().equals("1")) {
                holder.del.setText("Approved");
                holder.del.setEnabled(false);
                holder.del.setBackgroundColor(Color.parseColor("#288231"));
            }
        }

        if (S.isUserLoggedIn(context)) {
            holder.del.setVisibility(View.GONE);
            holder.dell.setVisibility(View.GONE);
            if (Papers.getStatus().equals("0")) {
             //   holder.row_details.linearLa
                return;
            }
        }

        holder.source.setText("Source : " + Papers.getSrc());
        holder.dest.setText("Destination : " + Papers.getDest());
        holder.humps.setText("Humps : " + Papers.getHumps() + "/5*");
        holder.time.setText("Road Condition : " + Papers.getRoad_condn() + "/5*");
        holder.avgspeed.setText("Traffic : " + Papers.getTraffic() + "/5*");
        // holder.mxspeed.setText("Max. Speed : " + String.format("%.2f", Double.parseDouble(Papers.getMax_speed())) + "km/h");
        holder.date.setText("Feedback By : " + Papers.getUser_name());

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFeedback(Papers.getId());
            }
        });

        holder.dell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFeedback(Papers.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return papers.size();
    }

    /*===SHOW ALERT DILOG BOX===*/
//    private AlertDialog AskOption(final String id) {
//        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(context)
//                //set message, title, and icon
//                .setTitle("DELETE")
//                .setMessage("Do you want to Delete ?")
//                .setIcon(R.drawable.delete)
//
//                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        //your deleting code
////                        deletePaper(id);
//                        dialog.dismiss();
//                    }
//
//                })
//
//
//                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        dialog.dismiss();
//
//                    }
//                })
//                .create();
//        return myQuittingDialogBox;
//
//    }

    /*===DELETE PAPER===*/
    private void updateFeedback(String iid) {
        Retrofit retrofit = Api.getRetrofitBuilder(context);
        WebServices webServices = retrofit.create(WebServices.class);
        pDialog.show();
        DeleteLogsInput cat = new DeleteLogsInput(
                iid
        );
        //CALL NOW
        webServices.dell(cat)
                .enqueue(new Callback<GetLogResult>() {
                    @Override
                    public void onResponse(Call<GetLogResult> call, Response<GetLogResult> response) {

                        if (pDialog.isShowing()) pDialog.dismiss();
                        if (!S.analyseResponse(response)) {
                            S.ShowErrorDialogAndBeHere(context, "Error", "SERVER ERROR");
                            return;
                        }

                        GetLogResult result = response.body();
                        if (result.is_success) {
                            // S.ShowSuccessDialog(context, "Success", result.msg);
                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                            //   S.ShowSuccessDialog(context, "Success", result.msg);
                            Intent intent = new Intent(context, GetFeedbacksActivity.class);
                            context.startActivity(intent);

                        } else {
                            S.ShowErrorDialogAndBeHere(context, "Error", result.msg);
                        }
                    }

                    @Override
                    public void onFailure(Call<GetLogResult> call, Throwable t) {
                        S.ShowErrorDialogAndBeHere(context, "Error", "Please Check Your Internet Connection");
                        if (pDialog.isShowing()) pDialog.dismiss();
                        S.displayNetworkErrorMessage(context, null, t);
                        t.printStackTrace();
                    }
                });
    }
    private void deleteFeedback(String iid) {
        Retrofit retrofit = Api.getRetrofitBuilder(context);
        WebServices webServices = retrofit.create(WebServices.class);
        pDialog.show();
        DeleteLogsInput cat = new DeleteLogsInput(
                iid
        );
        //CALL NOW
        webServices.delll(cat)
                .enqueue(new Callback<GetLogResult>() {
                    @Override
                    public void onResponse(Call<GetLogResult> call, Response<GetLogResult> response) {

                        if (pDialog.isShowing()) pDialog.dismiss();
                        if (!S.analyseResponse(response)) {
                            S.ShowErrorDialogAndBeHere(context, "Error", "SERVER ERROR");
                            return;
                        }

                        GetLogResult result = response.body();
                        if (result.is_success) {
                            // S.ShowSuccessDialog(context, "Success", result.msg);
                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                            //   S.ShowSuccessDialog(context, "Success", result.msg);
                            Intent intent = new Intent(context, GetFeedbacksActivity.class);
                            context.startActivity(intent);

                        } else {
                            S.ShowErrorDialogAndBeHere(context, "Error", result.msg);
                        }
                    }

                    @Override
                    public void onFailure(Call<GetLogResult> call, Throwable t) {
                        S.ShowErrorDialogAndBeHere(context, "Error", "Please Check Your Internet Connection");
                        if (pDialog.isShowing()) pDialog.dismiss();
                        S.displayNetworkErrorMessage(context, null, t);
                        t.printStackTrace();
                    }
                });
    }

}
