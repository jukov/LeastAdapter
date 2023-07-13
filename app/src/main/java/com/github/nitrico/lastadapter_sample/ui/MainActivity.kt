package com.github.nitrico.lastadapter_sample.ui

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.github.nitrico.lastadapter_sample.data.Data
import com.github.nitrico.lastadapter_sample.data.Header
import info.jukov.leastadapter_sample.R
import java.util.Random

class MainActivity : AppCompatActivity() {

    private val random = Random()

    private val randomPosition: Int
        get() = random.nextInt(Data.items.size-1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val viewPager = findViewById<ViewPager>(R.id.pager)
        val tabLayout = findViewById<TabLayout>(R.id.tabs)

        setSupportActionBar(toolbar)

        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu) = consume { menuInflater.inflate(R.menu.main, menu) }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.addFirst -> consume {
            Data.items.add(0, Header("New Header"))
        }
        R.id.addLast -> consume {
            Data.items.add(Data.items.size, Header("New header"))
        }
        R.id.addRandom -> consume {
            Data.items.add(randomPosition, Header("New Header"))
        }
        R.id.removeFirst -> consume {
            if (Data.items.isNotEmpty()) Data.items.removeAt(0)
        }
        R.id.removeLast -> consume {
            if (Data.items.isNotEmpty()) Data.items.removeAt(Data.items.size-1)
        }
        R.id.removeRandom -> consume {
            if (Data.items.isNotEmpty()) Data.items.removeAt(randomPosition)
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun consume(f: () -> Unit): Boolean {
        f()
        return true
    }

    class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getCount() = 2
        override fun getItem(i: Int) = if (i == 0) KotlinListFragment() else JavaListFragment()
        override fun getPageTitle(i: Int) = if (i == 0) "Kotlin" else "Java"
    }

}
