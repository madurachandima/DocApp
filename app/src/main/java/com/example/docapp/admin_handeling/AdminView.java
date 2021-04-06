package com.example.docapp.admin_handeling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.docapp.Category_consulter.Consulter;
import com.example.docapp.Category_consulter.ConsulterAdapter;
import com.example.docapp.Category_consulter.ConsulterModel;
import com.example.docapp.Category_consulter.SingleConsulter.SingleConsulter;
import com.example.docapp.R;
import com.example.docapp.spalsh_screen.SplashScreen;
import com.example.docapp.user_handeling.UserLogin;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminView extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private AdminAdapter mAdminAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<AdminModel> mModelArrayList;
    private ArrayList<String> mArrayList;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private static final String TAG = "AdminView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        mRecyclerView = findViewById(R.id.id_admin_R_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mModelArrayList = new ArrayList<>();
        mArrayList = new ArrayList<>();

        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.admin_logut, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logOut:
                Log.d(TAG, "onOptionsItemSelected: work");
                mAuth.signOut();

                Intent splashIntent = new Intent(AdminView.this, UserLogin.class);
                splashIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(splashIntent);
                finish();

//                startActivity(new Intent(AdminView.this, UserLogin.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {

        final CollectionReference mCollectionReference = mFirestore.collection("ConsultantRequest");
        mCollectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {

                            Log.d(TAG, "onSuccess: " + snapshot.getId());
                            mArrayList.add(snapshot.getId());
                            AdminModel holder = snapshot.toObject(AdminModel.class);
                            mModelArrayList.add(holder);


                        }
                        mAdminAdapter = new AdminAdapter(mModelArrayList);
                        mRecyclerView.setAdapter(mAdminAdapter);
                        mAdminAdapter.setonItemClickListener(new AdminAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {

                                Intent intent = new Intent(AdminView.this, ApproveCon.class);
//                                String name = mModelArrayList.get(position).getConName();
                                Log.d(TAG, "onItemClick: " + mArrayList.get(position));
                                mArrayList.get(position);
                                intent.putExtra("NAME", mModelArrayList.get(position).getConName());
                                intent.putExtra("TIME", mModelArrayList.get(position).getConAvailableTime());
                                intent.putExtra("NUMBER", mModelArrayList.get(position).getConContactNumber());
                                intent.putExtra("DESC", mModelArrayList.get(position).getConDescription());
                                intent.putExtra("EMAIL", mModelArrayList.get(position).getConEmail());
                                intent.putExtra("LOCATION", mModelArrayList.get(position).getConLocation());
                                intent.putExtra("XP", mModelArrayList.get(position).getExperienceYears());
                                intent.putExtra("HNAME", mModelArrayList.get(position).getHospitalName());
                                intent.putExtra("TYPE", mModelArrayList.get(position).getType());
                                intent.putExtra("ID", mArrayList.get(position));

                                startActivity(intent);

                            }
                        });

                    }
                });
    }
}