package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.messaging;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.NewsApp;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.Constants;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

import static android.content.Context.MODE_PRIVATE;

public class FirestoreDatabaseHandler {

    private String mToken;
    private int mIsNotified;
    private Context mContext;

    public FirestoreDatabaseHandler(String mToken, int mIsNotified, Context mContext) {
        this.mToken = mToken;
        this.mIsNotified = mIsNotified;
        this.mContext = mContext;
    }

    public void checkAndSaveToken() {
        FirebaseFirestore db = NewsApp.provideFirestore();

        if (!TextUtils.isEmpty(getDocumentId())) {
            db.collection(Constants.FIRESTORE_COLLECTION_NAME)
                    .document(getDocumentId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot documentSnapshot = task.getResult();

                            if (documentSnapshot.exists()) {
                                Timber.d("document exists");
                            } else {
                                storeToFireStore();
                                Timber.d("new data added");
                            }
                        }
                    });
        } else {
            storeToFireStore();
        }
    }

    private void storeToFireStore() {
        FirebaseFirestore db = NewsApp.provideFirestore();

        Map<String, Object> userToken = new HashMap<>();
        userToken.put(Constants.FIRESTORE_KEY, mToken);
        userToken.put(Constants.FIRESTORE_PUSH_KEY, mIsNotified);

        db.collection(Constants.FIRESTORE_COLLECTION_NAME)
                .add(userToken)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Timber.d("token saved successfully.");
                        Timber.d("Token: " + mToken);

                        addDocumentIdToSharedPreference(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Timber.d(e.getMessage());
                    }
                });
    }

    private void addDocumentIdToSharedPreference(String documentId) {
        SharedPreferences.Editor editor =
                mContext.getSharedPreferences(Constants.TOKEN_PREF_NAME, MODE_PRIVATE).edit();

        editor.putString(Constants.DOC_ID_PREF, documentId);

        Timber.d("Document Id " + documentId);

        editor.apply();
    }

    private String getDocumentId() {
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.TOKEN_PREF_NAME, MODE_PRIVATE);
        return prefs.getString(Constants.DOC_ID_PREF, "");
    }
}