package com.example.callerid

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.InputStream

class MainActivity : ComponentActivity() {

    private var contacts = listOf<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load contacts from CSV file
        contacts = loadContacts()

        // Check for permissions
        requestPermissions()

        // Check if activity was triggered by an incoming call
        val incomingNumber = intent.getStringExtra("INCOMING_NUMBER")

        setContent {
            MaterialTheme {
                if (incomingNumber != null) {
                    // Show contact details for the incoming number
                    val matchedContact = findContactByNumber(incomingNumber)
                    if (matchedContact != null) {
                        ContactInfoScreen(matchedContact)
                        // Send a notification
                        NotificationUtils.sendNotification(this, matchedContact.name, matchedContact.designation)
                    } else {
                        Text("Unknown Caller: $incomingNumber")
                    }
                } else {
                    // Show default contact list
                    ContactListScreen(contacts)
                }
            }
        }
    }

    @Composable
    fun ContactListScreen(contacts: List<Contact>) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(contacts) { contact ->
                ContactCard(contact = contact)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    @Composable
    fun ContactInfoScreen(contact: Contact) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Incoming Call from ${contact.name}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Designation: ${contact.designation}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Mobile: ${contact.mobile}", fontSize = 16.sp)
        }
    }

    @Composable
    fun ContactCard(contact: Contact) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(4.dp) // Updated to use CardDefaults for elevation
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Name: ${contact.name}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Designation: ${contact.designation}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Mobile: ${contact.mobile}", fontSize = 16.sp)
            }
        }
    }

    private fun findContactByNumber(number: String): Contact? {
        return contacts.find { contact -> contact.mobile == number }
    }

    private fun loadContacts(): List<Contact> {
        val inputStream: InputStream = assets.open("contacts.csv")
        val reader = csvReader()
        return reader.readAllWithHeader(inputStream).map { row ->
            Contact(
                name = row["Name"] ?: "",
                designation = row["Designation"] ?: "",
                mobile = row["Mobile"] ?: ""
            )
        }
    }

    // Request permissions for reading phone state and call logs
    private fun requestPermissions() {
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {

            val requestPermissionLauncher =
                registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                    val phoneStateGranted = permissions[Manifest.permission.READ_PHONE_STATE] ?: false
                    val callLogGranted = permissions[Manifest.permission.READ_CALL_LOG] ?: false
                    if (!phoneStateGranted || !callLogGranted) {
                        // Handle permission denial
                    }
                }

            requestPermissionLauncher.launch(arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG))
        }
    }
}
