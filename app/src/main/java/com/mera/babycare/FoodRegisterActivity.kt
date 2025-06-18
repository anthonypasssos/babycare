// app/src/main/java/com/mera/babycare/FoodRegisterActivity.kt
package com.mera.babycare

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton

class FoodRegisterActivity : AppCompatActivity() {

    private lateinit var toggleBreastMilk: MaterialButton
    private lateinit var toggleFormulaMilk: MaterialButton
    private lateinit var quantitySection: ConstraintLayout
    private lateinit var backButton: ImageButton

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
}