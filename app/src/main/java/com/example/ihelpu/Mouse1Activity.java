package com.example.ihelpu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Mouse1Activity extends AppCompatActivity implements View.OnClickListener {
    private TextView showX;
    private ImageView touchDetect;
    private VelocityTracker mVelocityTracker = null;
    float xPos = 0, yPos = 0, xSave = 0, ySave = 0, x = 0, y = 0, xEnd = 0, yEnd = 0;
    private final Paint paint = new Paint();
    private Bitmap bitmap;
    private Canvas canvas;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse1);

        showX = (TextView) findViewById(R.id.showx);

        ImageView top_button = (ImageView) findViewById(R.id.topHomeButton);
        top_button.setOnClickListener(this);

        TextView top_text = (TextView) findViewById(R.id.topText);
        top_text.setText("Touchpad");

        addTouchListener();
    }

    private void Draw(){
        if (bitmap == null){
            bitmap = Bitmap.createBitmap(touchDetect.getWidth(),touchDetect.getHeight(),Bitmap.Config.ARGB_8888);

            canvas = new Canvas(bitmap);

            paint.setAntiAlias(true);
            paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4f);
        }

        canvas.drawLine(x, y, xEnd, yEnd, paint);

        touchDetect.setImageBitmap(bitmap);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void addTouchListener(){
        touchDetect = (ImageView) findViewById(R.id.touchDetect);

        touchDetect.setOnTouchListener(new View.OnTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onDoubleTap(@NonNull MotionEvent e) {
                    Toast.makeText(Mouse1Activity.this,"Kliknięto dwa razy", Toast.LENGTH_LONG).show();
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
                    Toast.makeText(Mouse1Activity.this,"Kliknięto raz", Toast.LENGTH_LONG).show();
                    return super.onSingleTapUp(e);
                }
            });
            public boolean onTouch (View v, MotionEvent event){
            int action = 0, pointerId = 0;
            int counter = event.getPointerCount();
            if(counter <= 2) {
                action = event.getActionMasked();
                pointerId = event.getPointerId(event.getActionIndex());
            }
           else{
               return false;
            }
           gestureDetector.onTouchEvent(event);

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    if (mVelocityTracker == null) {
                        mVelocityTracker = VelocityTracker.obtain();
                    }
                    mVelocityTracker.addMovement(event);

                    x = event.getX();
                    y = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mVelocityTracker.addMovement(event);
                    xPos =mVelocityTracker.getXVelocity(pointerId);
                    yPos =mVelocityTracker.getYVelocity(pointerId)*(-1);
                    mVelocityTracker.computeCurrentVelocity(1,1000);

                    xEnd =event.getX();
                    yEnd =event.getY();

                    Draw();

                    x =event.getX();
                    y =event.getY();

                    xSave +=xPos;
                    ySave +=yPos;
                    String x2 = String.valueOf(xSave);
                    String y2 = String.valueOf(ySave);

                    showX.setText("x: "+x2 +" y: "+y2);
                    break;

                case MotionEvent.ACTION_UP:
                    bitmap.recycle();
                    bitmap = null;
                    touchDetect.setImageBitmap(bitmap);
                    break;

                case MotionEvent.ACTION_CANCEL:
                    mVelocityTracker.clear();
                    mVelocityTracker.recycle();
                    return true;
                }
                return true;
            }
        });
    }

    private void goToHomeActivity(){
        Intent intent = new Intent(getApplicationContext(), TabsActivity.class);
        startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topHomeButton:
                goToHomeActivity();
                break;
        }
    }
}
