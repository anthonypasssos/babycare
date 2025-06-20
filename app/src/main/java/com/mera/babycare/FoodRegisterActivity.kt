// app/src/main/java/com/mera/babycare/FoodRegisterActivity.kt
package com.mera.babycare

import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FoodRegisterActivity : BaseActivity() {

    private lateinit var toggleBreastMilk: MaterialButton
    private lateinit var toggleFormulaMilk: MaterialButton
    private lateinit var quantitySection: ConstraintLayout
    private lateinit var backButton: ImageButton

    private var inicioDateTime: Calendar? = null
    private var fimDateTime: Calendar? = null

    private var inicioTimestamp: Long? = null
    private var fimTimestamp: Long? = null

    private lateinit var inputQuantityEdit: TextInputEditText
    private lateinit var inputQuantityLayout: TextInputLayout
    private lateinit var inputInicioLayout: TextInputLayout
    private lateinit var inputFimLayout: TextInputLayout
    private lateinit var inputInicioEdit: TextInputEditText
    private lateinit var inputFimEdit: TextInputEditText
    private lateinit var textDay: TextView

    // Variável para guardar o estado do botão selecionado
    private var isBreastMilkSelected = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_register)

        // Inicializa as Views
        toggleBreastMilk = findViewById(R.id.toggle_breast_milk)
        toggleFormulaMilk = findViewById(R.id.toggle_formula_milk)
        quantitySection = findViewById(R.id.quantity_section)
        backButton = findViewById(R.id.back_button_food)

        // Configura os listeners
        setupToggleButtonListeners()
        setupBackButton()

        // Define o estado inicial
        updateButtonStyles()
        updateVisibility()

        inputInicioLayout = findViewById(R.id.time_input_start_food)
        inputFimLayout = findViewById(R.id.time_input_end_food)
        inputInicioEdit = findViewById(R.id.time_text_start_food)
        inputFimEdit = findViewById(R.id.time_text_end_food)
        textDay = findViewById(R.id.text_day)

        inputQuantityLayout = findViewById(R.id.text_quantity_label)
        inputQuantityEdit = findViewById(R.id.input_quantity_ml)

        inputInicioEdit.setOnClickListener {
            showTimePicker { calendar ->
                inicioDateTime = calendar
                inicioTimestamp = calendar.timeInMillis // salva como timestamp
                inputInicioEdit.setText(formatTime(calendar))
                textDay.setText(isHojeOuOntem(calendar))
            }
        }

        inputFimEdit.setOnClickListener {
            showTimePicker { calendar ->
                fimDateTime = calendar
                fimTimestamp = calendar.timeInMillis // salva como timestamp
                inputFimEdit.setText(formatTime(calendar))
            }
        }

        findViewById<android.view.View>(R.id.register_food_button).setOnClickListener {
            if (validarCampos()) {
                registerBaby()
            }
        }
    }



    private fun setupBackButton() {
        backButton.setOnClickListener {
            finish() // Fecha a atividade atual e volta para a anterior
        }
    }

    private fun setupToggleButtonListeners() {
        toggleBreastMilk.setOnClickListener {
            isBreastMilkSelected = true
            updateButtonStyles()
            updateVisibility()
        }

        toggleFormulaMilk.setOnClickListener {
            isBreastMilkSelected = false
            updateButtonStyles()
            updateVisibility()
        }
    }

    private fun updateButtonStyles() {
        if (isBreastMilkSelected) {
            // Estilo para botão selecionado
            toggleBreastMilk.setBackgroundColor(getColor(R.color.strong_green))
            toggleBreastMilk.setTextColor(Color.WHITE)
            toggleBreastMilk.strokeWidth = 0

            // Estilo para botão não selecionado
            toggleFormulaMilk.setBackgroundColor(Color.TRANSPARENT)
            toggleFormulaMilk.setTextColor(getColor(R.color.black))
            toggleFormulaMilk.strokeWidth = 2 // Ou o valor original do seu stroke
        } else {
            // Estilo para botão não selecionado
            toggleBreastMilk.setBackgroundColor(Color.TRANSPARENT)
            toggleBreastMilk.setTextColor(getColor(R.color.black))
            toggleBreastMilk.strokeWidth = 2

            // Estilo para botão selecionado
            toggleFormulaMilk.setBackgroundColor(getColor(R.color.strong_green))
            toggleFormulaMilk.setTextColor(Color.WHITE)
            toggleFormulaMilk.strokeWidth = 0
        }
    }

    private fun updateVisibility() {
        // Mostra a seção de quantidade apenas se "Leite (Fórmula)" estiver selecionado
        quantitySection.visibility = if (isBreastMilkSelected) View.GONE else View.VISIBLE
    }

    private fun showTimePicker(onTimeSelected: (Calendar) -> Unit) {
        val now = Calendar.getInstance()
        val hour = now.get(Calendar.HOUR_OF_DAY)
        val minute = now.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(Calendar.HOUR_OF_DAY, selectedHour)
            selectedCalendar.set(Calendar.MINUTE, selectedMinute)
            selectedCalendar.set(Calendar.SECOND, 0)
            selectedCalendar.set(Calendar.MILLISECOND, 0)
            onTimeSelected(selectedCalendar)
        }, hour, minute, true)

        timePicker.show()
    }

    private fun formatTime(calendar: Calendar): String {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return format.format(calendar.time)
    }

    private fun isHojeOuOntem(calendar: Calendar): String {
        val agora = Calendar.getInstance()

        val selecionada = Calendar.getInstance().apply {
            timeInMillis = calendar.timeInMillis
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val horaSelecionada = selecionada.get(Calendar.HOUR_OF_DAY)
        val minutoSelecionado = selecionada.get(Calendar.MINUTE)

        val horaAtual = agora.get(Calendar.HOUR_OF_DAY)
        val minutoAtual = agora.get(Calendar.MINUTE)

        return if (
            horaSelecionada < horaAtual ||
            (horaSelecionada == horaAtual && minutoSelecionado <= minutoAtual)
        ) {
            "Hoje"
        } else {
            "Ontem"
        }
    }

    private fun validarCampos(): Boolean {
        var valido = true

        if (inputInicioEdit.text.isNullOrBlank() || inicioTimestamp == null) {
            inputInicioLayout.error = "Campo obrigatório"
            valido = false
        } else {
            inputInicioLayout.error = null
        }

        if (inputFimEdit.text.isNullOrBlank() || fimTimestamp == null) {
            inputFimLayout.error = "Campo obrigatório"
            valido = false
        } else {
            inputFimLayout.error = null
        }

        // VALIDAÇÃO QUANTIDADE (somente se for fórmula)
        if (!isBreastMilkSelected) {
            if (inputQuantityEdit.text.isNullOrBlank()) {
                inputQuantityLayout.error = "Campo obrigatório"
                valido = false
            } else {
                inputQuantityLayout.error = null
            }
        } else {
            inputQuantityLayout.error = null // Limpa o erro caso tenha vindo de antes
        }

        return valido
    }


    private fun registerBaby() {
        val prefs = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val babyId = prefs.getString("baby_id", null)

        if (babyId == null) {
            Toast.makeText(this, "Nenhum bebê selecionado.", Toast.LENGTH_SHORT).show()
            return
        }

        val startTimestamp = inicioTimestamp
        val endTimestamp = fimTimestamp

        if (startTimestamp == null || endTimestamp == null) {
            Toast.makeText(this, "Horário inválido.", Toast.LENGTH_SHORT).show()
            return
        }

        val dbHelper = Database(this)
        val db = dbHelper.writableDatabase
        val dataBaseManager = DataBaseManager(this)

        val id = java.util.UUID.randomUUID().toString()
        val type = if (isBreastMilkSelected) "amamentação" else "fórmula"

        // Captura o volume, mas só se for fórmula
        val volumeMl: Int? = if (!isBreastMilkSelected) {
            inputQuantityEdit.text.toString().toIntOrNull()
        } else {
            null
        }

        val notes: String? = null // Adapte se futuramente quiser adicionar notas

        dataBaseManager.addFeeding(
            db = db,
            id = id,
            babyId = babyId,
            type = type,
            startTime = startTimestamp,
            endTime = endTimestamp,
            volumeMl = volumeMl,
            notes = notes
        )

        Toast.makeText(this, "Alimentação registrada com sucesso!", Toast.LENGTH_SHORT).show()
        finish()
    }

}