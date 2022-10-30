package com.example.ihelpu.tools.planner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ihelpu.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddPlanActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText planTitle, planDescription;
    FirebaseFirestore fStore;
    FirebaseUser user;
    Button getDate, timePicker;
    TextView showDate, showTime;
    int mYear, mMonth, mDate, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);

        //Data
        getDate = findViewById(R.id.getDate);
        getDate.setOnClickListener(this);
        showDate = findViewById(R.id.showDate);

        //Czas
        showTime = findViewById(R.id.showTime);
        timePicker = findViewById(R.id.getTime);
        timePicker.setOnClickListener(this);

        //Baza danych
        fStore = FirebaseFirestore.getInstance();

        //Plan tytuł i opis
        planTitle = findViewById(R.id.planTitle);
        planDescription = findViewById(R.id.planDescription);

        Button addPlanExecute = findViewById(R.id.addPlanExecute);
        addPlanExecute.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();

        Button fab = findViewById(R.id.addPlanExecute);
        fab.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addPlanExecute:
                createPlan();
                break;

            case R.id.getDate:
                final Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDate = calendar.get(Calendar.DATE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddPlanActivity.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int date) {
                        String setDate = date + "-" + month + "-" + year;
                        showDate.setText(setDate);
                    }
                }, mYear, mMonth, mDate);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
                break;

            case R.id.getTime:
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    //Wybór czasu
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                        hour = selectedHour;
                        minute = selectedMinute;

                        //Połączenie w jedną wartośc i wyświetlenie
                        showTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);
                timePickerDialog.setTitle("Wybierz godzinę");
                timePickerDialog.show();
                break;
        }
    }

    private void createPlan() {
        //Tytuł planu
        String title = planTitle.getText().toString();
        //Opis planu
        String description = planDescription.getText().toString();
        //Data planu
        String date = showDate.getText().toString();
        //Godzina planu
        String time = showTime.getText().toString();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(AddPlanActivity.this, "Nie można zapisać.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Przelazywanie warotści utworzonych planów do bazy danych
        DocumentReference documentReference = fStore.collection("Plans").document(user.getUid()).collection("UsersPlans").document();
        Map<String, Object> note = new HashMap<>();
        note.put("titleField", title);
        note.put("descriptionField", description);
        note.put("dateField", date);
        note.put("timeField", time);

        documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddPlanActivity.this, "Plan został utworzony.", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddPlanActivity.this, "Coś poszło nie tak, spróbuj ponownie później.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}