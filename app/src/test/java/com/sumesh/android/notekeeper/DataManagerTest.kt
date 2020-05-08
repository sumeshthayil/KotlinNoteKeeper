package com.sumesh.android.notekeeper

import org.junit.Test

import org.junit.Assert.*

class DataManagerTest {

    @Test
    fun addNote() {
        val course = DataManager.courses.get("android_async")!!
        val noteTitle = "This is a test note"
        val noteText = "A sample text for the test note"

        val index = DataManager.addNote(course, noteTitle, noteText)

        val note = DataManager.notes[index]

    }
}