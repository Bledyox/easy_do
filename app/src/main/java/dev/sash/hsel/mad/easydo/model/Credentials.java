package dev.sash.hsel.mad.easydo.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import dev.sash.hsel.mad.easydo.BR;

public class Credentials extends BaseObservable implements Serializable {

    private String email;

    @SerializedName("pwd")
    private String password;

    public Credentials() {
        this.email = "";
        this.password = "";
    }

    @Bindable public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }
}
