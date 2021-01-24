package com.example.bluetoothandnetworkcheck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.app.NotificationChannel;


public class MainActivity extends AppCompatActivity   {
    ToggleButton tblBluetoothButton;
    ToggleButton tblNetworkButton;
    BluetoothAdapter bluetoothAdapter;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    WifiManager wifi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getting bluetooth object
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //getting Mobile Network object
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();

        //getting Mobile Wifi object
        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        tblBluetoothButton= (ToggleButton) findViewById(R.id.toggleButtonForBluetooth);
        tblNetworkButton = (ToggleButton) findViewById(R.id.toggleButtonForNetwork);

        fnCheckBluetoothStatus();
        fnCheckDataNetworkStatus();
    }



    public void onClickToCheckBluetooth(View v)

    {
        if (tblBluetoothButton.isChecked()) {

            //confirming that device support for bluetooth or not
            if (bluetoothAdapter == null) {
                Toast.makeText(this, "Device does not support to Bluetooth", Toast.LENGTH_LONG).show();
            }
           //enable bt if is disabled
            if (!bluetoothAdapter.isEnabled()) {
                //enabled bluetooth
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, 1);
            }
            fnShowNotification("Notification","Bluetooth turned on");
            //creating intent other discover other bt devices
            //enabled bluetooth
            Intent discoverintent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverintent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3000);
            startActivity(discoverintent);
        }
        else{
            Toast.makeText(getApplicationContext()," Bluetooth turned off", Toast.LENGTH_LONG).show();}
        //get bt object if bt is enable making disable
        if(bluetoothAdapter.isEnabled())
        {
            //disable the bt
            if(bluetoothAdapter!=null)
            {
                bluetoothAdapter.disable();
                fnShowNotification("Notification","Bluetooth turned off");
            }
        }
    }

    public void onClickCheckMobileNetwork(View v){
        if (tblNetworkButton.isChecked()) {

            if (!wifi.isWifiEnabled()) {
                //enable bt if is disabled
                wifi.setWifiEnabled(true);
                fnShowNotification("Notification","Wifi turned on");
                Toast.makeText(getApplicationContext()," Wifi turned on", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext()," wifi turned off", Toast.LENGTH_LONG).show();}
        //get bt object if bt is enable making disable
        if(wifi.isWifiEnabled())
        {
            //disable the bt
            wifi.setWifiEnabled(false);
            fnShowNotification("Notification","Wifi turned off");
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        fnCheckBluetoothStatus();
        fnCheckDataNetworkStatus();

    }
    @Override
    protected void onPause () {
        super.onPause(); ;
        fnCheckBluetoothStatus();
        fnCheckDataNetworkStatus();
    }

    private void fnCheckBluetoothStatus() {
        if(bluetoothAdapter.isEnabled()){
            tblBluetoothButton.setChecked(true);
            fnShowNotification("Notification","Bluetooth turned on");
        }else{
            tblBluetoothButton.setChecked(false);
            fnShowNotification("Notification","Bluetooth turned off");
        }
    }
    private void fnCheckDataNetworkStatus() {
        if (wifi.isWifiEnabled()) {
            tblNetworkButton.setChecked(true);
            fnShowNotification("Notification","Wifi turned on");
        } else {
            tblNetworkButton.setChecked(false);
            fnShowNotification("Notification","Wifi turned off");
        }
    }

    private void fnShowNotification(String title, String message){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this )
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());
    }


}