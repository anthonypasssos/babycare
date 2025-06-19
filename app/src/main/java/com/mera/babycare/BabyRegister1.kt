package com.mera.babycare

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class BabyRegister1 : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baby_register1)
        setupListeners()
    }

    private var selectedBirthDate: Long? = null

    private fun setupListeners() {
        val dateEditText = findViewById<EditText>(R.id.input_date)

        dateEditText.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecione uma data")
                .setTheme(R.style.ThemeOverlay_App_DatePicker)
                .build()

            // Quando o usuário confirmar a data (clicar em OK):
            datePicker.addOnPositiveButtonClickListener { selection ->
                // O MaterialDatePicker retorna a data como um Long (timestamp em millis)
                val calendar = Calendar.getInstance()
                selectedBirthDate = calendar.timeInMillis
                calendar.timeInMillis = selection

                // Formatar a data como String (ex: 18/06/2025)
                val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = format.format(calendar.time)

                // Colocar a data formatada no EditText
                dateEditText.setText(formattedDate)
            }

            datePicker.show(supportFragmentManager, "DATE_PICKER")
        }

        val nextBtn = findViewById<ImageButton>(R.id.next_btn)

        nextBtn.setOnClickListener {
            val inputsData = getInputsData();

            if (validadeInputs(inputsData)) {
                goNextPage(inputsData)
            }
        }
    }

    private fun goNextPage(inputsData: RegisterBabyInput) {
        val intent = Intent(this, BabyRegister2::class.java)
        intent.putExtra("baby_name", inputsData.name)
        intent.putExtra("baby_birth", inputsData.date)
        startActivity(intent)
    }

    data class RegisterBabyInput(val name: String, val date: Long?)

    private fun getInputsData() : RegisterBabyInput {
        val name = findViewById<TextInputEditText>(R.id.input_name).text.toString()
        val date = selectedBirthDate

        return RegisterBabyInput(name, date)
    }

    private fun validadeInputs(inputsData: RegisterBabyInput): Boolean {
        return validateName(inputsData.name) && validateBirthDate(inputsData.date)
    }

    private fun validateName(nameText: String): Boolean {
        val nameLayout = findViewById<TextInputLayout>(R.id.layout_name)

        return if (nameText.isNotEmpty()) {
            nameLayout.error = null
            true
        } else {
            nameLayout.error = "Digite o nome do seu bebê"
            false
        }
    }

    private fun validateBirthDate(birthDate: Long?): Boolean {
        val birthDateLayout = findViewById<TextInputLayout>(R.id.layout_date)

        val now = System.currentTimeMillis()

        return when {
            birthDate == null -> {
                birthDateLayout.error = "Selecione uma data de nascimento"
                false
            }
            birthDate > now -> {
                birthDateLayout.error = "A data não pode estar no futuro"
                false
            }
            else -> {
                birthDateLayout.error = null
                true
            }
        }
    }

}
