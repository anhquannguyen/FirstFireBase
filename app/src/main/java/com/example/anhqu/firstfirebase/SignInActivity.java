package com.example.anhqu.firstfirebase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.user_email)
    EditText userEmail;
    @BindView(R.id.user_Pw)
    EditText userPw;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private String email;
    private String pw;
    private static final String TAG = SignInActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    protected void authentication() {
        email = userEmail.getText().toString().trim();
        pw = userPw.getText().toString().trim();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pw)) {
            Toast.makeText(this, "Email or password must be fill!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sign In
        auth.signInWithEmailAndPassword(email, pw)
                .addOnCompleteListener(SignInActivity.this, task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        // Get current user from Auth order to create new user
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            String uId = user.getUid();
                            reference = database.getReference("users");

                            // Check if user has exists?
                            Query query = reference.child(uId);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists())
                                        Log.d(TAG, "onDataChange: user has exists!");
                                    else {

                                        // Create new user on Firebase database base on Auth
                                        reference.child(uId).child("email").setValue(user.getEmail());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    } else {
                        Toast.makeText(SignInActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick({R.id.btn_signin, R.id.btn_signup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_signin:
                authentication();
                break;
            case R.id.btn_signup:
                Intent i = new Intent(this, SignUpActivity.class);
                startActivity(i);
        }
    }

}
