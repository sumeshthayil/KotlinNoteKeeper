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

class ItemsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val mLinearLayoutManager by lazy {
        LinearLayoutManager(this)}

    private val mNoteRecyclerAdapter by lazy {
        NoteRecyclerAdapter(this, DataManager.notes) }
    
    private val mGridLayoutManager by lazy { 
        GridLayoutManager(this, resources.getInteger(R.integer.course_grid_span))
    }
    
    private val mCourseRecyclerAdapter by lazy { 
        CourseRecyclerAdapter(this,DataManager.courses.values.toList())
    }
    

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_items)
            val toolbar: Toolbar = findViewById(R.id.toolbar)
            setSupportActionBar(toolbar)

            val fab: FloatingActionButton = findViewById(R.id.fab)
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
        listItems.layoutManager = mLinearLayoutManager
        listItems.adapter = mNoteRecyclerAdapter
        nav_view.menu.findItem(R.id.nav_notes).isEnabled=true
    }
    
    private fun displayCourses(){
        listItems.layoutManager = mGridLayoutManager
        listItems.adapter = mCourseRecyclerAdapter
        nav_view.menu.findItem(R.id.nav_courses).isEnabled=true
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
                R.id.nav_share -> {
                    handleSelection("Share")
                }
                R.id.nav_send -> {
                    handleSelection("Send")
                }
            }
            drawer_layout.closeDrawer(GravityCompat.START)
            return true
        }

        private fun handleSelection(message: String) {
            Snackbar.make(listItems, message, Snackbar.LENGTH_LONG).show()
        }
    }
