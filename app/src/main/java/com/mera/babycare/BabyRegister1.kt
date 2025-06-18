package com.mera.babycare

import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class BabyRegister1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_baby_register1)

        val dateEditText = findViewById<EditText>(R.id.input_name)

        dateEditText.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecione uma data")
                .setTheme(R.style.ThemeOverlay_App_DatePicker)
                .build()

            // Quando o usuário confirmar a data (clicar em OK):
            datePicker.addOnPositiveButtonClickListener { selection ->
                // O MaterialDatePicker retorna a data como um Long (timestamp em millis)
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = selection

                // Formatar a data como String (ex: 18/06/2025)
                val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = format.format(calendar.time)

                // Colocar a data formatada no EditText
                dateEditText.setText(formattedDate)
            }

            datePicker.show(supportFragmentManager, "DATE_PICKER")
        }
    }
}
