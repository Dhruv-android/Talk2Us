package com.dapp.talk2us.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dapp.talk2us.R
import com.dapp.talk2us.utils.PrefManager
import com.google.android.material.tabs.TabLayout

class WelcomeActivity : AppCompatActivity() {
    private var skip: Button? = null
    private var next: Button? = null
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private var layoutArray: IntArray? = null

    private val viewPagerChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            if (position == layoutArray?.size?.minus(1)) {
                skip?.visibility = View.GONE
                next?.text = getString(R.string.got_it)
            } else {
                skip?.visibility = View.VISIBLE
                next?.text = getString(R.string.got_it)
            }
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        if (PrefManager.getBoolean(R.string.first_time, true)) {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }
        skip = findViewById(R.id.welcome_skip)
        next = findViewById(R.id.welcome_next)
        tabLayout = findViewById(R.id.welcome_tab_layout)
        viewPager = findViewById(R.id.welcome_viewpager)
        layoutArray =
            intArrayOf(R.layout.welcome_slide1, R.layout.welcome_slide2, R.layout.welcome_slide3)

        val viewPagerAdapter = ViewPagerAdapter()
        viewPager?.adapter = viewPagerAdapter
        viewPager?.addOnPageChangeListener(viewPagerChangeListener)

        skip?.setOnClickListener {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }
        next?.setOnClickListener {
            val current = getLayoutPosition()
            val currentLayouts = layoutArray
            if (currentLayouts != null && current != null) {
                if (current < currentLayouts.size) {
                    //  to next screen
                    viewPager?.currentItem = current
                } else {
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                }
            }

        }
    }

    private fun getLayoutPosition(): Int? {
        return viewPager?.currentItem?.plus(1)
    }

    inner class ViewPagerAdapter : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater!!.inflate(layoutArray!![position], container, false)
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return layoutArray!!.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            if (`object` is View) container.removeView(`object`)
        }
    }
}
