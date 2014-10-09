package com.example.aditya841.nfctesting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class WifiActivationActivity extends Activity {
    Context context = this;
    private static final String TAG = "WifiActivationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifiactivation);
        Intent intent = getIntent();
        byte[] msgString = null;
        String s = "";
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                    msgString = msgs[i].getRecords()[0].getPayload();
                }
            }

            s = new String(msgString);
            s = s.trim();
            Log.d(TAG, s);
            enableWifi(s);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean enableWifi(String network) {
        network = network.replace("nfcwifi://", "");
        String[] wifiDetails = network.split("/");

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = '"' + wifiDetails[0].trim() + '"';
        conf.preSharedKey = '"' + wifiDetails[2].trim() + '"';
        conf.status = WifiConfiguration.Status.ENABLED;

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
            return true;
        }
        if (wifiManager.addNetwork(conf) < 0) {
            Toast.makeText(this, "Network was not added. Please try again", Toast.LENGTH_LONG).show();
            return false;
        } else {
            WifiInfo currentWifi = wifiManager.getConnectionInfo();
            if (currentWifi != null) {
                if (currentWifi.getSSID().equals(conf.SSID)) {
                    Toast.makeText(this, "Network already connected.", Toast.LENGTH_LONG).show();
                    return true;
                }
            } else {
                wifiManager.reconnect();
            }
        }
        return false;
    }
}
