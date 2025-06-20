package com.mera.babycare

import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SleepRegisterActivity : BaseActivity() {

    @RequiresApi(Build.VERSION_CODES.Q)
    private var inicioDateTime: Calendar? = null
    private var fimDateTime: Calendar? = null

    private var inicioTimestamp: Long? = null
    private var fimTimestamp: Long? = null

    private lateinit var inputInicioLayout: TextInputLayout
    private lateinit var inputFimLayout: TextInputLayout
    private lateinit var inputInicioEdit: TextInputEditText
    private lateinit var inputFimEdit: TextInputEditText
    private lateinit var textDay: TextView

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleep_register)

        inputInicioLayout = findViewById(R.id.time_input_inicio)
        inputFimLayout = findViewById(R.id.time_input_fim)
        inputInicioEdit = findViewById(R.id.time_text_inicio)
        inputFimEdit = findViewById(R.id.time_text_fim)
        textDay = findViewById(R.id.text_day)

        inputInicioEdit.setOnClickListener {
            showTimePicker { calendar ->
                inicioDateTime = calendar
                inicioTimestamp = calendar.timeInMillis
                inputInicioEdit.setText(formatTime(calendar))
                textDay.setText(isHojeOuOntem(calendar))
            }
        }

        inputFimEdit.setOnClickListener {
            showTimePicker { calendar ->
                fimDateTime = calendar
                fimTimestamp = calendar.timeInMillis
                inputFimEdit.setText(formatTime(calendar))
            }
        }

        findViewById<android.view.View>(R.id.sleep_register_btn).setOnClickListener {
            if (validarCampos()) {
                registerBaby()
            }
        }
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
        val isNap = false
        val notes = null

        dataBaseManager.addSleepSession(
            db = db,
            id = id,
            babyId = babyId,
            startTime = startTimestamp,
            endTime = endTimestamp,
            isNap = isNap,
            notes = notes
        )

        Toast.makeText(this, "Sessão de sono salva com sucesso!", Toast.LENGTH_SHORT).show()
        finish()
    }

}
