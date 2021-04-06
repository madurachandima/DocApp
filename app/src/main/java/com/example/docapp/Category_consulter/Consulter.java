package com.example.docapp.Category_consulter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.docapp.Category_consulter.SingleConsulter.SingleConsulter;
import com.example.docapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Consulter extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ConsulterAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private ArrayList<ConsulterModel> holderArrayList;
    private ArrayList<String> conIds;

    private static final String TAG = "Consulter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulter);


        mRecyclerView = findViewById(R.id.id_cat_consulter_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        holderArrayList = new ArrayList<>();
        conIds = new ArrayList<>();


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

        final CollectionReference mCollectionReference = mFirestore.collection("consulterDetails");
        mCollectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {

                            Log.d(TAG, "onSuccess: " + snapshot);
                            conIds.add(snapshot.getId());
                            ConsulterModel holder = snapshot.toObject(ConsulterModel.class);
                            holderArrayList.add(holder);

                            Log.d(TAG, "onSuccess: holder " + holder.getConName());

                        }


                        mAdapter = new ConsulterAdapter(holderArrayList);
                        mRecyclerView.setAdapter(mAdapter);


                        mAdapter.setonItemClickListener(new ConsulterAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {

                                String conAvailableTime = holderArrayList.get(position).getConAvailableTime();
                                String conContactNumber = holderArrayList.get(position).getConContactNumber();
                                String conDescription = holderArrayList.get(position).getConDescription();
                                String conLocation = holderArrayList.get(position).getConLocation();
                                String conLng = holderArrayList.get(position).getConLongitude();
                                String conLat = holderArrayList.get(position).getConLatitude();
                                String conName = holderArrayList.get(position).getConName();
                                String imageURL = holderArrayList.get(position).getImageURL();

                                String hName = holderArrayList.get(position).getHospitalName();
                                String xP = holderArrayList.get(position).getExperienceYears();

                                String id = conIds.get(position);
                                Log.d(TAG, "onItemClick: " + id);

                                Intent intent = new Intent(Consulter.this, SingleConsulter.class);
                                intent.putExtra("NAME", conName);
                                intent.putExtra("LOCATION", conLocation);
                                intent.putExtra("TIME", conAvailableTime);
                                intent.putExtra("NUMBER", conContactNumber);
                                intent.putExtra("URL", imageURL);
                                intent.putExtra("DESC", conDescription);
                                intent.putExtra("CONID", id);
                                intent.putExtra("LAT", conLat);
                                intent.putExtra("LNG", conLng);

                                intent.putExtra("XP", xP);
                                intent.putExtra("Hname", hName);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);


                            }
                        });
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.id_actionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}