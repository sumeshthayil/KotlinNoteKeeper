package com.sumesh.android.notekeeper

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class NoteActivity : AppCompatActivity() {
    private val tag = this::class.simpleName
    private var notePosition = POSITION_NOT_SET

    val locManager = PseudoLocationManager(this) {lat, lon ->
        Log.d(tag, "Location callback Lat:$lat Lon:$lon")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val adapterCourses = ArrayAdapter<CourseInfo>(this,
            android.R.layout.simple_spinner_item,
            DataManager.courses.values.toList())
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerCourses.adapter = adapterCourses

        notePosition = savedInstanceState?.getInt(NOTE_POSITION, POSITION_NOT_SET) ?:
                intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET)

        if(notePosition != POSITION_NOT_SET)
            displayNote()
        else {
            createNewNote()
        }
        Log.d(tag,"onCreate" )
    }

    override fun onStart() {
        super.onStart()
        locManager.start()
    }

    override fun onStop() {
        locManager.stop()
        super.onStop()
    }

    private fun createNewNote() {
        notePosition = DataManager.addNote(NoteInfo())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(NOTE_POSITION, notePosition)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_next -> {
                if(DataManager.isLastNoteId(notePosition)) {
                    val message = "No more notes"
                    showMessage(message)
                } else {
                    moveNext()
                }
                true
            }
            R.id.action_get_together -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun displayNote() {
        Log.i(tag, "Displaying note for position $notePosition")
        val note = DataManager.loadNote(notePosition)
        textNoteTitle.setText(note.title)
        textNoteText.setText(note.text)

        val coursePosition = DataManager.courses.values.indexOf(note.course)
        spinnerCourses.setSelection(coursePosition)
    }

    private fun showMessage(message: String) {
        Snackbar.make(textNoteTitle, message, Snackbar.LENGTH_LONG).show()
    }


    private fun moveNext() {
        ++notePosition
        displayNote()
        invalidateOptionsMenu()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if(DataManager.isLastNoteId(notePosition)) {
            val menuItem = menu?.findItem(R.id.action_next)
            if (menuItem!= null){
                menuItem.icon = getDrawable(R.drawable.ic_block_white_24dp)
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onPause() {
        super.onPause()
        saveNote()
        Log.d(tag,"onPause" )
    }

    private fun saveNote() {
        val note = DataManager.loadNote(notePosition)
        note.title = textNoteTitle.text.toString()
        note.text = textNoteText.text.toString()
        note.course = spinnerCourses.selectedItem as CourseInfo
    }
}
