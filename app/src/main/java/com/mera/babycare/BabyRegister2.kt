package com.mera.babycare

import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class BabyRegister2 : AppCompatActivity() {
    private lateinit var girlBtn: MaterialButton
    private lateinit var boyBtn: MaterialButton

    private var isGirlSelected = false
    private var isBoySelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_baby_register2)

        girlBtn = findViewById(R.id.girl_btn)
        boyBtn = findViewById(R.id.boy_btn)

        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        girlBtn.setOnTouchListener { v, event ->
            handleButtonTouch(v as MaterialButton, event, "girl")
            true
        }

        boyBtn.setOnTouchListener { v, event ->
            handleButtonTouch(v as MaterialButton, event, "boy")
            true
        }
    }

    private fun handleButtonTouch(button: MaterialButton, event: MotionEvent, type: String) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Ao tocar para baixo, aplica a elevação temporária
                if (type == "girl" && !isGirlSelected || type == "boy" && !isBoySelected) {
                    button.translationZ = 8f
                }
            }
            MotionEvent.ACTION_UP -> {
                // Ao soltar, alterna o estado de seleção e ajusta a elevação
                if (type == "girl") {
                    if (!isGirlSelected) { // Se a menina não estiver selecionada, selecione-a
                        isGirlSelected = true
                        isBoySelected = false // Deselecione o menino
                    } else { // Se a menina já estiver selecionada, deselecione-a
                        isGirlSelected = false
                    }
                } else if (type == "boy") {
                    if (!isBoySelected) { // Se o menino não estiver selecionado, selecione-o
                        isBoySelected = true
                        isGirlSelected = false // Deselecione a menina
                    } else { // Se o menino já estiver selecionado, deselecione-o
                        isBoySelected = false
                    }
                }
                updateButtonStates()
            }
        }
    }

    private fun updateButtonStates() {
        // Atualiza a elevação e aparência dos botões com base no estado de seleção
        girlBtn.translationZ = if (isGirlSelected) 4f else 0f
        boyBtn.translationZ = if (isBoySelected) 4f else 0f

        // Opcional: Você pode querer mudar a cor do texto ou background para indicar seleção
        // Exemplo:
        // girlBtn.setTextColor(if (isGirlSelected) getColor(R.color.selected_color) else getColor(R.color.unselected_color))
        // boyBtn.setTextColor(if (isBoySelected) getColor(R.color.selected_color) else getColor(R.color.unselected_color))
    }
}