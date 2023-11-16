package com.example.aston_contactbook

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.aston_contactbook.Model.Contact

class EditContactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact)

        val goBack: Button = findViewById(R.id.goBack)
        val createContact: Button = findViewById(R.id.saveContact)
        val firstName: EditText = findViewById(R.id.firstName)
        val lastName: EditText = findViewById(R.id.lastName)
        val phoneNumber: EditText = findViewById(R.id.phoneNumber)

        val contact = intent.getSerializableExtra("contactData") as? Contact

        if (contact != null) {
            firstName.setText(contact.firstName)
            lastName.setText(contact.lastName)
            phoneNumber.setText(contact.phoneNumber)

            goBack.setOnClickListener {
                val goBackIntent = Intent()
                setResult(Activity.RESULT_CANCELED, goBackIntent)
                finish()
            }

            createContact.setOnClickListener {
                val saveContactIntent = Intent()
                if (firstName.text.isEmpty() || lastName.text.isEmpty() || phoneNumber.text.isEmpty()) {
                    setResult(Activity.RESULT_CANCELED, saveContactIntent)
                    Toast.makeText(this, "Empty field", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    contact.firstName = firstName.text.toString()
                    contact.lastName = lastName.text.toString()
                    contact.phoneNumber = phoneNumber.text.toString()
                    saveContactIntent.putExtra("contactData", contact)
                    setResult(Activity.RESULT_OK, saveContactIntent)
                    finish()
                }
            }
        }


    }
}