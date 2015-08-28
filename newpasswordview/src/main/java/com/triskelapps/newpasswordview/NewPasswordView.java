package com.triskelapps.newpasswordview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by julio on 26/08/15.
 */
public class NewPasswordView extends LinearLayout implements View.OnClickListener, TextView.OnEditorActionListener {

    private EditText mEditPass, mEditPassRepeat;
    private Button mButtonCheck;
    private int minimumLenght = 6;
    private OnPasswordCheckListener onPasswordCheckListener;
    private String digestAlgorithm;

    public NewPasswordView(Context context) {
        this(context, null);
    }

    public NewPasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp();
    }

    private void setUp() {

        View layout = View.inflate(getContext(), R.layout.view_new_password, this);
        mEditPass = (EditText) layout.findViewById(R.id.edit_pass);
        mEditPassRepeat = (EditText) layout.findViewById(R.id.edit_pass_repeat);
        mButtonCheck = (Button) layout.findViewById(R.id.btn_check);

        mButtonCheck.setOnClickListener(this);

        mEditPassRepeat.setOnEditorActionListener(this);
    }

    // --- CONFIGURATIONS ---
    public void performButtonCheckClick() {
        mButtonCheck.performClick();
    }

    public void setButtonCheckVisible(boolean visible) {
        mButtonCheck.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * Set the minimum lenght of the password. By default 6
     * @param lenght
     */
    public void setMinimumLenght(int lenght) {
        this.minimumLenght = lenght;
    }

    public void digestPassword(String algorithm) throws IllegalArgumentException {
        this.digestAlgorithm = algorithm;

        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Algorithm not supported: " + algorithm);
        }
    }

    // --- USER ACTIONS ---
    @Override
    public void onClick(View v) {

        if (v == mButtonCheck) {
            checkPasswords();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            mButtonCheck.performClick();
            return true;
        }
        return false;
    }


    // --- LOGIC ---
    private void checkPasswords() {


        String pass = mEditPass.getText().toString();
        String passRepeat = mEditPassRepeat.getText().toString();

        if (pass.length() < minimumLenght) {

            String error = String.format(getContext().getString(R.string.at_least_x_characters), minimumLenght);

            mEditPass.setError(error);

            if (onPasswordCheckListener != null) {
                onPasswordCheckListener.onPasswordError(error);
            }

            return;
        }

        if (!pass.equals(passRepeat)) {

            String error = getContext().getString(R.string.pass_dont_match);

            mEditPass.setError(error);
            mEditPassRepeat.setError(error);

            if (onPasswordCheckListener != null) {
                onPasswordCheckListener.onPasswordError(error);
            }

            return;
        }

        if (onPasswordCheckListener != null) {

            if (digestAlgorithm != null) {
                try {
                    pass = DigestUtils.hashString(pass, digestAlgorithm);
                } catch (NoSuchAlgorithmException e) {
                    // This will never enter here, checked before
                }
            }

            onPasswordCheckListener.onPasswordCorrect(pass);
        }
    }




    // --- CALLBACK ---
    public interface OnPasswordCheckListener {

        /**
         * Called if password have minimum lenght and both matches
         * @param password The password string user have introduced. If an encryption method was configured, this is the hashed password
         */
        public void onPasswordCorrect(String password);

        /**
         * View holds internally error messages, but it can be interesting to handle externally as well :)
         * @param reason 2 errors possible: "At least x characters" and "Password does not match"
         */
        public void onPasswordError(String reason);
    }

    public void setOnPasswordCheckListener(OnPasswordCheckListener listener) {
        this.onPasswordCheckListener = listener;
    }
}
