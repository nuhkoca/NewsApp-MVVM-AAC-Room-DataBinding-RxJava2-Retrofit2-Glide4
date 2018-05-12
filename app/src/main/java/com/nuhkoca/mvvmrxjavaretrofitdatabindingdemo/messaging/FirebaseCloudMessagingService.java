package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.messaging;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.NewsApp;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.Constants;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class FirebaseCloudMessagingService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Timber.d(refreshedToken);

        if (!TextUtils.isEmpty(refreshedToken)) {
            storeToFireStore(refreshedToken);
        }
    }

    private void storeToFireStore(final String token) {
        FirebaseFirestore db = NewsApp.provideFirestore();

        Map<String, Object> userToken = new HashMap<>();
        userToken.put(Constants.FIRESTORE_KEY, token);
        userToken.put(Constants.FIRESTORE_PUSH_KEY, 1);

        db.collection(Constants.FIRESTORE_COLLECTION_NAME)
                .add(userToken)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Timber.d("token saved successfully.");
                        Timber.d("Token: " + token);

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
                getSharedPreferences(Constants.TOKEN_PREF_NAME, MODE_PRIVATE).edit();

        editor.putString(Constants.DOC_ID_PREF, documentId);

        Timber.d("Document Id " + documentId);

        editor.apply();
    }
}