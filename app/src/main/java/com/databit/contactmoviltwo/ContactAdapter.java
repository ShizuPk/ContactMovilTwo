package com.databit.contactmoviltwo;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private static List<Contact> contactList;
    private OnContactClickListener onContactClickListener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public ContactAdapter(List<Contact> contactList, OnContactClickListener onContactClickListener) {
        this.contactList = contactList;
        this.onContactClickListener = onContactClickListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(view, onContactClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void updateList(List<Contact> newContacts) {
        contactList = newContacts;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public Contact getItem(int position) {
        return contactList.get(position);
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public interface OnContactClickListener {
        void onContactClick(Contact contact);
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textName;
        private TextView textEmail;
        private OnContactClickListener onContactClickListener;

        public ContactViewHolder(@NonNull View itemView, OnContactClickListener onContactClickListener) {
            super(itemView);
            this.textName = itemView.findViewById(R.id.contactName);
            this.textEmail = itemView.findViewById(R.id.contactEmail);
            this.onContactClickListener = onContactClickListener;

            itemView.setOnClickListener(this);
        }

        public void bind(Contact contact) {
            textName.setText(contact.getNombre());
            textEmail.setText(contact.getCorreo());
        }

        @Override
        public void onClick(View view) {
            if (onContactClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onContactClickListener.onContactClick(contactList.get(position));
                }
            }
        }
    }
}
