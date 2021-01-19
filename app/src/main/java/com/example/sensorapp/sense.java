package com.example.sensorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class sense extends AppCompatActivity implements SensorEventListener {
TextView xv,yv,zv;
EditText number;
SensorManager sensorManager;
Sensor as;
boolean isAvail,isnotFirst=false;
float cx,cy,cz,lx,ly,lz,xd,yd,zd;
float threshold=5f;
private Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sense);
        xv=findViewById(R.id.xv);
        yv=findViewById(R.id.yv);
        zv=findViewById(R.id.zv);
        number=findViewById(R.id.number);
        vibrator= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null){
            as=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAvail=true;
        }else {
            xv.setText("Sensor not available");
            isAvail=false;
        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(isAvail){
            sensorManager.registerListener(this,as,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isAvail){
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        xv.setText(event.values[0]+"m/s2");
        yv.setText(event.values[1]+"m/s2");
        zv.setText(event.values[2]+"m/s2");
        cx=event.values[0];
        cy=event.values[1];
        cz=event.values[2];
        if(isnotFirst){
          xd=Math.abs(lx-cx);
          yd=Math.abs(ly-cy);
          zd=Math.abs(lz-cz);
          if((xd>threshold && yd>threshold)||(xd>threshold && zd>threshold)||(zd>threshold && yd>threshold)){
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                  vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
              }
              else {
                  vibrator.vibrate(500);
              }
              String phone=number.getText().toString();
              if(phone.isEmpty()){
                  Toast.makeText(this,"Enter a number to call",Toast.LENGTH_SHORT).show();
              }
              else{
                  String s="tel:"+phone;
                  Intent intent=new Intent(Intent.ACTION_CALL);
                  intent.setData(Uri.parse(s));
                  startActivity(intent);
              }
          }

        }

        lx=cx;ly=cy;lz=cz;isnotFirst=true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
   /* static final int REQUEST_SELECT_CONTACT = 1;

    public void selectContact(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_CONTACT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {
           // Uri contactUri = data.getData();
            String[] cols={ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

            Cursor rs = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);
            String ph="";
            if(rs.moveToFirst()){
                System.out.println("**"+rs.getString(rs.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                 ph=rs.getString(rs.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }

            String s="tel:"+ph;
            /*Intent intent=new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(s));
            number.setText(ph);
            startActivity(intent);
        }

    }*/
}