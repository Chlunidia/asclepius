package com.dicoding.asclepius.ui

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.R

open class BaseActivity:AppCompatActivity() {

    fun customizeActionBar(){
        supportActionBar?.setCustomView(R.layout.toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.purple)))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val historyIntent = Intent(this, HistoryActivity::class.java)
        startActivity(historyIntent)
        return super.onOptionsItemSelected(item)
    }
}