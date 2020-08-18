package com.nextgenlabs.mxclone.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.nextgenlabs.mxclone.MainActivity;
import com.nextgenlabs.mxclone.Model.InitialTableModel;
import com.nextgenlabs.mxclone.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class VerifyLoginActivity extends AppCompatActivity {
    private static final String TAG = "VERIFY_LOGIN_ACTIVITY" ;
    private String code;
    private EditText codeEt;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Button button;
    final private Context context = this;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.backgroundBar));
        window.setNavigationBarColor(ContextCompat.getColor(this,R.color.backgroundBar));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_login);
        Intent intent = getIntent();
        String phoneNumber = intent.getStringExtra("phoneNumber");
        Log.i(TAG,"not empty " + phoneNumber);
        sendVerifyCode(phoneNumber);
        button = findViewById(R.id.verifyOTPBtn);
        codeEt = findViewById(R.id.otp_edit);
        code = codeEt.getText().toString();

    }
    public void sendVerifyCode(String phoneNumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,                 // Timeout duration
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            final String systemCode = s;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VerifyCode(systemCode,code);
                }
            });
        }

        @Override
        public void onVerificationCompleted(@NonNull final PhoneAuthCredential phoneAuthCredential) {
            String phoneCode = phoneAuthCredential.getSmsCode();
            if (phoneCode != null){
                codeEt.setText(phoneCode);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }
                });
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyLoginActivity.this,"Wrong OTP",Toast.LENGTH_SHORT).show();
            Log.w(TAG, "onVerificationFailed", e);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Toast.makeText(VerifyLoginActivity.this,"Wrong OTP",Toast.LENGTH_SHORT).show();
                // ...
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
            }

            Intent intent = new Intent(VerifyLoginActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    };
    public void VerifyCode(String verificationId,String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            setInitialTable(user);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(VerifyLoginActivity.this,"Wrong OTP",Toast.LENGTH_SHORT).show();
                                codeEt.setText(null);
                            }
                        }
                    }
                });
    }

    public final void setInitialTable(final FirebaseUser user){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("userProfiles").document(user.getPhoneNumber() + "_");
        db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(doc);
                if(!snapshot.exists()){
                    Map<String, Object> map = new HashMap<>();

                    map.put("phone",user.getPhoneNumber());
                    map.put("profileSet",false);

                    db.collection("userProfiles").document(user.getPhoneNumber() + "_").set(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    startActivity(new Intent(VerifyLoginActivity.this, SetDetails.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
                else {
                    startActivity(new Intent(VerifyLoginActivity.this,MainActivity.class));
                }
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}