package com.example.internetcheck;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.EXTRA_NO_CONNECTIVITY);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(new CheckConnectivity(), filter);

        IntentFilter GPSFilter = new IntentFilter();
        GPSFilter.addAction(LocationManager.MODE_CHANGED_ACTION);
        GPSFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        this.registerReceiver(new GpsLocationReceiver(), GPSFilter);

    }

    public class CheckConnectivity extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent arg1) {
            Log.i("hi", "hi");

            boolean isConnected = arg1.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            if(isConnected){
                Toast.makeText(context, "Internet Connection Lost", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(context, "Internet Connected", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class GpsLocationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final LocationManager manager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
            if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                Toast.makeText(context, "GPS connected",
                        Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "GPS Not Connected",
                        Toast.LENGTH_SHORT).show();
            }

            }
        }
    }
