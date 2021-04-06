package com.example.docapp.Category_consulter.consulter_dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.docapp.Category_consulter.SingleConsulter.SingleConsulter;
import com.example.docapp.Category_consulter.SingleConsulter.SingleConsulterAdapter;
import com.example.docapp.Category_consulter.SingleConsulter.SingleConsulterModel;
import com.example.docapp.Category_consulter.consulter_dashboard.feedback_reply.FeedbackReplyAdapter;
import com.example.docapp.Category_consulter.consulter_dashboard.feedback_reply.FeedbackReplyModel;
import com.example.docapp.R;
import com.example.docapp.category_music.playList.PlayListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FragmentReplyFeedback extends Fragment {

    RecyclerView recyclerView;
    FeedbackReplyAdapter feedbackReplyAdapter;
    RecyclerView.LayoutManager layoutManager;

    private ArrayList<FeedbackReplyModel> arrayList = new ArrayList<>();
    private ArrayList<String> newArrayList = new ArrayList<>();
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private String consultId;

    private MaterialEditText mFeedback;

    private String userName;
    private String userId;
    private String date;
    private String feedback;

    private static final String TAG = "FragmentReplyFeedback";

    public FragmentReplyFeedback() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_reply_feed_back, container, false);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        consultId = auth.getUid();

        recyclerView = view.findViewById(R.id.id_userFeedBack_Reply_RecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

//        arrayList = new ArrayList<>();

        //     retrieveData(arrayList,firestore);
//TODO get User feedback
        CollectionReference collectionReference = firestore.collection("consulterFeedback")
                .document(consultId).collection("feedback");
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            newArrayList.add(snapshot.getId());
                            Log.d(TAG, "onSuccess: " + snapshot);
                            FeedbackReplyModel model = snapshot.toObject(FeedbackReplyModel.class);
                            arrayList.add(model);

                        }

//
//                        try {
//
//                            CollectionReference collection = firestore.collection("consulterFeedback")
//                                    .document(consultId).collection("feedbacksReply");
//                            collection.get()
//                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                        @Override
//                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
////                                        ArrayList<FeedbackReplyModel> replyModels = new ArrayList<>();
////                                        replyModels
//
//                                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
//                                                Log.d(TAG, "onSuccess: " + snapshot);
//                                                FeedbackReplyModel model = snapshot.toObject(FeedbackReplyModel.class);
//                                                newArrayList.add(model);
//
//                                            }
//                                        }
//                                    });
//                        } catch (Exception e) {
//
//                        }

                        //    arrayList = new ArrayList<>(newArrayList);
//                        Log.d(TAG, "onSuccess: " + newArrayList.size());
//                        arrayList.addAll(newArrayList);
                        feedbackReplyAdapter = new FeedbackReplyAdapter(arrayList);
                        recyclerView.setAdapter(feedbackReplyAdapter);

                        feedbackReplyAdapter.setonItemClickListener(new FeedbackReplyAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Log.d(TAG, "onItemClick: Work " + position);
                                showDialog(position);
                            }
                        });

                        feedbackReplyAdapter.setLongClickListener(new FeedbackReplyAdapter.OnItemLongClickListener() {
                            @Override
                            public void onItemLongClick(int position) {
                                Log.d(TAG, "onItemLongClick: Work " + arrayList.get(position).getFeedbackReply());
                                conformDelete(position);

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e);
            }
        });


        return view;
    }

//    private ArrayList retrieveData(final ArrayList<FeedbackReplyModel> arrayList, FirebaseFirestore firestore) {
//
//        //    consulterFeedback
//        CollectionReference collectionReference = firestore.collection("consulterFeedback")
//                .document(consultId).collection("feedback");
//        collectionReference.get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
//                            Log.d(TAG, "onSuccess: " + snapshot);
//                            FeedbackReplyModel model = snapshot.toObject(FeedbackReplyModel.class);
//                            arrayList.add(model);
//
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "onFailure: " + e);
//            }
//        });
//
//        return arrayList;
//    }

    private void showDialog(final int pos) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Reply..");
        dialog.setMessage("Provide your Reply.");


        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View reglayout = inflater.inflate(R.layout.send_feedback, null, false);

        mFeedback = reglayout.findViewById(R.id.id_feedback_text);

        dialog.setView(reglayout);
        dialog.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(mFeedback.getText().toString())) {
                    Toast.makeText(getContext(), "Please type your Reply...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isEmpty(mFeedback.getText().toString())) {
                    String feedback = mFeedback.getText().toString();

                    sendFeedback(feedback, pos);
                }


            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void conformDelete(final int pos) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Delete..");
        dialog.setMessage("Delete your Reply.");


        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFeedbackReply(pos);
            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void deleteFeedbackReply(int pos) {
        String replyId = newArrayList.get(pos);
        Log.d(TAG, "deleteFeedbackReply ID : " + replyId);

        DocumentReference reference = firestore.collection("consulterFeedback")
                .document(consultId).collection("feedback").document(replyId);

        Map<String, Object> updates = new HashMap<>();
        updates.put("feedbackReply", FieldValue.delete());

        reference.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getContext(), "Delete Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendFeedback(final String feedbackReply, int position) {

        final String id = newArrayList.get(position);//Get User ID
        //Get Feedback user data
        Log.d(TAG, "sendFeedback: " + id);
        DocumentReference reference = firestore.collection("consulterFeedback")
                .document(consultId).collection("feedback").document(id);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();

                userName = snapshot.getString("userName");
                userId = snapshot.getString("userId");
                date = snapshot.getString("date");
                feedback = snapshot.getString("feedback");
                Log.d(TAG, "onComplete: " + userName);

                //Send Feedback reply

                DocumentReference document = firestore.collection("consulterFeedback").document(auth.getUid())
                        .collection("feedback").document(id);

                Map<String, Object> userFeedback = new HashMap<>();

                userFeedback.put("userName", userName);
                userFeedback.put("userId", userId);
                userFeedback.put("date", date);
                userFeedback.put("feedback", feedback);
                userFeedback.put("feedbackReply", feedbackReply);

                Log.d(TAG, "sendFeedback: " + userFeedback);

                document.set(userFeedback)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Submitted your feedback.  ", Toast.LENGTH_SHORT).show();

//                        mConsulterFeedbackArrayList.add(new SingleConsulterModel("you", mDate, feedback, mUid));
//
//                        mConsulterAdapter = new SingleConsulterAdapter(mConsulterFeedbackArrayList);
//                        mConsulterAdapter.notifyDataSetChanged();
//                        mRecyclerView.setAdapter(mConsulterAdapter);


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Fail to submit " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}