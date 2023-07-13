package com.github.nitrico.lastadapter_sample.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
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
        setSupportActionBar(findViewById(R.id.toolbar))

        savedInstanceState ?: supportFragmentManager.beginTransaction()
            .replace(R.id.container, KotlinListFragment())
            .commit()
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

}
