package com.example.aditya841.nfctesting;

import android.app.Activity;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

/**
 * Created by aditya841 on 10/3/2014.
 */
public class WritingActivity extends Activity {
    Tag mytag;
    NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            try {
                writeTag(mytag);
            } catch (Exception e) {
                Log.d("NFCTag", "Got and error");
            }
        }
    }


    public boolean writeTag(Tag myTag) throws IOException, FormatException {
        if (myTag == null) {
            return false;
        }
        NdefRecord rtdUriRecord1 = NdefRecord.createUri("nfcwifi://N5KTF/wap/ye@hright10504");
        NdefMessage ndefMessage = new NdefMessage(rtdUriRecord1);
        Ndef ndef = Ndef.get(mytag);
        ndef.connect();
        ndef.writeNdefMessage(ndefMessage);
        ndef.close();
        return true;
    }
}
