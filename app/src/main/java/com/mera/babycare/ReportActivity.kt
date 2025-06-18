package com.mera.babycare

import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ReportActivity : AppCompatActivity() {
    private lateinit var foodButton: ImageButton
    private lateinit var sleepButton: ImageButton
    private lateinit var relatoryTitle: TextView
    private lateinit var sleepScroll: ScrollView
    private lateinit var databaseHelper: Database
    private lateinit var dbManager: DataBaseManager
    private lateinit var graph: GraphView
    private lateinit var container: LinearLayout
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    private data class FeedingRecord(
        val id: String,
        val type: String,
        val startTime: Long,
        val endTime: Long,
        val volumeMl: Int,
        val notes: String?
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_report)

        // Inicializar Banco de Dados
        databaseHelper = Database(this)
        dbManager = DataBaseManager(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar os elementos
        foodButton = findViewById(R.id.foodButton)
        sleepButton = findViewById(R.id.sleepButton)
        relatoryTitle = findViewById(R.id.relatoryTitle)
        sleepScroll = findViewById(R.id.sleepScroll)
        graph = findViewById(R.id.sleepgraph);
        container = findViewById(R.id.foodItemsContainer)

        // Configurar listeners de clique
        foodButton.setOnClickListener {
            setSelectedButton(true)
        }

        sleepButton.setOnClickListener {
            setSelectedButton(false)
            loadSleepSessions()
        }


        // Definir estado inicial (foodButton selecionado por padrão)
        setSelectedButton(true)
    }

    private fun getFeedingsCursor(): Cursor {
        return databaseHelper.readableDatabase.query(
            "feedings",
            arrayOf("id", "baby_id", "type", "start_time", "end_time", "volume_ml", "notes", "created_at"),
            null, null, null, null,
            "created_at DESC"
        ).also {
            Log.d("Feedings", "Cursor with ${it.count} records")
        }
    }

    private fun loadSleepSessions() {
        val db = databaseHelper.readableDatabase

        // Query all sleep sessions
        val cursor = db.query(
            "sleep_sessions",
            arrayOf("id", "baby_id", "start_time", "end_time", "is_nap", "notes", "created_at"),
            null, null, null, null, "created_at DESC"
        )

        Log.d("SleepSessions", "Found ${cursor.count} sleep sessions")

    }

    private fun setSelectedButton(isFoodSelected: Boolean) {
        if (isFoodSelected) {
            foodButton.setImageResource(R.drawable.relatory_selected)
            sleepButton.setImageResource(R.drawable.relatory_unselected)
            relatoryTitle.text = "Histórico de Refeições"
            sleepScroll.visibility = View.GONE

            findViewById<ScrollView>(R.id.foodScroll).visibility = View.VISIBLE
            loadFoodItems()

        } else {
            foodButton.setImageResource(R.drawable.relatory_unselected)
            sleepButton.setImageResource(R.drawable.relatory_selected)
            relatoryTitle.text = "Análise de Sono"

            val series = LineGraphSeries(
                arrayOf(
                    DataPoint(1.0, 5.0),
                    DataPoint(2.0, 3.0),
                    DataPoint(3.0, 2.0),
                    DataPoint(4.0, 6.0),
                    DataPoint(5.0, 6.0),
                    DataPoint(6.0, 6.0),
                    DataPoint(7.0, 6.0),
                )
            )
            graph.addSeries(series)
            series.color = Color.parseColor("#FFCBC5")

            val weekdays = arrayOf("", "S", "T", "Q", "Q", "S", "S", "D") // Index 0 is empty

            graph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
                override fun formatLabel(value: Double, isValueX: Boolean): String {
                    return if (isValueX) {
                        val index = value.toInt()
                        if (index in 1..7) weekdays[index] else ""
                    } else {
                        super.formatLabel(value, isValueX)
                    }
                }
            }
            /*graph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
                private val weekdays = arrayOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sab", "Dom")

                override fun formatLabel(value: Double, isValueX: Boolean): String {
                    return if (isValueX) {
                        if (value >= 0 && value < weekdays.size) {
                            weekdays[value.toInt()]
                        } else {
                            ""
                        }
                    } else {
                        super.formatLabel(value, isValueX)
                    }
                }
            }*/
            graph.viewport.setMinX(0.5)  // Small margin before first point
            graph.viewport.setMaxX(7.5)  // Small margin before first point
            graph.viewport.isXAxisBoundsManual = true
            graph.viewport.isYAxisBoundsManual = true
            graph.gridLabelRenderer.padding = 32
            //graph.gridLabelRenderer.setHorizontalLabelsAngle(45)
            graph.gridLabelRenderer.numHorizontalLabels = 8
            graph.gridLabelRenderer.verticalAxisTitle = "Horas"
            graph.gridLabelRenderer.verticalAxisTitleTextSize = 30f // in SP
            graph.gridLabelRenderer.verticalAxisTitle
            graph.gridLabelRenderer.verticalAxisTitleColor = Color.BLACK
            graph.gridLabelRenderer.gridStyle = GridLabelRenderer.GridStyle.HORIZONTAL

            sleepScroll.visibility = View.VISIBLE
            // Aqui você pode adicionar lógica para mostrar o conteúdo de sono
            findViewById<ScrollView>(R.id.foodScroll).visibility = View.GONE
        }
    }

    private fun loadFoodItems() {
        container.removeAllViews() // Limpa os itens existentes

        getFeedingsCursor().use { cursor ->
            val dayMap = mutableMapOf<String, MutableList<FeedingRecord>>()

            while (cursor.moveToNext()) {
                val startTime = cursor.getLong(cursor.getColumnIndexOrThrow("start_time"))
                val day = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(startTime))

                val record = FeedingRecord(
                    id = cursor.getString(cursor.getColumnIndexOrThrow("id")),
                    type = cursor.getString(cursor.getColumnIndexOrThrow("type")),
                    startTime = startTime,
                    endTime = cursor.getLong(cursor.getColumnIndexOrThrow("end_time")),
                    volumeMl = if (cursor.isNull(cursor.getColumnIndexOrThrow("volume_ml"))) 0
                    else cursor.getInt(cursor.getColumnIndexOrThrow("volume_ml")),
                    notes = cursor.getString(cursor.getColumnIndexOrThrow("notes"))
                )

                if (!dayMap.containsKey(day)) {
                    dayMap[day] = mutableListOf()
                }
                dayMap[day]?.add(record)
            }

            dayMap.forEach { (day, records) ->
                records.sortedByDescending { it.startTime }.forEach { record ->
                    addMealItem(
                        dayLabel = day,
                        startTime = record.startTime,
                        endTime = record.endTime,
                        volumeMl = record.volumeMl,
                        showDelete = true
                    )
                }
            }
        }
    }
    private fun addMealItem(dayLabel: String, startTime: Long, endTime: Long, volumeMl: Int, showDelete: Boolean) {
        // Verifica se já existe cabeçalho para este dia
        var dayHeader: TextView? = null
        for (i in 0 until container.childCount) {
            val view = container.getChildAt(i)
            if (view is TextView && view.tag == "day_header_$dayLabel") {
                dayHeader = view
                break
            }
        }

        // Adiciona cabeçalho se não existir
        if (dayHeader == null) {
            dayHeader = TextView(this).apply {
                text = dayLabel
                textSize = 16f
                setTextColor(Color.BLACK)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 24.dpToPx(), 0, 8.dpToPx())
                }
                tag = "day_header_$dayLabel"
            }
            container.addView(dayHeader)
        }

        // Infla o layout do item exatamente como você forneceu
        val mealItem = LayoutInflater.from(this).inflate(R.layout.activity_item_food_report, container, false)

        // Configura os textos
        val startTimeText = timeFormat.format(Date(startTime))
        val endTimeText = timeFormat.format(Date(endTime))
        val duration = (endTime - startTime) / (60 * 1000) // em minutos

        mealItem.findViewById<TextView>(R.id.time_range).text =
            "$startTimeText às $endTimeText - ${duration / 60}h e ${duration % 60}min"

        mealItem.findViewById<TextView>(R.id.volume).text = "${volumeMl}ml"

        // Configura o botão de deletar
        val deleteButton = mealItem.findViewById<ImageView>(R.id.delete_button)
        if (showDelete) {
            deleteButton.visibility = View.VISIBLE
            deleteButton.setOnClickListener {
                container.removeView(mealItem)
                checkAndRemoveDayHeader(dayLabel)
            }
        } else {
            deleteButton.visibility = View.GONE
        }

        // Adiciona o item após o cabeçalho do dia
        val dayHeaderIndex = container.indexOfChild(dayHeader)
        container.addView(mealItem, dayHeaderIndex + 1)
    }

    private fun checkAndRemoveDayHeader(dayLabel: String) {
        var hasItemsForDay = false
        val dayHeader = container.findViewWithTag<TextView>("day_header_$dayLabel")

        // Verifica se ainda existem itens para este dia
        for (i in 0 until container.childCount) {
            val view = container.getChildAt(i)
            if (view.tag == "day_header_$dayLabel") continue

            if (i > container.indexOfChild(dayHeader)) {
                if (view is TextView && view.tag.toString().startsWith("day_header_")) break
                hasItemsForDay = true
                break
            }
        }

        if (!hasItemsForDay && dayHeader != null) {
            container.removeView(dayHeader)
        }
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}