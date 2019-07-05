package com.lobxy.cryptoprices.View;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lobxy.cryptoprices.Controller.CustomRecyclerViewAdapter;
import com.lobxy.cryptoprices.Controller.RetrofitClient;
import com.lobxy.cryptoprices.Controller.RetrofitInterface;
import com.lobxy.cryptoprices.Model.CryptoData;
import com.lobxy.cryptoprices.Model.Datum;
import com.lobxy.cryptoprices.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = "663be5e0-a85c-418a-808c-8d0b3b64860b";
    private static final String TAG = "Main";

    private TextView text_no_connection;

    //parameters for request.
    private int limit = 20;
    private String param = "price";
    private String order = "asc";

    //TODO: save filters into sharedPreferences and load data accordingly.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_no_connection = findViewById(R.id.main_text_no_connectivity);

        //no reason to call it in onStart() as the api refreshes values in minimum 1 hour.
        if (connectivity()) {
            getData();
        } else {
            text_no_connection.setVisibility(View.VISIBLE);
        }

    }

    public void getData() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Working...");
        dialog.show();

        RetrofitInterface request = RetrofitClient.getClient().create(RetrofitInterface.class);

        Call<CryptoData> call = request.getCryptoData(API_KEY, 1, limit, param, order);

        call.enqueue(new Callback<CryptoData>() {
            @Override
            public void onResponse(Call<CryptoData> call, Response<CryptoData> response) {
                dialog.dismiss();

                switch (response.code()) {
                    case 200:
                        setData(response.body().getData());
                        break;
                    case 400:
                        Toast.makeText(MainActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                        break;
                    case 401:
                        Toast.makeText(MainActivity.this, "Unauthorized Request", Toast.LENGTH_SHORT).show();
                        break;
                    case 403:
                        Toast.makeText(MainActivity.this, "Access Forbidden", Toast.LENGTH_SHORT).show();
                        break;
                    case 429:
                        Toast.makeText(MainActivity.this, "Too many request", Toast.LENGTH_SHORT).show();
                        break;
                    case 500:
                        Toast.makeText(MainActivity.this, "Internal Error", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Contact Support", Toast.LENGTH_SHORT).show();
                        break;
                }

            }

            @Override
            public void onFailure(Call<CryptoData> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onFailure: error: " + t.getMessage());
            }
        });

    }

    public void setData(List<Datum> list) {
        RecyclerView mRecyclerView = findViewById(R.id.main_recyclerview);
        CustomRecyclerViewAdapter adapter = new CustomRecyclerViewAdapter(list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
    }

    private void handleFilters() {
        // sort by - Rank,price,percent_change_1h,percent_change_24h,percent_change_7d
        // sort-desc = asc,desc

        LayoutInflater layoutInflater = getLayoutInflater();
        View alertView = layoutInflater.inflate(R.layout.dialog_filters, null);
        final Spinner spinner_params = alertView.findViewById(R.id.dialog_spinner_params);
        final Spinner spinner_order = alertView.findViewById(R.id.dialog_spinner_order);
        final EditText edit_limit = alertView.findViewById(R.id.dialog_edit_limit);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Filters")
                .setView(alertView)
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        limit = Integer.valueOf(edit_limit.getText().toString().trim());
                        if (limit > 20) {
                            Toast.makeText(MainActivity.this, "Over the maximum limit", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            param = spinner_params.getSelectedItem().toString();
                            order = spinner_order.getSelectedItem().toString();
                            
                            configureParameters(param, order);
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void configureParameters(String p, String o) {
        switch (p) {
            case "Rank":
                param = "cms_rank";
                break;
            case "Price":
                param = "price";
                break;
            case "Change in 1 Hour":
                param = "percent_change_1h";
                break;
            case "Change in 1 Day":
                param = "percent_change_24h";
                break;
            case "Change in 1 Week":
                param = "percent_change_7d";
                break;
        }

        switch (o) {
            case "Asc":
                order = "asc";
                break;
            case "Desc":
                order = "desc";
                break;
        }

        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_main_filter) {
            //show filter options.
            handleFilters();

        }
        return super.onOptionsItemSelected(item);
    }

    private Boolean connectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}

