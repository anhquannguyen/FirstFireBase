package com.example.anhqu.firstfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

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
    private static final String TAG = SignInActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
    }

    protected void authentication() {
        String email = userEmail.getText().toString();
        String pw = userPw.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pw)) {
            Toast.makeText(this, "Email or password must be fill!", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, pw)
                .addOnCompleteListener(SignInActivity.this, task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignInActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick(R.id.btn_signin)
    public void onViewClicked() {
        authentication();
    }
}
