package com.example.elpis.ac_ctrl;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ConsumerIrManager mCIR;
    SparseArray<String> irData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCIR = (ConsumerIrManager)getSystemService(Context.CONSUMER_IR_SERVICE);


        setContentView(R.layout.activity_main);
        irData = new SparseArray<String>();
        irData.put(
                R.id.buttonOn,
                hex2dec("0000 006D 001E 0000 0140 00A1 0014 003C 0014 0014 0014 0014 0014 0014 0014 003C 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 003C 0014 003C 0014 0014 0014 003C 0014 0014 0014 0014 0014 0014 0014 003C 0014 003C 0014 003C 0014 00A1"));
        irData.put(
                R.id.buttonOff,
                hex2dec("0000 006D 001E 0000 0140 00A1 0014 003C 0014 0014 0014 0014 0014 0014 0014 003C 0014 0014 0014 0014 0014 0014 0014 003C 0014 003C 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 003C 0014 0014 0014 003C 0014 0014 0014 0014 0014 0014 0014 003C 0014 00A1"));
        irData.put(
                R.id.buttonTest,
                hex2dec("0000 006D 001E 0000 0140 00A1 0014 003C 0014 0014 0014 0014 0014 0014 0014 003C 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 003C 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 0014 003C 0014 00A1"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.buttonOn).setOnClickListener(mSendClickListenerOn);
        findViewById(R.id.buttonOff).setOnClickListener(mSendClickListenerOff);
        findViewById(R.id.buttonTest).setOnClickListener(mSendClickListenerTest);

    }

    View.OnClickListener mSendClickListenerOn = new View.OnClickListener() {

        public void onClick(View view) {
            if (!mCIR.hasIrEmitter()) {
                return;
            }
            String data = irData.get(view.getId());
            //Log.d("ON", data);
            if (data != null) {
                String values[] = data.split(",");
                int[] pattern = new int[values.length - 1];

                for (int i = 0; i < pattern.length; i++) {
                    pattern[i] = Integer.parseInt(values[i + 1]);
                }

                mCIR.transmit(Integer.parseInt(values[0]), pattern);
            }
        }
    };
    View.OnClickListener mSendClickListenerOff = new View.OnClickListener() {

        public void onClick(View view) {
            if (!mCIR.hasIrEmitter()) {
                return;
            }
            String data = irData.get(view.getId());
            //Log.d("OFF", data);
            if (data != null) {
                String values[] = data.split(",");
                int[] pattern = new int[values.length - 1];

                for (int i = 0; i < pattern.length; i++) {
                    pattern[i] = Integer.parseInt(values[i + 1]);
                }

                mCIR.transmit(Integer.parseInt(values[0]), pattern);
            }
        }
    };
    View.OnClickListener mSendClickListenerTest = new View.OnClickListener() {

        public void onClick(View view) {
            if (!mCIR.hasIrEmitter()) {
                return;
            }
            String data = irData.get(view.getId());
            //Log.d("TEST", data);
            if (data != null) {
                String values[] = data.split(",");
                int[] pattern = new int[values.length - 1];

                for (int i = 0; i < pattern.length; i++) {
                    pattern[i] = Integer.parseInt(values[i + 1]);
                }

                mCIR.transmit(Integer.parseInt(values[0]), pattern);
            }
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected String hex2dec(String irData) {
        List<String> list = new ArrayList<String>(Arrays.asList(irData
                .split(" ")));
        list.remove(0); // dummy
        int frequency = Integer.parseInt(list.remove(0), 16); // frequency
        list.remove(0); // seq1
        list.remove(0); // seq2

        for (int i = 0; i < list.size(); i++) {
            list.set(i, Integer.toString(Integer.parseInt(list.get(i), 16)));
        }

        frequency = (int) (1000000 / (frequency * 0.241246));
        list.add(0, Integer.toString(frequency));

        irData = "";
        for (String s : list) {
            irData += s + ",";
        }
        return irData;
    }
}
