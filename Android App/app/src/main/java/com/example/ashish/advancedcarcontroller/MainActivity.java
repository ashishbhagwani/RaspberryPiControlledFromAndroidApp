package com.example.ashish.advancedcarcontroller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private int maxSpeed=180;
    private int minSpeed=90;
    private int currentSpeed=minSpeed;
    private long brakePressTime;
    private long leftButtonTime;
    private long rightButtonTime;
    private ScheduledExecutorService increaseSpeed;
    private ScheduledExecutorService decreaseSpeed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button rightButton=(Button)findViewById(R.id.buttonright);
        Button leftButton=(Button)findViewById(R.id.buttonleft);
        Button accelarateButton=(Button)findViewById(R.id.buttonaccelarate);
        Button stopButton=(Button)findViewById(R.id.buttonstop);
        Button breakButton=(Button)findViewById(R.id.buttonbreak);
        final TextView speedTextView=(TextView)findViewById(R.id.speeddisplay);
        final TextView directionTextView=(TextView)findViewById(R.id.directiontextview);
        final TextView turningTimeTextView=(TextView)findViewById(R.id.turningtimetextview);
        final TextView brakeTimeTextView=(TextView)findViewById(R.id.braketimetextview);

        rightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    giveSteerCommand("r1");
                    directionTextView.setText("Right");
                    rightButtonTime=System.currentTimeMillis();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    giveSteerCommand("r0");
                    directionTextView.setText("");
                    turningTimeTextView.setText(Long.toString(System.currentTimeMillis()-rightButtonTime));
                }
                return true;
            }
        });
        leftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    giveSteerCommand("l1");
                    directionTextView.setText("Left");
                    leftButtonTime=System.currentTimeMillis();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    giveSteerCommand("l0");
                    directionTextView.setText("");
                    turningTimeTextView.setText(Long.toString(System.currentTimeMillis()-leftButtonTime));
                }
                return true;
            }
        });
        accelarateButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // If this button is kept pressed it will increase the speed by 1 after each second
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    increaseSpeed = Executors.newSingleThreadScheduledExecutor();
                    increaseSpeed.scheduleAtFixedRate(new Runnable() {
                        @Override
                        public void run() {
                            currentSpeed++;
                            if (currentSpeed <= maxSpeed) {
                                giveSpeedCommand("s" + Integer.toString(currentSpeed));
                                speedTextView.post(new Runnable(){
                                    public void run(){
                                        speedTextView.setText(Integer.toString(currentSpeed));
                                    }
                                });
                            }
                        }
                    }, 0, 1, TimeUnit.SECONDS);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    increaseSpeed.shutdown();
                }
                return true;
            }
        });
        stopButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    decreaseSpeed = Executors.newSingleThreadScheduledExecutor();
                    decreaseSpeed.scheduleAtFixedRate(new Runnable() {
                        @Override
                        public void run() {
                            currentSpeed--;
                            if (currentSpeed >minSpeed) {
                                giveSpeedCommand("s" + Integer.toString(currentSpeed));
                                speedTextView.post(new Runnable(){
                                    public void run(){
                                        speedTextView.setText(Integer.toString(currentSpeed));
                                    }
                                });
                            }
                        }
                    }, 0, 1, TimeUnit.SECONDS);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    decreaseSpeed.shutdown();
                }
                return true;
            }
        });
        breakButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    giveSpeedCommand("s0");
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    giveSteerCommand("b1");
                    brakePressTime=System.currentTimeMillis();
                    currentSpeed=minSpeed;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    giveSteerCommand("b0");
                    brakeTimeTextView.setText(Long.toString(System.currentTimeMillis()-brakePressTime));
                }
                return true;
            }
        });

    }
    private void giveSteerCommand(final String command){
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Socket clientSocket=new Socket(Globals.IP_Address,Integer.parseInt(Globals.port_number));
                    clientSocket.getOutputStream().write(command.getBytes());
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void giveSpeedCommand(final String command){
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Socket clientSocket=new Socket(Globals.SpeedIpAddress,Integer.parseInt(Globals.port_number));
                    clientSocket.getOutputStream().write(command.getBytes());
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
