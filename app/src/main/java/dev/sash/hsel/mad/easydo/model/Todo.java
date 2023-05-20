package dev.sash.hsel.mad.easydo.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.sash.hsel.mad.easydo.BR;
import dev.sash.hsel.mad.easydo.persistence.repository.RoomRepository;

@Entity(tableName = RoomRepository.Database.TABLE_NAME_TODO)
public class Todo extends BaseObservable implements Serializable {

    private static final String CONTACTS_SEPARATOR = "; ";

    @SerializedName("id") @PrimaryKey(autoGenerate = true) private long id;
    @SerializedName("name") @ColumnInfo(name = "name") private String name;
    @SerializedName("description") @ColumnInfo(name = "description") private String description;
    @SerializedName("done") @ColumnInfo(name = "done") private boolean done;
    @SerializedName("favourite") @ColumnInfo(name = "favourite") private boolean favourite;
    @SerializedName("expiry") @ColumnInfo(name = "expiry") private long expiry;
    @SerializedName("contacts") @ColumnInfo(name = "contacts") private List<String> contacts;
    /**@Expose(serialize = false, deserialize = false) @SerializedName("contacts") private String contacts_string;**/

    public Todo() {
        this.name = "";
        this.description = "";
        this.done = false;
        this.favourite = false;
        this.expiry = Long.MAX_VALUE;
        this.contacts = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Bindable public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
        notifyPropertyChanged(BR.done);
    }

    @Bindable public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
        notifyPropertyChanged(BR.favourite);
    }

    @Bindable public long getExpiry() {
        return expiry;
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
        notifyPropertyChanged(BR.expiry);
    }

    @Override public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Todo todo = (Todo) object;
        return id == todo.id;
    }

    @Bindable public List<String> getContacts() {
        return contacts;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
        notifyPropertyChanged(BR.contacts);
    }



    /**@Bindable public String getContacts_string() {
        return contacts_string;
    }

   public void setContacts_string(String contacts_string) {
        this.contacts_string = contacts_string;
        notifyPropertyChanged(BR.contacts_string);
    }

    public void beforePersist() {
        if (this.contacts != null) this.contacts_string = this.contacts.stream().collect(Collectors.joining(CONTACTS_SEPARATOR));
        Log.e("TEST", contacts_string + " " + contacts.toString());
    }

    public Todo afterLoad() {
        if (this.contacts_string != null) Arrays.stream(this.contacts_string.split(CONTACTS_SEPARATOR)).map(String::trim).collect(Collectors.toList());
        Log.e("TEST", contacts_string + " " + contacts.toString());
        return this;
    }**/

    @Override public int hashCode() {
        return Objects.hash(id);
    }

    @NotNull @Override public String toString() {
        return "[ " + id + " | " + name + " | " + description + " | " + done + " | " + favourite + " | " + expiry + " ]";
    }
}
