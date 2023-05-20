package dev.sash.hsel.mad.easydo.app;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import dev.sash.hsel.mad.easydo.R;
import dev.sash.hsel.mad.easydo.databinding.ActivityDetailBinding;
import dev.sash.hsel.mad.easydo.model.Contact;
import dev.sash.hsel.mad.easydo.model.Todo;
import dev.sash.hsel.mad.easydo.persistence.repository.Repository;
import dev.sash.hsel.mad.easydo.utils.Globals;
import dev.sash.hsel.mad.easydo.widget.ContactAdapter;

public class DetailActivity extends AppCompatActivity implements ContactAdapter.Listener {

    private ActivityDetailBinding binding;
    private Repository repository;
    private Todo todo;
    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        this.repository = ((TodoApplication) getApplication()).getRepository();
        this.adapter = new ContactAdapter(this);
        Intent intent = getIntent();
        if (intent != null) {
            switch (getIntent().getIntExtra(Globals.EXTRA_ACTION, 0)) {
                case Globals.ACTION_TODO_CREATE:
                    this.todo = new Todo();
                    binding.setActivity(this);
                    break;
                case Globals.ACTION_TODO_UPDATE:
                    this.repository.read(getIntent().getLongExtra(Globals.EXTRA_ID, 0), buffer -> {
                        this.todo = buffer;
                        List<Contact> contacts = new ArrayList<>();
                        todo.getContacts().forEach(id -> contacts.add(loadContact(id)));
                        adapter.fillItems(contacts);
                        binding.setActivity(this);
                    });
                    break;
                default:
                    setResult(RESULT_CANCELED);
                    finish();
            }
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int request_code, int result_code, @Nullable Intent intent) {
        super.onActivityResult(request_code, result_code, intent);
        if (result_code == RESULT_OK && request_code == Globals.REQUEST_ACTIVITY_CONTACT_PICKER) {
            assert intent != null;
            Cursor cursor = getContentResolver().query(Objects.requireNonNull(intent.getData()), null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                if (!todo.getContacts().contains(intent.getData().toString())) {
                    todo.getContacts().add(intent.getData().toString());
                    adapter.addItem(loadContact(intent.getData().toString()));
                }
            }
            if (cursor != null) cursor.close();
        }
    }

    public Contact loadContact(String id) {
        Contact contact = new Contact(id);
        String internal_id = null;
        Cursor cursor = getContentResolver().query(Uri.parse(id), null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            contact.setDisplayname(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
            internal_id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            cursor.close();
        }
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{internal_id}, null, null);
        while (cursor != null && cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA2)) == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                contact.setMobilnumber(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                cursor.close();
                break;
            }
        }
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{internal_id}, null, null);
        if (cursor != null && cursor.moveToNext()) {
            contact.setEmailaddress(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)));
            cursor.close();
        }
        return contact;
    }

    public void onClickSaveButton() {
        if (todo.getName().equals("")) {
            new MaterialAlertDialogBuilder(this).setTitle(R.string.dialog_title_save_todo).setMessage(R.string.dialog_message_save_todo)
                    .setPositiveButton(R.string.dialog_action_positive, (dialog, button) -> {
                        setResult(RESULT_CANCELED);
                        finish();
                    }).setNegativeButton(R.string.dialog_action_negative, (dialog, button) -> {
            }).show();
        } else {
            if (todo.getId() == 0) repository.create(todo, id -> {
                setResult(RESULT_OK, new Intent().putExtra(Globals.EXTRA_ACTION, Globals.ACTION_TODO_CREATED).putExtra(Globals.EXTRA_ID, id));
                finish();
            });
            else repository.update(todo, id -> {
                setResult(RESULT_OK, new Intent().putExtra(Globals.EXTRA_ACTION, Globals.ACTION_TODO_UPDATED).putExtra(Globals.EXTRA_ID, id));
                finish();
            });
        }
    }

    public void onClickDeleteButton() {
        new MaterialAlertDialogBuilder(this).setTitle(R.string.dialog_title_delete_todo).setMessage(R.string.dialog_message_delete_todo)
                .setPositiveButton(R.string.dialog_action_positive, (dialog, button) -> repository.delete(todo, id -> {
                    setResult(RESULT_OK, new Intent().putExtra(Globals.EXTRA_ACTION, Globals.ACTION_TODO_DELETED).putExtra(Globals.EXTRA_ID, id));
                    finish();
                })).setNegativeButton(R.string.dialog_action_negative, (dialog, button) -> {
        }).show();
    }

    public void onClickExpiryEdit() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime((todo.getExpiry() == Long.MAX_VALUE) ? new Date() : new Date(todo.getExpiry()));
        new DatePickerDialog(this, (date, year, month, day) -> new TimePickerDialog(this, (time, hour, minute) -> {
            calendar.set(year, month, day, hour, minute);
            todo.setExpiry(calendar.getTime().getTime());
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void onClickContactButton() {
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, Globals.REQUEST_PERMISSION_READ_CONTACTS);
            return;
        }
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), Globals.REQUEST_ACTIVITY_CONTACT_PICKER);
    }

    public Todo getTodo() {
        return todo;
    }

    @Override public void onClickItem(String id) {
        Contact contact = this.adapter.getItem(id);
        MaterialAlertDialogBuilder alert_dialog = new MaterialAlertDialogBuilder(this).setTitle(R.string.dialog_title_share_contact).setMessage(R.string.dialog_message_share_contact);
        if (contact.getEmailaddress() != null && !contact.getEmailaddress().isEmpty())
            alert_dialog.setPositiveButton(R.string.dialog_action_email, (dialog, button) -> startActivity(
                    new Intent(Intent.ACTION_SENDTO)
                            .setData(Uri.parse("mailto:"))
                            .putExtra(Intent.EXTRA_EMAIL, new String[]{contact.getEmailaddress()})
                            .putExtra(Intent.EXTRA_SUBJECT, todo.getName())
                            .putExtra(Intent.EXTRA_TEXT, todo.getDescription())
            ));
        if (contact.getMobilnumber() != null && !contact.getMobilnumber().isEmpty())
            alert_dialog.setNegativeButton(R.string.dialog_action_sms, (dialog, button) -> startActivity(
                    new Intent(Intent.ACTION_SENDTO)
                            .setData(Uri.parse("smsto:" + contact.getMobilnumber()))
                            .putExtra(Intent.EXTRA_TEXT, todo.getName() + ": " + todo.getDescription())
            ));
        alert_dialog.setNeutralButton(R.string.dialog_action_neutral, (dialog, button) -> {
        }).show();
    }

    @Override public void onClickItemCloseIcon(String id) {
        todo.getContacts().removeIf(buffer -> buffer.equals(id));
        adapter.removeItem(id);
    }

    public ContactAdapter getAdapter() {
        return adapter;
    }

}