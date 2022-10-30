package com.example.ihelpu.tools.planner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ihelpu.MainActivity;
import com.example.ihelpu.R;
import com.example.ihelpu.tools.tabs.TabsActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class PlannerActivity extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton addPlan;
    ActionBarDrawerToggle toggle;
    FirebaseFirestore fStore;
    PlanAdapter planAdapter;
    FirebaseUser user;
    FirebaseAuth mAuth;
    RecyclerView plansView;
    ArrayList<Plan> planArrayList;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        addPlan = findViewById(R.id.addPlan);
        addPlan.setOnClickListener(this);

        plansView = findViewById(R.id.plans_view);
        plansView.setHasFixedSize(true);
        plansView.setLayoutManager(new LinearLayoutManager(this));

        fStore = FirebaseFirestore.getInstance();

        planArrayList = new ArrayList<Plan>();

        planAdapter = new PlanAdapter(planArrayList,PlannerActivity.this);

        plansView.setAdapter(planAdapter);

        fStore.collection("Plans").document(user.getUid()).collection("UsersPlans").orderBy("titleField", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    toast("Coś poszło nie tak, spróbuj ponownie później. Error: " + error);
                    return;
                }
                assert value != null;
                for (DocumentChange dc : value.getDocumentChanges()){
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            planArrayList.add(dc.getDocument().toObject(Plan.class));
                        }
                        planAdapter.notifyDataSetChanged();
                    }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addPlan:
                toAddPlan();
                break;
        }
    }

    private void goToHomeActivity(){
        Intent intent = new Intent(getApplicationContext(), TabsActivity.class);
        startActivity(intent);
    }

    private void toAddPlan() {
        Intent intent = new Intent(this, AddPlanActivity.class);
        startActivity(intent);
    }

    void toast(String toastText){
        Toast.makeText(PlannerActivity.this,toastText, Toast.LENGTH_LONG).show();
    }
}