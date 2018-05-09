package com.kirklandtech.toiletries;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class MainActivity extends AppCompatActivity {
    private EditText editTextEnterEmail;
    private EditText editTextEnterPassword;
    private EditText editTextName;
    private Button buttonSignUp;
    private Button buttonLogin;
    private TextView textViewSignMeUp;
    private final String TAG = MainActivity.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Backendless.initApp( this,
                getString(R.string.backendless_app_id),
                getString(R.string.backendless_api_key));

        editTextEnterEmail = findViewById(R.id.enter_email);
        editTextEnterPassword = findViewById(R.id.enter_password);
        editTextName = findViewById(R.id.enter_name);
        buttonSignUp = findViewById(R.id.signup_button);
        buttonLogin = findViewById(R.id.login_button);
        textViewSignMeUp =
                findViewById(R.id.sign_up_text);

        textViewSignMeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonLogin.getVisibility() == View.VISIBLE) {
                    textViewSignMeUp.setText(R.string.signup_button_label_cancel);
                    buttonLogin.setVisibility(View.GONE);
                    buttonSignUp.setVisibility(View.VISIBLE);
                    editTextName.setVisibility(View.VISIBLE);
                } else {
                    textViewSignMeUp.setText(R.string.signup_button_label);
                    buttonLogin.setVisibility(View.VISIBLE);
                    buttonSignUp.setVisibility(View.GONE);
                    editTextName.setVisibility(View.GONE);
                }
            }
        });

        MySignUpOnClickListener buttonListener = new MySignUpOnClickListener();
        buttonSignUp.setOnClickListener(buttonListener);

        MyLoginOnClickListener loginButtonLister = new MyLoginOnClickListener();
        buttonLogin.setOnClickListener(loginButtonLister);
    }

    private class MySignUpOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String userEmail = editTextEnterEmail.getText().toString();
            String password = editTextEnterPassword.getText().toString();
            String name = editTextName.getText().toString();

            userEmail = userEmail.trim();
            password = password.trim();
            name = name.trim();

            if (!userEmail.isEmpty() &&!password.isEmpty() && !name.isEmpty()) {
                if(userEmail.contains("@")) {
                    /* register the user in Backendless */
                    BackendlessUser user = new BackendlessUser();
                    user.setEmail(userEmail);
                    user.setPassword(password);
                    user.setProperty("name", name);

                    Backendless.UserService.register(user,
                            new AsyncCallback<BackendlessUser>() {
                                @Override
                                public void handleResponse(BackendlessUser backendlessUser) {
                                    Log.i(TAG, "Registration successful for " +
                                            backendlessUser.getEmail());
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Log.i(TAG, "Registration failed: " + fault.getMessage());
                                    warnUser(
                                            getResources().getString(R.string.authentication_error_title),
                                            fault.getMessage());
                                }
                            });
                }
                else {
                    warnUser(
                            getString(R.string.authentication_error_title),
                            getString(R.string.invalid_email_message));
                }
            }
            else {
                /* warn the user of the problem */
                warnUser(
                        getResources().getString(R.string.authentication_error_title),
                        getResources().getString(R.string.empty_field_signup_error));
            }
        }
    }

    private class MyLoginOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String userEmail = editTextEnterEmail.getText().toString();
            String password = editTextEnterPassword.getText().toString();

            userEmail = userEmail.trim();
            password = password.trim();

            if (!userEmail.isEmpty() &&!password.isEmpty()) {
                final ProgressDialog pDialog = ProgressDialog.show(MainActivity.this,
                        "Please Wait!",
                        "Logging in...",
                        true);

                Backendless.UserService.login(userEmail, password,
                        new AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse( BackendlessUser backendlessUser ) {
                                Log.i(TAG, "Login successful for " +
                                        backendlessUser.getEmail());

                                Intent i = new Intent(MainActivity.this, LoggedInActivity.class);
                                startActivity(i);
                            }
                            @Override
                            public void handleFault( BackendlessFault fault ) {
                                Log.i(TAG, "Login failed: " + fault.getMessage());

                                pDialog.dismiss();
                                warnUser(
                                        getResources().getString(R.string.authentication_error_title),
                                        fault.getMessage());
                            }
                        } );
            }
            else {
                /* warn the user of the problem */
                warnUser(
                        getResources().getString(R.string.authentication_error_title),
                        getResources().getString(R.string.empty_field_signup_error));
            }
        }
    }

    private void warnUser(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}

