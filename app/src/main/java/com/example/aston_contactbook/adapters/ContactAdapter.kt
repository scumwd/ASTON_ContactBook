package com.example.aston_contactbook.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_contactbook.Model.Contact
import com.example.aston_contactbook.R

class ContactAdapter(private val itemClickListener: ContactItemClickListener) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private var contacts = mutableListOf<Contact>()
    private var isCheckboxVisible = false

    fun setCheckboxVisibility(isVisible: Boolean) {
        isCheckboxVisible = isVisible
        notifyDataSetChanged()
    }

    fun swapItems(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                contacts[i] = contacts.set(i + 1, contacts[i])
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                contacts[i] = contacts.set(i - 1, contacts[i])
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.fullNameTextView)
        val phoneNumberTextView: TextView = itemView.findViewById(R.id.phoneNumberTextView)
        val checkboxContact: CheckBox = itemView.findViewById(R.id.checkboxContact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.nameTextView.text = "${contact.firstName} ${contact.lastName}"
        holder.phoneNumberTextView.text = contact.phoneNumber

        if (isCheckboxVisible) {
            holder.checkboxContact.visibility = View.VISIBLE
            holder.checkboxContact.isChecked = contact.isChecked
            holder.itemView.setOnClickListener(null)
            holder.checkboxContact.setOnCheckedChangeListener { button, b ->
                itemClickListener.onCheckboxClick(position, b)
            }
        } else {
            holder.checkboxContact.isChecked = false
            holder.checkboxContact.visibility = View.GONE
            holder.itemView.setOnClickListener {
                itemClickListener.onContactItemClick(holder.adapterPosition)
            }
        }
    }


    override fun getItemCount(): Int {
        return contacts.size
    }

    fun submitList(newContacts: List<Contact>) {
        val diffResult =
            DiffUtil.calculateDiff(ContactDiffCallback(contacts.toList(), newContacts.toList()))
        diffResult.dispatchUpdatesTo(this)
        contacts.clear()
        contacts.addAll(newContacts)
    }

    fun getCurrentList(): MutableList<Contact> {
        return contacts.toMutableList()
    }
}

private class ContactDiffCallback(
    private val oldList: List<Contact>,
    private val newList: List<Contact>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id ||
                oldList[oldItemPosition].firstName == newList[newItemPosition].firstName ||
                oldList[oldItemPosition].lastName == newList[newItemPosition].lastName ||
                oldList[oldItemPosition].phoneNumber == newList[newItemPosition].phoneNumber
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}