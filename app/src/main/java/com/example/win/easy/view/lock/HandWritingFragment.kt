package com.example.win.easy.view.lock

import android.Manifest
import android.content.pm.PackageManager
import android.gesture.Gesture
import android.gesture.GestureOverlayView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.win.easy.R
import com.example.win.easy.recognization.PositionedImage
import com.example.win.easy.recognization.interfaces.RecognitionService
import java.util.*

class HandWritingFragment(private val recognitionService: RecognitionService) : Fragment() {
    private val gestureBoardIds = arrayOf(R.id.gesture1, R.id.gesture2, R.id.gesture3, R.id.gesture4)
    private val gestureBoards: MutableList<GestureOverlayView> = ArrayList()
    private val recognitionLiveData = MutableLiveData<List<Char>>()

    fun getObserved(lifecycleOwner: LifecycleOwner?, observer: Observer<in List<Char>>?) = recognitionLiveData.observe(lifecycleOwner!!, observer!!)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_hand_writing, container, false).also {
                setUpRecognitionAsset()
                setUpGestureBoardsOf(it)
            }

    private fun setUpRecognitionAsset() = recognitionService.setAssetManager(activity!!.assets)

    private fun setUpGestureBoardsOf(view: View) {
        for (gestureBoardId in gestureBoardIds) {
            view.findViewById<GestureOverlayView>(gestureBoardId).let {
                setUpGestureBoard(it)
                gestureBoards.add(it)
            }
        }
    }

    private fun setUpGestureBoard(gestureBoard: GestureOverlayView) =
            gestureBoard.apply{
                gestureStrokeWidth = 15f
                addOnGesturePerformedListener { thisBoard: GestureOverlayView, performedGesture: Gesture ->
                    if (authorityPermitted())
                        notifyObserver(recognize(thisBoard, performedGesture))
                    else
                        requestPermission()
                }
            }

    private fun authorityPermitted() = (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)

    private fun requestPermission() = ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 123)

    private fun recognize(thisBoard: GestureOverlayView, gesture: Gesture): List<Char> {
        val indexOfThisBoard = gestureBoards.indexOf(thisBoard).toLong()
        val imageToRecognize = PositionedImage.create(gesture, indexOfThisBoard)
        return recognitionService.receive(imageToRecognize)
    }

    private fun notifyObserver(recolonizationResult: List<Char>) {
        recognitionLiveData.value = recolonizationResult
    }
}