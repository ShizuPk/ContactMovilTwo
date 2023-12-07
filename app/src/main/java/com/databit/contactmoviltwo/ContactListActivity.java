package com.databit.contactmoviltwo;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.internal.FlowLayout; // For VectorDrawableCompat


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.databit.contactmoviltwo.Contact;
import com.databit.contactmoviltwo.ContactAdapter;
import com.databit.contactmoviltwo.R;
import com.databit.contactmoviltwo.View.AddContactActivity;
import com.databit.contactmoviltwo.View.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class ContactListActivity extends AppCompatActivity {
    private static final int ADD_CONTACT_REQUEST = 1;

    private List<Contact> contactList;
    private ContactAdapter adapter;
    private EditText txtSearchUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        txtSearchUser = findViewById(R.id.txtSearchUser);

        contactList = getContactList();
        loadAllUsers();
        configureRecyclerView();

        ImageButton btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(view -> searchUser());

        ImageButton btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(ContactListActivity.this, AddContactActivity.class);
            startActivityForResult(intent, ADD_CONTACT_REQUEST);
        });


        ImageButton btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(view -> goToEditProfile());

        configureProfile();
    }

    private void configureRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rvContactList);
        adapter = new ContactAdapter(contactList, contact -> openContactProfile(contact));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadAllUsers() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Contact> contactList = new ArrayList<>();
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        Contact contact = userSnapshot.getValue(Contact.class);

                        if (contact != null && currentUser != null
                                && !TextUtils.equals(contact.getCorreo(), currentUser.getEmail())) {
                            contactList.add(contact);
                        }
                    }

                    adapter.updateList(contactList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ContactListActivity.this, "Error loading users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchUser() {
        String nameEmailSearched = txtSearchUser.getText().toString().trim().toLowerCase();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");

        String[] nameEmailParts = nameEmailSearched.split("\\s+");
        String nameSearched = nameEmailParts[0];

        Query query;
        if (nameEmailParts.length > 1) {
            String emailSearched = nameEmailParts[1];
            query = userRef.orderByChild("nameEmail").startAt(nameEmailSearched).endAt(nameEmailSearched + "\uf8ff");
        } else {
            query = userRef.orderByChild("name").startAt(nameSearched).endAt(nameSearched + "\uf8ff");
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Contact> results = new ArrayList<>();
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        Contact contact = userSnapshot.getValue(Contact.class);
                        if (contact != null) {
                            results.add(contact);
                        }
                    }


                    adapter.updateList(results);
                } else {
                    // Si no se encuentran resultados, restablece la lista original
                    adapter.updateList(contactList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ContactListActivity.this, "Error searching users", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addUserFromSearch() {
        int selectedPosition = adapter.getSelectedPosition();
        if (selectedPosition != RecyclerView.NO_POSITION) {
            Contact newContact = adapter.getItem(selectedPosition);

            if (!contactList.contains(newContact)) {
                addContact(newContact);
                txtSearchUser.setText("");
                Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "The user is already in the list", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Select a user from the list", Toast.LENGTH_SHORT).show();
        }
    }

    private void addContact(Contact newContact) {
        contactList.add(newContact);
        adapter.updateList(contactList);
    }

    private void goToEditProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private List<Contact> getContactList() {
        List<Contact> contacts = new ArrayList<>();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        Contact contact = userSnapshot.getValue(Contact.class);
                        if (contact != null) {
                            contacts.add(contact);
                        }
                    }
                    adapter.updateList(contacts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ContactListActivity.this, "Error loading users", Toast.LENGTH_SHORT).show();
            }
        });

        return contacts;
    }

    private void openContactProfile(Contact contact) {
        // Implement this function to open the profile of the contact
        // It can be a new activity or fragment that shows contact details
        // You can also implement the option to add new contacts here
    }

    private void configureProfile() {
        ImageButton btnProfile = findViewById(R.id.btnProfile);

        String userEmail = getUserEmail();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");
        userRef.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String profileImageUrl = userSnapshot.child("profileImageUrl").getValue(String.class);

                        Log.d("ContactListActivity", "Profile image URL: " + profileImageUrl);

                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            Glide.with(ContactListActivity.this).load(profileImageUrl).into(btnProfile);
                        } else {
                            btnProfile.setImageResource(R.drawable.baseline_person_outline_black_24);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ContactListActivity.this, "Error getting user data", Toast.LENGTH_SHORT).show();
            }
        });

        btnProfile.setOnClickListener(v -> {
            Intent editProfileIntent = new Intent(ContactListActivity.this, ProfileActivity.class);
            startActivity(editProfileIntent);
        });
    }

    private String getUserEmail() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            return currentUser.getEmail();
        } else {
            return null;
        }
    }
}
