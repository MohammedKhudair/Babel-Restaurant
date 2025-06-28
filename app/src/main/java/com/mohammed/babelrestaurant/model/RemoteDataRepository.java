package com.mohammed.babelrestaurant.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mohammed.babelrestaurant.data.entity.MealListItem;
import com.mohammed.babelrestaurant.data.entity.SnackItem;
import com.mohammed.babelrestaurant.data.entity.User;

import java.util.ArrayList;
import java.util.List;

public class RemoteDataRepository {
    private MutableLiveData<List<MealListItem>> mealListMLD;
    private MutableLiveData<List<SnackItem>> snacksMLD;
    private final List<MealListItem> itemList;
    private final FirebaseFirestore db;
    private MealListItem mealItem;
    private final String userId;
    private User user;


    public RemoteDataRepository() {
        mealListMLD = new MutableLiveData<>();
        snacksMLD = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
        itemList = new ArrayList<>();
        mealItem = new MealListItem();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        user = new User();
    }

    public LiveData<List<MealListItem>> getAllMealList() {
        db.collection("MealList").addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w("TAG", "Listen failed." + error.getMessage());
                return;
            }

            db.collection("users").
                    document(userId).
                    get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            DocumentSnapshot userDoc = task.getResult();
                            user = userDoc.toObject(User.class);
                        }

                assert value != null;
                itemList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    mealItem = doc.toObject(MealListItem.class);
                    mealItem.setDocumentId(doc.getId());
                if(user != null){
                    mealItem.setLike(user.getBookmarks().contains(doc.getId()));
                }
                    itemList.add(mealItem);
                }
                mealListMLD.postValue(itemList);
            });

        });
        return mealListMLD;
    }


    public LiveData<List<MealListItem>> getMealTypeList(String type) {
        db.collection("MealList")
                .whereEqualTo("type", type).addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w("TAG", "Listen failed." + error.getMessage());
                return;
            }

            db.collection("users").
                    document(userId).
                    get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DocumentSnapshot userDoc = task.getResult();
                    user = userDoc.toObject(User.class);
                }

                assert value != null;
                itemList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    mealItem = doc.toObject(MealListItem.class);
                    mealItem.setDocumentId(doc.getId());
                    if(user != null){
                        mealItem.setLike(user.getBookmarks().contains(doc.getId()));
                    }
                    itemList.add(mealItem);
                }
                mealListMLD.postValue(itemList);
            });
        });
        return mealListMLD;
    }

    public LiveData<List<SnackItem>> getStartersLis() {
        List<SnackItem> snackItems = new ArrayList<>();

        db.collection("Starters").addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w("TAG", "Listen failed." + error.getMessage());
                return;
            }

            assert value != null;
            for (QueryDocumentSnapshot doc : value) {
                snackItems.add(doc.toObject(SnackItem.class));
            }
            snacksMLD.postValue(snackItems);

        });
        return snacksMLD;
    }


}
