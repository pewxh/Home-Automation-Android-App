package com.example.home_automation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class ledControl extends AppCompatActivity {
    Button dn1, df1, dn2, df2, allOn, allOff;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter bluetoothAdapter = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_control);
        Intent newint = getIntent();
        address = newint.getStringExtra(MainActivity.EXTRA_ADDRESS); //receive the address of the bluetooth device
        setContentView(R.layout.activity_led_control);
        dn1 = findViewById(R.id.dn1);
        dn2 = findViewById(R.id.dn2);
        df1 = findViewById(R.id.df1);
        df2 = findViewById(R.id.df2);
        allOn = findViewById(R.id.allOn);
        allOff = findViewById(R.id.allOff);
        new ConnectBT().execute();
        dn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btSocket != null) {
                    try {
                        btSocket.getOutputStream().write("N1".toString().getBytes());
                    } catch (IOException e) {
                        System.out.println("Error");
                    }
                }
            }
        });
        dn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btSocket != null) {
                    try {
                        btSocket.getOutputStream().write("N2".toString().getBytes());
                    } catch (IOException e) {
                        System.out.println("Error");
                    }
                }
            }
        });
        df1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btSocket != null) {
                    try {
                        btSocket.getOutputStream().write("F1".toString().getBytes());
                    } catch (IOException e) {
                        System.out.println("Error");
                    }
                }
            }
        });
        df2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btSocket != null) {
                    try {
                        btSocket.getOutputStream().write("F2".toString().getBytes());
                    } catch (IOException e) {
                        System.out.println("Error");
                    }
                }
            }
        });
        allOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btSocket != null) {
                    try {
                        btSocket.getOutputStream().write("TO".toString().getBytes());
                    } catch (IOException e) {
                        System.out.println("Error");
                    }
                }
            }
        });
        allOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btSocket != null) {
                    try {
                        btSocket.getOutputStream().write("TF".toString().getBytes());
                    } catch (IOException e) {
                        System.out.println("Error");
                    }
                }
            }
        });

//        @Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//            // Handle action bar item clicks here. The action bar will
//            // automatically handle clicks on the Home/Up button, so long
//            // as you specify a parent activity in AndroidManifest.xml.
//            int id = item.getItemId();
//
//            //noinspection SimplifiableIfStatement
//            if (id == R.id.action_settings) {
//                return true;
//            }
//
//            return super.onOptionsItemSelected(item);
//        }
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(ledControl.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket == null || !isBtConnected) {
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = bluetoothAdapter.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                System.out.println("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            } else {
                System.out.println("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}

