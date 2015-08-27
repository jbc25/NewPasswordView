package com.triskelapps.newpasswordviewsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.triskelapps.newpasswordview.NewPasswordDialog;
import com.triskelapps.newpasswordview.NewPasswordView;

public class MainActivity extends AppCompatActivity implements NewPasswordView.OnPasswordCheckListener {

    private NewPasswordView viewNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewNewPassword = (NewPasswordView) findViewById(R.id.view_new_pass);
        viewNewPassword.digestPassword("SHA-1");
        viewNewPassword.setMinimumLenght(4);
        viewNewPassword.setOnPasswordCheckListener(this);



        findViewById(R.id.btn_show_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        NewPasswordDialog dialog = NewPasswordDialog.newInstance();
        dialog.configure(getString(R.string.enter_new_password), new NewPasswordDialog.NewPasswordDialogListener() {
            @Override
            public void onAccept(String text) {

                succeed();
            }
        });

        dialog.show(getSupportFragmentManager(), null);
    }


    @Override
    public void onPasswordCorrect(String password) {

        succeed();
    }

    @Override
    public void onPasswordError(String reason) {

        // Handled internally
    }


    private void succeed() {

        Toast.makeText(getBaseContext(), R.string.new_pass_correct, Toast.LENGTH_LONG).show();
    }
}
