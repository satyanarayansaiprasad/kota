package com.example.callerid.util

import android.content.Context
import androidx.activity.ComponentActivity
import com.example.callerid.Contact
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.InputStream

object ContactUtils {

    private lateinit var contacts: List<Contact>
    private var isInitialized = false

    // Function to initialize contacts (should be called once)
    fun setContacts(context: Context) {
        if(!isInitialized) {
            isInitialized = true
            contacts = loadContacts(context)
        }
    }

    // Private function to load contacts from a CSV file
    private fun loadContacts(context: Context): List<Contact> {
        // Open the CSV file from assets
        val inputStream: InputStream = context.assets.open("contacts.csv")
        val reader = csvReader()

        // Map the CSV data to Contact objects
        return reader.readAllWithHeader(inputStream).map { row ->
            Contact(
                name = row["Name"] ?: "",
                designation = row["Designation"] ?: "",
                mobile = row["Mobile"] ?: ""
            )
        }
    }

    // Function to find a contact by phone number
    fun findContactByNumber(number: String): Contact? {
        return contacts.find { contact -> contact.mobile == number }
    }
}