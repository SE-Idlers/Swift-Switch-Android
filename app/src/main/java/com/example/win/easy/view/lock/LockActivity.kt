package com.example.win.easy.view.lock

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.example.win.easy.R
import com.example.win.easy.dagger.SwiftSwitchApplication
import javax.inject.Inject

class LockActivity : AppCompatActivity() {
    private lateinit var displayFragment: DisplayFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var handWritingFragment: HandWritingFragment
    @Inject lateinit var fragmentFactory: FragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock)
        SwiftSwitchApplication.application.dashboardComponent.inject(this)

        supportFragmentManager.run {
            fragmentFactory=this@LockActivity.fragmentFactory
            displayFragment = findFragmentById(R.id.lockDisplay) as DisplayFragment
            searchFragment = findFragmentById(R.id.searchFragment) as SearchFragment
            handWritingFragment = findFragmentById(R.id.handWritingFragment) as HandWritingFragment
        }

        handWritingFragment.getObserved(this, Observer { recognitionResult: List<Char> ->
            println(recognitionResult)
            searchFragment.search(recognitionResult)
        })

        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
    }

    override fun onDestroy() {
        super.onDestroy()
        SwiftSwitchApplication.application.clearDashboardComponent()
    }
}