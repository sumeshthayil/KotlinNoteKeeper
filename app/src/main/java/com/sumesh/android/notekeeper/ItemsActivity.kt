package com.sumesh.android.notekeeper

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jwhh.notekeeper.CourseRecyclerAdapter
import kotlinx.android.synthetic.main.activity_items.*
import kotlinx.android.synthetic.main.content_items.*
import kotlinx.android.synthetic.main.app_bar_items.*

class ItemsActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        NoteRecyclerAdapter.OnNoteSelectedListener {

    private val maxRecentlyViewedNotes = 5
    val recentlyViewedNotes = ArrayList<NoteInfo>(maxRecentlyViewedNotes)

    private val noteLayoutManager by lazy {
        LinearLayoutManager(this)
    }
    private val noteRecyclerAdapter by lazy {
        val adapter = NoteRecyclerAdapter(this, DataManager.loadNotes())
        adapter.setOnSelectedListener(this)
        adapter
    }
    
    private val courseLayoutManager by lazy {
        GridLayoutManager(this, 2)
    }
    
    private val mCourseRecyclerAdapter by lazy { 
        CourseRecyclerAdapter(this,DataManager.courses.values.toList())
    }
    
    private val recentlyViewedNoteRecyclerAdapter by lazy {
        val adapter = NoteRecyclerAdapter(this, recentlyViewedNotes)
        adapter.setOnSelectedListener(this)
        adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_items)
            setSupportActionBar(toolbar)

            fab.setOnClickListener { view ->
                startActivity(Intent(this, NoteActivity::class.java))
            }

        displayNotes()
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            drawer_layout.addDrawerListener(toggle)
            toggle.syncState()

            nav_view.setNavigationItemSelectedListener(this)
        }

    private fun displayNotes() {
        listItems.layoutManager = noteLayoutManager
        listItems.adapter = noteRecyclerAdapter
        nav_view.menu.findItem(R.id.nav_notes).isEnabled=true
    }
    
    private fun displayCourses(){
        listItems.layoutManager = courseLayoutManager
        listItems.adapter = mCourseRecyclerAdapter
        nav_view.menu.findItem(R.id.nav_courses).isEnabled=true
    }

    private fun displayRecentlyViewedNotes() {
        listItems.layoutManager = noteLayoutManager
        listItems.adapter = recentlyViewedNoteRecyclerAdapter

        nav_view.menu.findItem(R.id.nav_recent_notes).isChecked = true
    }
    override fun onResume() {
            super.onResume()
            listItems.adapter?.notifyDataSetChanged()
        }

        override fun onBackPressed() {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }
        }
        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            // Inflate the menu; this adds items to the action bar if it is present.
            menuInflater.inflate(R.menu.items, menu)
            return true
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            when (item.itemId) {
                R.id.action_settings -> return true
                else -> return super.onOptionsItemSelected(item)
            }
        }
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            // Handle navigation view item clicks here.
            when (item.itemId) {
                R.id.nav_notes -> {
                    displayNotes()
                }
                R.id.nav_courses -> {
                    displayCourses()
                }
            R.id.nav_recent_notes -> {
                displayRecentlyViewedNotes()
            }
                R.id.nav_share -> {
                    handleSelection(R.string.nav_share_message)
                }
                R.id.nav_send -> {
                    handleSelection(R.string.nav_send_message)
                }
        }

            drawer_layout.closeDrawer(GravityCompat.START)
            return true
    }
    override fun onNoteSelected(note: NoteInfo) {
        addToRecentlyViewedNotes(note)
    }


    fun addToRecentlyViewedNotes(note: NoteInfo) {
        // Check if selection is already in the list
        val existingIndex = recentlyViewedNotes.indexOf(note)
        if (existingIndex == -1) {
            // it isn't in the list...
            // Add new one to beginning of list and remove any beyond max we want to keep
            recentlyViewedNotes.add(0, note)
            for (index in recentlyViewedNotes.lastIndex downTo maxRecentlyViewedNotes)
                recentlyViewedNotes.removeAt(index)
        } else {
            // it is in the list...
            // Shift the ones above down the list and make it first member of the list
            for (index in (existingIndex - 1) downTo 0)
                recentlyViewedNotes[index + 1] = recentlyViewedNotes[index]
            recentlyViewedNotes[0] = note
        }
    }

        private fun handleSelection(stringId: Int) {
            Snackbar.make(listItems, stringId, Snackbar.LENGTH_LONG).show()
        }
    }
