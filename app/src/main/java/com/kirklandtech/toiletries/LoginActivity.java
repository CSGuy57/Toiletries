// LoginActivity.java
package com.kirklandtech.toiletries;

import android.support.v7.app.AppCompatActivity;    //
import android.os.Bundle;                           //
import android.view.View;                           //
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kirklandtech.toiletries.R;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextName;
    private Button buttonSignUp;
    private Button buttonLogin;
    private TextView textViewSignMeUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextName = findViewById(R.id.enter_name);
        buttonSignUp = findViewById(R.id.signup_button);
        buttonLogin = findViewById(R.id.login_button);
        textViewSignMeUp = findViewById(R.id.sign_up_text);

        textViewSignMeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonLogin.getVisibility() == View.VISIBLE) {
                    buttonLogin.setVisibility(View.GONE);
                    buttonSignUp.setVisibility(View.VISIBLE);
                    editTextName.setVisibility(View.VISIBLE);
                    textViewSignMeUp.setText("Back to Log In");
                } else {
                    buttonLogin.setVisibility(View.VISIBLE);
                    buttonSignUp.setVisibility(View.GONE);
                    editTextName.setVisibility(View.GONE);
                    textViewSignMeUp.setText("Sign Up");
                }
            }
        });
    }
}
