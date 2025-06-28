package com.mohammed.babelrestaurant.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mohammed.babelrestaurant.callback.UserAddress;
import com.mohammed.babelrestaurant.data.entity.MealListItem;
import com.mohammed.babelrestaurant.data.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final String userId;
    private final FirebaseFirestore db;
    private final List<MealListItem> itemList;
    private final MutableLiveData<List<MealListItem>> mealListMLD;
    private final MutableLiveData<Task<DocumentSnapshot>> taskMLD;


    public UserRepository() {
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        itemList = new ArrayList<>();
        mealListMLD = new MutableLiveData<>();
        taskMLD = new MutableLiveData<>();
    }

   public LiveData<List<MealListItem>> getFavorites() {
       db.collection("favoriteMeals").whereEqualTo("userId", userId).
               addSnapshotListener((value, error) -> {
                   if (error != null) {
                       Log.w("TAG", "Listen failed." + error.getMessage());
                       return;
                   }
                   assert value != null;
                   itemList.clear();
                   for (QueryDocumentSnapshot doc : value) {
                       MealListItem mealItem = doc.toObject(MealListItem.class);
                       mealItem.setLike(true);
                       itemList.add(mealItem);
                   }
                   mealListMLD.postValue(itemList);
               });
       return mealListMLD;
   }

   public void getUserAddress(UserAddress userAddress){
       DocumentReference userDoc = db.collection("users").document(userId);
       userDoc.get().addOnCompleteListener(task -> {
               DocumentSnapshot document = task.getResult();
               if (document.exists()) {
                   User user = task.getResult().toObject(User.class);
                   userAddress.address(user);
               } else
                   userAddress.address(new User());
       });
   }

    public LiveData<String> addItemToFavorites(MealListItem item) {
        return AddAndRemoveFavorites.getInstance().addItemToFavorites(item);
    }

}
