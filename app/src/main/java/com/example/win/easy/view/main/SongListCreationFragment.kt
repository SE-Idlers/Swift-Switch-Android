package com.example.win.easy.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import butterknife.BindString
import butterknife.BindView
import butterknife.ButterKnife
import com.example.win.easy.R
import com.example.win.easy.enumeration.DataSource
import com.example.win.easy.exception.SongListToCreateAlreadyExistLocallyException
import com.example.win.easy.db.SongListDO
import com.example.win.easy.viewmodel.SongListViewModel
import com.qmuiteam.qmui.alpha.QMUIAlphaButton
import com.qmuiteam.qmui.widget.QMUITopBar
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog

class SongListCreationFragment(private val viewModelFactory: ViewModelProvider.Factory) : Fragment() {
    @BindView(R.id.newSongListCreationTopBar) lateinit var topBar: QMUITopBar
    @BindView(R.id.newSongListEditText) lateinit var newSongListEditText: EditText
    @BindView(R.id.newSongListCreationButton) lateinit var buttonToCreateNewSongList: QMUIAlphaButton
    @BindString(R.string.newSongListCreationTitleText) lateinit var topBarTitle: String
    @BindString(R.string.successfulSongListCreationTip) lateinit var successTip: String
    @BindString(R.string.unsuccessfulCreationTipForAlreadyExistedSongList) lateinit var alreadyExistedTip: String
    @BindString(R.string.unsuccessfulCreationTipForEmptySongListName) lateinit var emptyTip: String
    private val showTimeInMillis = 500
    private lateinit var songListViewModel: SongListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        songListViewModel = ViewModelProviders.of(this, viewModelFactory).get(SongListViewModel::class.java)
    }

    /**
     * 设置标题，设置“点击创建歌单”按钮
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_song_list_creation, container, false).also {
                ButterKnife.bind(this, it)
                topBar.setTitle(topBarTitle)
                setUpButtonToCreateNewSongList()
            }

    private fun setUpButtonToCreateNewSongList() =
            buttonToCreateNewSongList.setOnClickListener {
                newSongListEditText.text.toString().let {
                    if(it!="")
                        tryToCreate(SongListDO(name = it, source = DataSource.Local))
                    else
                        temporarilyShowUnsuccessfulCreationTip(emptyTip, showTimeInMillis)
                }
            }

    private fun tryToCreate(songListDO: SongListDO) {
        try {
            songListViewModel.insert(songListDO)
            temporarilyShowSuccessfulCreationTip(successTip, showTimeInMillis)
            returnToLastFragment()
        } catch (e: SongListToCreateAlreadyExistLocallyException) {
            temporarilyShowUnsuccessfulCreationTip(alreadyExistedTip, showTimeInMillis)
        }
    }

    private fun temporarilyShowSuccessfulCreationTip(tipWord: String?, showTimeInMillis: Int) =
        temporarilyShowTipDialog(tipWord, QMUITipDialog.Builder.ICON_TYPE_SUCCESS, showTimeInMillis)

    private fun returnToLastFragment() = Navigation.findNavController(view!!).navigateUp()

    private fun temporarilyShowUnsuccessfulCreationTip(tipWord: String?, showTimeInMillis: Int) =
        temporarilyShowTipDialog(tipWord, QMUITipDialog.Builder.ICON_TYPE_FAIL, showTimeInMillis)

    private fun temporarilyShowTipDialog(tipWord: String?, iconType: Int, showTimeInMillis: Int) =
            buildDialog(tipWord, iconType).run {
                show()
                view!!.postDelayed({ dismiss() }, showTimeInMillis.toLong())
            }

    private fun buildDialog(tipWord: String?, iconType: Int) =
            QMUITipDialog.Builder(context)
                    .setIconType(iconType)
                    .setTipWord(tipWord)
                    .create()
}