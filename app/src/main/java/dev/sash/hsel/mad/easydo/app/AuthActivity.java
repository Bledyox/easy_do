package dev.sash.hsel.mad.easydo.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;

import dev.sash.hsel.mad.easydo.R;
import dev.sash.hsel.mad.easydo.databinding.ActivityAuthBinding;
import dev.sash.hsel.mad.easydo.model.Credentials;
import dev.sash.hsel.mad.easydo.persistence.repository.Repository;
import dev.sash.hsel.mad.easydo.utils.ValidatorUtils;
import dev.sash.hsel.mad.easydo.widget.TextInputEditTextError;

public class AuthActivity extends AppCompatActivity {

    private ActivityAuthBinding binding;
    private Repository repository;
    private Credentials credentials;
    private Snackbar snackbar;
    private ProgressDialog progress;
    private TextInputEditTextError email_error;
    private TextInputEditTextError password_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        this.repository = ((TodoApplication) getApplication()).getRepository();
        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.message_verify_remote_connection));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        this.repository.connection(connected -> {
            if (!connected) startActivity(new Intent(this, OverviewActivity.class).putExtra("connected", false));
            else this.repository.synchronize();
            progress.dismiss();
        });
        this.credentials = new Credentials();
        this.email_error = new TextInputEditTextError(ValidatorUtils.STRING_IS_EMAIL.and(ValidatorUtils.STRING_NOT_EMPTY).and(ValidatorUtils.STRING_NOT_NULL), getString(R.string.error_email_invalid));
        this.password_error = new TextInputEditTextError(ValidatorUtils.STING_LENGTH_EQUAL(6).and(ValidatorUtils.STRING_NOT_EMPTY).and(ValidatorUtils.STRING_NOT_NULL), getString(R.string.error_password_invalid));
        this.binding.setEmailWatcher(email_error.createTextWatcher(this));
        this.binding.setPasswordWatcher(password_error.createTextWatcher(this));
        this.binding.setActivity(this);
    }

    public void showActivityMessage(String message, int time) {
        this.snackbar = Snackbar.make(binding.getRoot(), message, time);
        this.snackbar.show();
    }

    public void hideActivityMessage() {
        if(this.snackbar != null && this.snackbar.isShown()) this.snackbar.dismiss();
    }

    public void onClickLoginButton() {
        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.message_verify_credentials));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        this.repository.verify(credentials, accepted -> {
            progress.dismiss();
            if (accepted) startActivity(new Intent(this, OverviewActivity.class).putExtra("connected", true));
            else showActivityMessage(getString(R.string.message_invalid_credentials), Snackbar.LENGTH_INDEFINITE);
        });
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public TextInputEditTextError getEmail_error() {
        return email_error;
    }

    public TextInputEditTextError getPassword_error() {
        return password_error;
    }

}