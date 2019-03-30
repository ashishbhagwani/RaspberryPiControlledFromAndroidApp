package com.example.ashish.advancedcarcontroller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GetIpAddressAndPortNumber extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_ip_address_and_port_number);
        Button proceedButton = findViewById(R.id.buttonproceed);
        final EditText ipAddressEditText = findViewById(R.id.edittextipaddress);
        final EditText speedIpAddressEditText = findViewById(R.id.edittextspeedipaddress);
        final EditText portNumberEditText = findViewById(R.id.edittextportnumber);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipAddressText = ipAddressEditText.getText().toString().trim();
                String portNumberText = portNumberEditText.getText().toString().trim();
                String speedIpAddress = speedIpAddressEditText.getText().toString().trim();
                if (!ipAddressText.isEmpty() && !portNumberText.isEmpty() && !speedIpAddress.isEmpty()) {
                    Globals.IP_Address = ipAddressText;
                    Globals.port_number = portNumberText;
                    Globals.SpeedIpAddress = speedIpAddress;
                    if (Globals.IP_Address != null && Globals.port_number != null) {
                        Intent intent = new Intent(GetIpAddressAndPortNumber.this, MainActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(GetIpAddressAndPortNumber.this, "Enter Proper Values", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
