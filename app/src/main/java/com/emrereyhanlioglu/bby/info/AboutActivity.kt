package com.emrereyhanlioglu.bby.info

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emrereyhanlioglu.bby.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }



    fun sendEmail(v: View) {
        setupAndStartEmailIntent()
    }


    private fun setupAndStartEmailIntent() {
        val intentEmail = Intent(Intent.ACTION_SEND)
        intentEmail.data = Uri.parse("mailto:")
        intentEmail.type = "text/plain"
        intentEmail.putExtra(Intent.EXTRA_EMAIL, arrayOf("reyhanlioglu@itu.edu.tr"))
        intentEmail.putExtra(Intent.EXTRA_SUBJECT, "BBY Mobile Application")
        intentEmail.putExtra(Intent.EXTRA_TEXT, "")

        try {
            startActivity(Intent.createChooser(intentEmail, "Bir e-mail kanalı seçiniz"))
        }
        catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }
}
