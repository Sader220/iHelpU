package com.example.ihelpu.tools.mouse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ihelpu.R;
import com.example.ihelpu.tools.tabs.TabsActivity;

public class Mouse1Activity extends AppCompatActivity implements View.OnClickListener {
    //Wyświetlanie warotści
    private TextView showX, showY, showClick;

    //Obszar roboczy
    private ImageView touchDetect;

    //Wektor przesunięcia, rysowanie
    private VelocityTracker mVelocityTracker = null;
    private float x = 0, y = 0, xEnd = 0, yEnd = 0;
    private int xPos = 0, yPos = 0, xSave = 0, ySave = 0;

    //Obrazowanie dotyku
    private final Paint paint = new Paint();
    private Bitmap bitmap;
    private Canvas canvas;

    private int n = 10;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse1);

        //Wyświetlnie przesunięcia
        showX = findViewById(R.id.showx);
        showY = findViewById(R.id.showy);
        showClick = findViewById(R.id.showClick);
        showX.setText("0");
        showY.setText("0");
        showClick.setText("0");

        ImageView top_button = findViewById(R.id.topHomeButton);
        top_button.setOnClickListener(this);

        TextView top_text = findViewById(R.id.topText);
        top_text.setText("Touchpad");

        addTouchListener();
    }

    private void showDraw(){
        showClick.setText("0");
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
        showClick.setText("0");

        touchDetect = findViewById(R.id.touchDetect);
        touchDetect.setOnTouchListener(new View.OnTouchListener() {
            final GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener(){
                @Override

                public boolean onDoubleTap(@NonNull MotionEvent e) {
                    showClick.setText("2");
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
                    showClick.setText("1");
                    return super.onSingleTapUp(e);
                }
            });

            public boolean onTouch (View v, MotionEvent event){
                sendMouse();

                int action, pointerId;
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
                        xPos = (int) mVelocityTracker.getXVelocity(pointerId)*3;
                        yPos = (int) mVelocityTracker.getYVelocity(pointerId)*3;
                        mVelocityTracker.computeCurrentVelocity(2);

                        xEnd =event.getX();
                        yEnd =event.getY();

                        showDraw();

                        x = event.getX();
                        y = event.getY();

                        xSave +=xPos;
                        ySave +=yPos;
                        if(xSave<0){
                            xSave = 0;
                        }
                        if(ySave<0){
                            ySave = 0;
                        }

                        String x2 = String.valueOf(xSave);
                        String y2 = String.valueOf(ySave);

                        showX.setText(x2);
                        showY.setText(y2);

                        break;

                    case MotionEvent.ACTION_UP:
                        bitmap.recycle();
                        bitmap = null;
                        touchDetect.setImageBitmap(null);

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


    public void sendMouse(){
        String data = showX.getText().toString() + "|" + showY.getText().toString() + "|" +showClick.getText().toString();

        if (showClick.getText().toString() != "0") {
            showClick.setText("0");
        }

//        RegisterMouse registerMouse = new RegisterMouse();
//        registerMouse.execute(data);
    }
}
