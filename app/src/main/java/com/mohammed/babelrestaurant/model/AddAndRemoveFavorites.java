package com.mohammed.babelrestaurant.model;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mohammed.babelrestaurant.data.entity.FavoriteItem;
import com.mohammed.babelrestaurant.data.entity.MealListItem;
import com.mohammed.babelrestaurant.data.entity.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddAndRemoveFavorites {
    private final FirebaseFirestore db;
    private final MutableLiveData<String> statusText;
    private final String userId;

    private AddAndRemoveFavorites() {
        db = FirebaseFirestore.getInstance();
        statusText = new MutableLiveData<>();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static AddAndRemoveFavorites getInstance() {
        return new AddAndRemoveFavorites();
    }

    // Check if the favorite doc exist or not, we delete or add the doc depends on the state;
    public MutableLiveData<String> addItemToFavorites(MealListItem item) {
        db.collection("users").document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                if (doc.exists()) {
                    User user;
                    user = doc.toObject(User.class);

                    assert user != null;
                    if (user.getBookmarks().contains(item.getDocumentId())) {
                        removeFavItem(item);
                    } else {
                        addFavItem(item);
                    }

                } else {
                    addNewUserDocumentFavorite(item);
                }
            } else {
                statusText.postValue(task.getException().getMessage());
            }
        });
        return statusText;
    }

    private void addFavItem(MealListItem item) {
        // Add the new document favorite.
        FavoriteItem favoriteItem = new FavoriteItem(item, userId);
        db.collection("favoriteMeals").add(favoriteItem);

        // Update the user document with the new docId, we list it in array bookmarks.
        DocumentReference docRef = db.collection("users").document(userId);
        docRef.update("bookmarks", FieldValue.arrayUnion(item.getDocumentId())).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                statusText.postValue("Favorite added");
            } else {
                statusText.postValue("Failed to add");
            }
        });
    }

    private void removeFavItem(MealListItem item) {
        // Remove the document favorite.
        db.collection("favoriteMeals").
                whereEqualTo("userId", userId).
                whereEqualTo("documentId", item.getDocumentId()).get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                db.collection("favoriteMeals").document(document.getId()).delete();
            }
        });

        // Remove the user document id from the list in bookmarks.
        DocumentReference docRef = db.collection("users").document(userId);
        docRef.update("bookmarks", FieldValue.arrayRemove(item.getDocumentId()))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        statusText.postValue("Removed");
                    } else {
                        statusText.postValue("Failed to removed");
                    }
                });
    }


    // Add new user document if there is no user favorites yet.
    private void addNewUserDocumentFavorite(MealListItem item) {
        FavoriteItem favoriteItem = new FavoriteItem(item, userId);
        db.collection("favoriteMeals").add(favoriteItem);

        Map<String, Object> docData = new HashMap<>();
        docData.put("bookmarks", Collections.singletonList(item.getDocumentId()));

        db.collection("users").document(userId).set(docData).
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        statusText.postValue("Favorite added");
                    } else {
                        statusText.postValue("Failed to add");
                    }
                });
    }
}
