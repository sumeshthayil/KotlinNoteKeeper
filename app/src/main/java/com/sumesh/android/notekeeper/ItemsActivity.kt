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
import androidx.lifecycle.ViewModelProviders
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
        val adapter = NoteRecyclerAdapter(this, itemsActivityViewModel.recentlyViewedNotes)
        adapter.setOnSelectedListener(this)
        adapter
    }

    private val itemsActivityViewModel by lazy {
        ViewModelProviders.of(this)[ItemsActivityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_items)
            setSupportActionBar(toolbar)

            fab.setOnClickListener { view ->
                startActivity(Intent(this, NoteActivity::class.java))
            }

        if(itemsActivityViewModel.isNewlyCreated && savedInstanceState!=null){
            itemsActivityViewModel.restoreState(savedInstanceState)
        itemsActivityViewModel.isNewlyCreated = false
        }
        handleDisplaySelection(itemsActivityViewModel.navDrawerDisplaySelection)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            drawer_layout.addDrawerListener(toggle)
            toggle.syncState()

            nav_view.setNavigationItemSelectedListener(this)
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        itemsActivityViewModel.saveState(outState)

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
                R.id.nav_notes,
                R.id.nav_courses,
                R.id.nav_recent_notes-> {
                    handleDisplaySelection(item.itemId)
                    itemsActivityViewModel.navDrawerDisplaySelection = item.itemId
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

    fun handleDisplaySelection(itemId:Int) {
        when (itemId) {
            R.id.nav_notes -> {
                displayNotes()
            }
            R.id.nav_courses -> {
                displayCourses()
            }
            R.id.nav_recent_notes -> {
                displayRecentlyViewedNotes()
            }
        }
    }

    override fun onNoteSelected(note: NoteInfo) {
        itemsActivityViewModel.addToRecentlyViewedNotes(note)
    }




        private fun handleSelection(stringId: Int) {
            Snackbar.make(listItems, stringId, Snackbar.LENGTH_LONG).show()
        }
    }
