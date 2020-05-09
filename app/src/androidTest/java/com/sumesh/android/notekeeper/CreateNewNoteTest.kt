package com.sumesh.android.notekeeper

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.Matchers.*
import androidx.test.rule.ActivityTestRule
//import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.Espresso.pressBack

@RunWith(AndroidJUnit4::class)
class CreateNewNoteTest{
    @Rule @JvmField
    val noteListActivity = ActivityTestRule(NoteListActivity::class.java)

    @Test
    fun createNewNote(){

        val course = DataManager.courses["android_async"]

        onView(withId(R.id.fab)).perform(click())

        onView(withId(R.id.spinnerCourses)).perform(click())
        onData(allOf(instanceOf(CourseInfo::class.java), equalTo(course))).perform(click())

        val title = "This is a test note title"
        onView(withId(R.id.textNoteTitle)).perform(typeText(title))
        val text = "This is a test note text"
        onView(withId(R.id.textNoteText)).perform(typeText(text), closeSoftKeyboard())

        pressBack()

        val note = DataManager.notes.last()

        assertEquals(note.course, course)
        assertEquals(note.title, title)
        assertEquals(note.text, text)
    }
}