package com.triskelapps.newpasswordview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;


/**
 * Created by julio on 5/05/15.
 */
public class NewPasswordDialog extends DialogFragment implements NewPasswordView.OnPasswordCheckListener {

    private final String TAG = "NewPasswordDialog";

    private NewPasswordDialogListener mListener;
    private String mTitle;
    private NewPasswordView viewNewPassword;

    public static NewPasswordDialog newInstance() {

        NewPasswordDialog dialog = new NewPasswordDialog();

        return dialog;
    }

    public void configure(String title, NewPasswordDialogListener listener) {

        mTitle = title;
        mListener = listener;
    }


    public interface NewPasswordDialogListener {
        public void onAccept(String text);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        final NewPasswordView viewNewPassword = new NewPasswordView(getActivity());
        viewNewPassword.setButtonCheckVisible(false);
        viewNewPassword.digestPassword("MD5");
        viewNewPassword.setOnPasswordCheckListener(this);

        final AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
        ab.setTitle(mTitle);
        ab.setView(viewNewPassword);
        ab.setPositiveButton(R.string.accept, null);
        ab.setNegativeButton(R.string.cancel, null);

        final AlertDialog d = ab.create();
        d.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        viewNewPassword.performButtonCheckClick();

//                        d.dismiss();
                    }
                });
            }
        });
        return d;

    }



    @Override
    public void onPasswordCorrect(String password) {

        acceptAndDismiss(password);
    }

    @Override
    public void onPasswordError(String reason) {

        //Handled internally
    }

    private void acceptAndDismiss(String password) {

        if (mListener != null) {
            mListener.onAccept(password);
        }
        this.dismiss();

    }

}
