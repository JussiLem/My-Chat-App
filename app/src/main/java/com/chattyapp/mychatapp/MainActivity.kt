package com.chattyapp.mychatapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.chattyapp.mychatapp.fragment.MyPostsFragment
import com.chattyapp.mychatapp.fragment.MyTopPostsFragment
import com.chattyapp.mychatapp.fragment.RecentPostFragment
import com.chattyapp.timber.Timber
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()  {

    private lateinit var pagerAdapter: FragmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag(TAG)
        Timber.d { "Activity Created" }
        setContentView(R.layout.activity_main)

        // Create the adapter that will return a fragment for each section
        pagerAdapter = object : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            private val fragments = arrayOf<Fragment>(
                RecentPostFragment(),
                MyPostsFragment(),
                MyTopPostsFragment())

            private val fragmentNames = arrayOf(
                getString(R.string.heading_recent),
                getString(R.string.heading_my_posts),
                getString(R.string.heading_my_top_posts))

            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

            override fun getCount() = fragments.size

            override fun getPageTitle(position: Int): CharSequence? {
                return fragmentNames[position]
            }
        }

        // Set up the ViewPager with the sections adapter.
        container.adapter = pagerAdapter
        tabs.setupWithViewPager(container)
        // Button launches NewPostActivity
        fabNewPost.setOnClickListener {
            startActivity(Intent(this, NewPostActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = item.itemId
        return if (i == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    companion object {

        private const val TAG = "MainActivity"
    }
}
