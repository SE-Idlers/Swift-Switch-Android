package com.example.win.easy.view.lock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import com.example.win.easy.R
import com.example.win.easy.display.DisplayService

class DisplayFragment(private val displayService: DisplayService) : Fragment() {

    @BindView(R.id.start) lateinit var btnPause: ImageButton
    @BindView(R.id.previous) lateinit var btnPrevious: ImageButton
    @BindView(R.id.next) lateinit var btnNext: ImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_display, container, false).also {
                ButterKnife.bind(this, it)
                initButtons()
            }

    private fun initButtons() {
        initPauseButton()
        initPreviousButton()
        initNextButton()
    }

    private fun initPauseButton() =
            btnPause.setOnClickListener {
                if (displayService.whetherPlaying()) {
                    displayService.pause()
                    updateBeginView()
                } else {
                    displayService.start()
                    updatePauseView()
                }
            }

    private fun initPreviousButton() =
            btnPrevious.setOnClickListener {
                if (!displayService.whetherPlaying())
                    updatePauseView()
                displayService.previous()
            }

    private fun initNextButton() =
            btnNext.setOnClickListener {
                if (!displayService.whetherPlaying())
                    updatePauseView()
                displayService.next()
            }

    private fun updateBeginView() = btnPause.setImageResource(android.R.drawable.ic_media_play)
    private fun updatePauseView() = btnPause.setImageResource(android.R.drawable.ic_media_pause)
}