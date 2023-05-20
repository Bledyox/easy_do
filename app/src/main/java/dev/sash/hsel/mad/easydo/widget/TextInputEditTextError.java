package dev.sash.hsel.mad.easydo.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.io.Serializable;

import dev.sash.hsel.mad.easydo.BR;
import dev.sash.hsel.mad.easydo.app.AuthActivity;
import dev.sash.hsel.mad.easydo.utils.ValidatorUtils;

public class TextInputEditTextError extends BaseObservable implements Serializable {

    private boolean present;
    private String message;
    private ValidatorUtils.Validation<String> validation;
    private String invalid_message;
    
    public TextInputEditTextError(ValidatorUtils.Validation<String> validation, String invalid_message) {
        this.present = false;
        this.message = "";
        this.validation = validation;
        this.invalid_message = invalid_message;
    }

    @Bindable public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
        notifyPropertyChanged(BR.present);
    }

    @Bindable public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        notifyPropertyChanged(BR.message);
    }

    public void onFocusChange(boolean focus, String text) {
        if (!focus) {
            ValidatorUtils.Result result = this.validation.test(text);
            this.setMessage(result.getMessageIfInvalid(this.invalid_message).orElse(""));
            this.setPresent(result.isInvalid());
        }
    }

    public TextWatcher createTextWatcher(Context context) {
        return new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence chars, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence chars, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable editable) {
                setPresent(false);
                ((AuthActivity) context).hideActivityMessage();
            }
        };
    }

}
