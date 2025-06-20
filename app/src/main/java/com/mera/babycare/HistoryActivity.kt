package com.mera.babycare

import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily

class HistoryActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        setupViews()
    }

    private fun setupViews() {
        val card = findViewById<MaterialCardView>(R.id.card_title)
        card.shapeAppearanceModel = card.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 100f)
            .setTopRightCorner(CornerFamily.ROUNDED, 100f)
            .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
            .build()

        val cardBtns = findViewById<MaterialCardView>(R.id.card_btns)
        cardBtns.shapeAppearanceModel = cardBtns.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 100f)
            .setTopRightCorner(CornerFamily.ROUNDED, 100f)
            .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
            .build()

        val content = findViewById<MaterialCardView>(R.id.content_box)
        content.shapeAppearanceModel = content.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomLeftCorner(CornerFamily.ROUNDED, 100f)
            .setBottomRightCorner(CornerFamily.ROUNDED, 100f)
            .build()

        val navbar = findViewById<MaterialCardView>(R.id.navbar)

        ViewCompat.setOnApplyWindowInsetsListener(navbar) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            val params = view.layoutParams as ViewGroup.MarginLayoutParams
            params.bottomMargin = systemBarsInsets.bottom
            view.layoutParams = params

            insets
        }

        val babyCard = findViewById<LinearLayout>(R.id.baby_card)

        ViewCompat.setOnApplyWindowInsetsListener(babyCard) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            val params = view.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = systemBarsInsets.top
            view.layoutParams = params

            insets
        }
    }
}