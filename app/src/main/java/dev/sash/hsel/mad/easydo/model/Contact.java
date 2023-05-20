package dev.sash.hsel.mad.easydo.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.io.Serializable;
import java.util.Objects;

import dev.sash.hsel.mad.easydo.BR;

public class Contact extends BaseObservable implements Serializable {

    private String id;
    private String displayname;
    private String emailaddress;
    private String mobilnumber;

    public Contact(String id) {
        this.id = id;
        this.displayname = "";
        this.emailaddress = "";
        this.mobilnumber = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Bindable public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String display_name) {
        this.displayname = display_name;
        notifyPropertyChanged(BR.displayname);
    }

    @Bindable public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String email_address) {
        this.emailaddress = email_address;
        notifyPropertyChanged(BR.emailaddress);
    }

    @Bindable public String getMobilnumber() {
        return mobilnumber;
    }

    public void setMobilnumber(String mobil_number) {
        this.mobilnumber = mobil_number;
        notifyPropertyChanged(BR.mobilnumber);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return id.equals(contact.id);
    }

    @Override public int hashCode() {
        return Objects.hash(id);
    }

    @Override public String toString() {
        return "Contact{" + "id='" + id + '\'' + ", displayname='" + displayname + '\'' + ", emailaddress='" + emailaddress + '\'' + ", mobilnumber='" + mobilnumber + '\'' + '}';
    }
}
