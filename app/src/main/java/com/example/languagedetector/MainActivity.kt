package com.example.languagedetector

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.mlkit.nl.languageid.LanguageIdentification

class MainActivity : AppCompatActivity() {
    lateinit var userInput: EditText
    lateinit var search: Button
    lateinit var language: Button
    lateinit var result: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userInput = findViewById(R.id.editText)
        search = findViewById(R.id.btn1)
        result = findViewById(R.id.tv)
        language = findViewById(R.id.btn2)

        search.setOnClickListener {
            val str=userInput.text.toString()
            identifyLanguageWithStringInput(str)
        }
        language.setOnClickListener {
            val str = userInput.text.toString()
            getPossibleLanguages(str)
        }
    }

    private fun getPossibleLanguages(str: String) {
        val languageIdentifier = LanguageIdentification.getClient()
        languageIdentifier.identifyPossibleLanguages(str)
            .addOnSuccessListener { identifiedLanguages ->
                val resultStringBuilder = StringBuilder("Possible Languages:\n")
                for (identifiedLanguage in identifiedLanguages) {
                    val language = identifiedLanguage.languageTag
                    val confidence = identifiedLanguage.confidence
                    resultStringBuilder.append("$language $confidence\n")
                }
                result.text = resultStringBuilder.toString()
            }
            .addOnFailureListener {
                // Handle failure
                Log.e(TAG, "Language identification failed.", it)
                result.text = "Language identification failed."
            }
    }

    private fun identifyLanguageWithStringInput(str: String) {
        val languageIdentifier = LanguageIdentification.getClient()
        languageIdentifier.identifyLanguage(str)
            .addOnSuccessListener { languageCode ->
                if (languageCode == "und") {
                    result.text = "Language Result: Can't identify language."
                } else {
                    result.text = "Language Result: $languageCode"
                }
            }
            .addOnFailureListener {
                // Handle failure
                Log.e(TAG, "Language identification failed.", it)
                result.text = "Language identification failed."
            }
    }
}