package com.example.aston_contactbook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_contactbook.Model.Contact
import com.example.aston_contactbook.adapters.ContactAdapter
import com.example.aston_contactbook.adapters.ContactItemClickListener
import com.example.aston_contactbook.adapters.ContactTouchHelperCallback
import io.github.serpro69.kfaker.Faker

class MainActivity : AppCompatActivity() {

    private lateinit var contactAdapter: ContactAdapter
    private var contactPosition = 0
    private var contacts = mutableListOf<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.contactsList)
        val countOfContacts: TextView = findViewById(R.id.countOfContacts)
        val createContact: Button = findViewById(R.id.addContactButton)
        val deletedContacts: ImageButton = findViewById(R.id.deleteContacts)
        val deleteButtonsContainer: LinearLayout = findViewById(R.id.deleteButtonsContainer)
        val deleteSelected: Button = findViewById(R.id.deleteSelected)
        val cancelDelete: Button = findViewById(R.id.cancelDelete)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    if (data != null) {
                        val contact = data.getSerializableExtra("contactData") as? Contact
                        if (contact != null) {
                            if (contact.id == null) createContact(contact)
                            else updateContact(contact)
                            setCountContact(countOfContacts)
                        }
                    }
                }
            }
        val itemClickListener = object : ContactItemClickListener {
            override fun onContactItemClick(position: Int) {
                contactPosition = position
                val intent = Intent(this@MainActivity, EditContactActivity::class.java)
                intent.putExtra("contactData", contacts[position])
                resultLauncher.launch(intent)
            }

            override fun onCheckboxClick(position: Int, isChecked: Boolean) {
                contacts[position].isChecked = isChecked
            }
        }
        contactAdapter = ContactAdapter(itemClickListener)
        recyclerView.adapter = contactAdapter

        val itemTouchHelper = ItemTouchHelper(ContactTouchHelperCallback(contactAdapter))

        itemTouchHelper.attachToRecyclerView(recyclerView)

        generateContacts()
        contactAdapter.submitList(contacts)
        setCountContact(countOfContacts)

        createContact.setOnClickListener {
            val intent = Intent(this, CreateContactActivity::class.java)
            resultLauncher.launch(intent)
        }

        deletedContacts.setOnClickListener {
            createContact.visibility = View.GONE
            deleteButtonsContainer.visibility = View.VISIBLE
            contactAdapter.setCheckboxVisibility(true)
            contactAdapter.submitList(contacts)
        }

        cancelDelete.setOnClickListener {
            deleteButtonsContainer.visibility = View.GONE
            createContact.visibility = View.VISIBLE
            contactAdapter.setCheckboxVisibility(false)
            contactAdapter.submitList(contacts)
        }

        deleteSelected.setOnClickListener {
            deleteButtonsContainer.visibility = View.GONE
            createContact.visibility = View.VISIBLE
            contacts = deleteContacts(contacts)
            contactAdapter.setCheckboxVisibility(false)
            contactAdapter.submitList(contacts)
            setCountContact(countOfContacts)
        }
    }

    fun updateContacts(updatedContacts: List<Contact>) {
        contacts.clear()
        contacts.addAll(updatedContacts)
    }

    private fun createContact(contact: Contact) {
        val highestId = contacts.maxByOrNull { it.id ?: Int.MIN_VALUE }?.id ?: 0
        Log.d("New_ID", highestId.toString())
        contact.id = highestId + 1
        contacts.add(contact)
        contactAdapter.submitList(contacts)
    }

    private fun updateContact(contact: Contact) {
        contacts[contactPosition] = contact
        contactAdapter.submitList(contacts)
    }

    private fun deleteContacts(deleteContacts: List<Contact>): MutableList<Contact> {
        val newListContact = mutableListOf<Contact>()
        deleteContacts.forEach { contact ->
            if (!contact.isChecked) {
                newListContact.add(contact)
            }
        }
        return newListContact
    }

    private fun setCountContact(countOfContacts: TextView) {
        countOfContacts.text = "Count of contacts: ${contacts.size}"
    }

    private fun generateContacts() {
        val faker = Faker()
        for (i in 1..100) {
            val contact = Contact(
                id = i,
                firstName = faker.name.firstName(),
                lastName = faker.name.lastName(),
                phoneNumber = faker.phoneNumber.phoneNumber(),
                isChecked = false
            )
            contacts.add(contact)
        }
    }
}
