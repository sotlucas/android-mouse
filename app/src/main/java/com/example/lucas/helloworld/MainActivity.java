package com.example.lucas.helloworld;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {
    public static final String EXTRA_MESSAGE = "com.example.lucas.helloworld.MESSAGE"; // Esto se podria borrar

    private static final String TAG = "MainActivity";

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private TextView xValue, yValue, zValue, connectionMsg;
    private EditText etIp, etPort, etMessage;
    private float x, y, z;

    private boolean conn = false;

    // Mouse buttons
    private Button leftMouse, rightMouse, scrollMouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set sensor
        Log.d(TAG, "onCreate: Initializing Sensor Services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG, "onCreate: Registered accelerometer listener");

        xValue = findViewById(R.id.xValue);
        yValue = findViewById(R.id.yValue);
        zValue = findViewById(R.id.zValue);

        // Set client GUI
        Button btnConnect = findViewById(R.id.connect);

        etIp = findViewById(R.id.ip);
        etPort = findViewById(R.id.port);
        etMessage = findViewById(R.id.message);
        connectionMsg = findViewById(R.id.txtConnect);

        btnConnect.setOnClickListener(this);

        // Setting buttons
        leftMouse = findViewById(R.id.leftMouseBtn);
        rightMouse = findViewById(R.id.rightMouseBtn);
        scrollMouse = findViewById(R.id.scrollArea);

        leftMouse.setOnClickListener(this);
        rightMouse.setOnClickListener(this);

        scrollMouse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch (View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "DOWN");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "UP");
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        x = event.values[0];
        y = event.values[1];
        z = event.values[2];

        // Sends every time the sensor info changes
        if (conn == true) {
            float[] pos = new float[]{x, y, z};
            sendArray(pos);
        }

        Log.d(TAG, "onSensorChanged: X: " + x + " Y: " + y + " Z: " + z);
        xValue.setText("xValue: " + x);
        yValue.setText("yValue: " + y);
        zValue.setText("zValue: " + z);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connect:
                if (!conn) {
                    conn = true;
                    connectionMsg.setText("Connected");
                } else {
                    conn = false;
                    connectionMsg.setText("Disconnected");
                }
                //sendMessage(etMessage.getText().toString());
                //float[] pos = new float[]{x, y, z};
                //sendArray(pos);
                break;
            case R.id.leftMouseBtn:
                if (conn) sendMessage("left");
                break;
            case R.id.rightMouseBtn:
                if (conn) sendMessage("right");
                break;
        }
    }

    private void sendMessage(final String msg) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String ip = etIp.getText().toString();
                int port = Integer.parseInt(etPort.getText().toString());

                try {
                    Socket s = new Socket(ip, port);

                    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());

                    out.writeObject(msg);

                    // Get reply from server here

                    out.close();
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private void sendArray(final float arr[]) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String ip = etIp.getText().toString();
                int port = Integer.parseInt(etPort.getText().toString());

                try {
                    Socket s = new Socket(ip, port);

                    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());

                    out.writeObject(arr);
                    // out.flush();

                    // Get reply from server here

                    out.close();
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}
