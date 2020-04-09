package com.kapok.brianramirez.kapok;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.support.constraint.Constraints.TAG;

public class RegisterActivity extends AppCompatActivity implements TextWatcher {
    EditText emailField;
    EditText passwordField;
    EditText confirmPassField;
    Button registerBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Theme set
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_register);

        //getting user data
        mAuth = Database.mAuth;

        //Setting activity vies
        emailField = (EditText) findViewById(R.id.email_text_field);
        passwordField = (EditText) findViewById(R.id.password_text_field);
        confirmPassField = (EditText) findViewById(R.id.confirm_pass_text_field);
        registerBtn = (Button) findViewById(R.id.register_btn);

        passwordField.addTextChangedListener(this);
        //Checks password strength and email validity
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                String confirmPass = confirmPassField.getText().toString();
                    int count =2;

                    if (!emailisValid(email)) {
                        Toast pass = Toast.makeText(RegisterActivity.this, "Invalid Email", Toast.LENGTH_SHORT);
                        pass.show();
                        count--;
                    }
                PasswordStrength str = PasswordStrength.calculateStrength(password);

                if (str.getText(RegisterActivity.this).equals("Weak")) {
                    Toast pass = Toast.makeText(RegisterActivity.this, "Password strength must be medium", Toast.LENGTH_SHORT);
                    pass.show();
                    count--;
                }

                    if (!passisValid(password,confirmPass)) {
                        //popup message
                        Toast pass = Toast.makeText(RegisterActivity.this, "Passwords don't match!", Toast.LENGTH_SHORT);
                        pass.show();
                        count--;
                    }


                    if (count==2)
                    {
                        //insert details in the database
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        // Sign in success
                                    }

                                    else {
                                        mAuth.getCurrentUser().sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(RegisterActivity.this, "Email Sent",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        Intent goToProfileSetupIntent = new Intent(RegisterActivity.this, userVerifyActivity.class);
                                        startActivity(goToProfileSetupIntent);
                                    }
                                }
                            });




                    } else {
                    //TODO: show error saying pass don't match or email is too short
                }
            }
        });
    }

    public void goToProfileSetup()
    {
        Intent goToProfileSetupIntent = new Intent(this, ProfileSetupActivity.class);
        startActivity(goToProfileSetupIntent);
    }

    @Override
    public void afterTextChanged(Editable s)
    {

    }

    @Override
    public void beforeTextChanged(
            CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        updatePasswordStrengthView(s.toString());

    }

    private void updatePasswordStrengthView(String pass1) {

        ProgressBar progressBar1 = (ProgressBar) findViewById(R.id.progress);
        TextView strengthView = (TextView) findViewById(R.id.password_strength);


        if (TextView.VISIBLE != strengthView.getVisibility())
            return;

        if (pass1.isEmpty()) {
            strengthView.setText("");
            progressBar1.setProgress(0);
            return;
        }

        PasswordStrength str = PasswordStrength.calculateStrength(pass1);

        strengthView.setText(str.getText(this));
        strengthView.setTextColor(str.getColor());

        progressBar1.getProgressDrawable().setColorFilter(str.getColor(), android.graphics.PorterDuff.Mode.SRC_IN);
        if (str.getText(this).equals("Weak"))
        {
            progressBar1.setProgress(25);
        }
        else if (str.getText(this).equals("Medium"))
        {
            progressBar1.setProgress(50);
        }
        else if (str.getText(this).equals("Strong"))
        {
            progressBar1.setProgress(75);
        }
        else
        {
            progressBar1.setProgress(100);
        }
    }

    public boolean emailisValid(String emailstr) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(emailstr);

        if (emailstr.isEmpty()) {
            return false;

        } else if (!matcher.matches()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean passisValid(String password, String confirmPass) {
        if (password.equals(confirmPass))
            return true;

        else
            return false;
    }
}