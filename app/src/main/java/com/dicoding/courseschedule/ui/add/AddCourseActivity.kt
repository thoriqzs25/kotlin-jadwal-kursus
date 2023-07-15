package com.dicoding.courseschedule.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var viewModel: AddCourseViewModel
    private lateinit var ibStartTime: View
    private lateinit var ibEndTime: View

    private var startTime: String = ""
    private var endTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        supportActionBar?.title = getString(R.string.add_course)

        val factory = AddCourseViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory)[AddCourseViewModel::class.java]

        ibStartTime = findViewById<ImageButton>(R.id.ib_start_time)
        ibEndTime = findViewById<ImageButton>(R.id.ib_end_time)

        ibStartTime.setOnClickListener {
            showTimePicker("startPicker")
        }
        ibEndTime.setOnClickListener {
            showTimePicker("endPicker")
        }

        viewModel.saved.observe(this) {
            it.getContentIfNotHandled()?.let { isSave ->
                if (isSave) {
                    onBackPressed()
                } else {
                    Toast.makeText(applicationContext, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                val courseName =
                    findViewById<TextInputEditText>(R.id.ed_course_name).text.toString().trim()
                val lecturer =
                    findViewById<TextInputEditText>(R.id.ed_lecture).text.toString().trim()
                val note = findViewById<TextInputEditText>(R.id.ed_note).text.toString().trim()
                val day = findViewById<Spinner>(R.id.spinner).selectedItemPosition

                viewModel.insertCourse(courseName, day, startTime, endTime, lecturer, note)
                Toast.makeText(applicationContext, "added", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun showTimePicker(tag: String) {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(supportFragmentManager, tag)
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        when (tag) {
            "startPicker" -> {
                findViewById<TextView>(R.id.tv_start_time).text = timeFormat.format(calendar.time)
                startTime = timeFormat.format(calendar.time)
            }

            "endPicker" -> {
                findViewById<TextView>(R.id.tv_end_time).text = timeFormat.format(calendar.time)
                endTime = timeFormat.format(calendar.time)
            }
        }
    }
}