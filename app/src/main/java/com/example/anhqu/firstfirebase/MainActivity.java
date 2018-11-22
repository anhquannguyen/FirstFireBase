package com.example.anhqu.firstfirebase;

import android.os.Bundle;
import android.util.Log;

import com.example.anhqu.firstfirebase.network.ApiClient;
import com.example.anhqu.firstfirebase.network.FCMRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @OnClick(R.id.btn_push)
    public void onViewClicked() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, instanceIdResult -> {
            String regToken = instanceIdResult.getToken();
            postToken(regToken);
        });
    }

    protected void postToken(String token) {
        FCMRequest request = ApiClient.getRetrofit().create(FCMRequest.class);
        Call<String> call = request.postToken(token);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful())
                    Log.d(TAG, "onResponse:" + response.body());
                else {
                    Log.d(TAG, "onResponse:" + response.raw());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });
    }
}
