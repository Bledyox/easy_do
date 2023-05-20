package dev.sash.hsel.mad.easydo.widget;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev.sash.hsel.mad.easydo.R;
import dev.sash.hsel.mad.easydo.databinding.AdapterContactBinding;
import dev.sash.hsel.mad.easydo.model.Contact;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private ContactAdapter.Listener listener;
    private List<Contact> contacts;

    public ContactAdapter(ContactAdapter.Listener listener) {
        this.listener = listener;
        this.contacts = new ArrayList<>();
    }

    @NonNull @Override public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        AdapterContactBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_contact, parent, false);
        return new ContactAdapter.ViewHolder(binding);
    }

    @Override public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {
        holder.bind(contacts.get(position));
        holder.binding.setListener(this.listener);
    }

    @Override public int getItemCount() {
        return contacts.size();
    }

    public void addItem(Contact contact) {
        this.contacts.add(contact);
        notifyDataSetChanged();
    }

    public void fillItems(List<Contact> contacts) {
        this.contacts.clear();
        this.contacts.addAll(contacts);
        notifyDataSetChanged();
    }

    public Contact getItem(String id) {
        return this.contacts.get(this.contacts.indexOf(new Contact(id)));
    }

    public void setItem(Contact contact) {
        this.contacts.set(this.contacts.indexOf(contact), contact);
        notifyDataSetChanged();
    }

    public void removeItem(String id) {
        this.contacts.removeIf(contact -> contact.getId().equals(id));
        notifyDataSetChanged();
    }

    public void clearItems() {
        this.contacts.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public AdapterContactBinding binding;

        public ViewHolder(@NonNull AdapterContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Contact contact) {
            binding.setContact(contact);
            binding.executePendingBindings();
        }

    }

    public interface Listener {
        void onClickItem(String id);
        void onClickItemCloseIcon(String id);
    }

}
