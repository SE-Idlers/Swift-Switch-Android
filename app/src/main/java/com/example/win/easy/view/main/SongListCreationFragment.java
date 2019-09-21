package com.example.win.easy.view.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.win.easy.R;
import com.example.win.easy.value_object.SongListVO;
import com.example.win.easy.viewmodel.SongListViewModel;
import com.qmuiteam.qmui.alpha.QMUIAlphaButton;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.win.easy.enumeration.DataSource.Local;

public class SongListCreationFragment extends Fragment {

    @BindView(R.id.newSongListCreationTopBar) QMUITopBar topBar;
    @BindView(R.id.newSongListEditText) EditText newSongListEditText;
    @BindView(R.id.newSongListCreationButton) QMUIAlphaButton buttonToCreateNewSongList;

    @BindString(R.string.newSongListCreationTitleText) String topBarTitle;
    @BindString(R.string.successfulSongListCreationTip) String successTip;
    @BindString(R.string.unsuccessfulCreationTipForAlreadyExistedSongList) String alreadyExistedTip;
    @BindString(R.string.unsuccessfulCreationTipForEmptySongListName) String emptyTip;
    @BindInt(R.integer.temporaryTipShowTimeInMillis) int showTimeInMillis;


    private ViewModelProvider.Factory viewModelFactory;
    private SongListViewModel songListViewModel;

    public SongListCreationFragment(ViewModelProvider.Factory viewModelFactory){
        this.viewModelFactory=viewModelFactory;
    }

    /**
     * <ol>
     *     <li>设置标题</li>
     *     <li>设置“点击创建歌单”按钮</li>
     * </ol>
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View thisView= inflater.inflate(R.layout.fragment_song_list_creation,container,false);
        ButterKnife.bind(this,thisView);

        songListViewModel=getViewModel();

        topBar.setTitle(topBarTitle);
        setUpButtonToCreateNewSongList();

        return thisView;
    }

    private SongListViewModel getViewModel(){
        return ViewModelProviders.of(this,viewModelFactory).get(SongListViewModel.class);
    }

    private void setUpButtonToCreateNewSongList(){
        buttonToCreateNewSongList.setOnClickListener(v->{
            String newSongListName=getNewSongListName();

            if(isNotEmpty(newSongListName)){
                SongListVO songListToCreate=buildSongList(newSongListName);
                tryToCreate(songListToCreate);
            }else
                temporarilyShowUnsuccessfulCreationTip(emptyTip,showTimeInMillis);
        });
    }

    private String getNewSongListName(){
        return newSongListEditText.getText().toString();
    }

    private boolean isNotEmpty(String string) {
        return !string.equals("");
    }

    private SongListVO buildSongList(String songListName){
        return SongListVO.builder()
                .name(songListName)
                .dataSource(Local)
                .build();
    }

    private void tryToCreate(SongListVO songListVO){
        try {
            songListViewModel.create(songListVO);
            temporarilyShowSuccessfulCreationTip(successTip,showTimeInMillis);
            returnToLastFragment();
        } catch (SongListToCreateAlreadyExistLocallyException e) {
            temporarilyShowUnsuccessfulCreationTip(alreadyExistedTip,showTimeInMillis);
        }
    }

    private void temporarilyShowSuccessfulCreationTip(String tipWord, int showTimeInMillis){
        temporarilyShowTipDialog(tipWord, QMUITipDialog.Builder.ICON_TYPE_SUCCESS,showTimeInMillis);
    }

    private void returnToLastFragment(){
        Navigation.findNavController(getView()).navigateUp();
    }

    private void temporarilyShowUnsuccessfulCreationTip(String tipWord, int showTimeInMillis){
        temporarilyShowTipDialog(tipWord,QMUITipDialog.Builder.ICON_TYPE_FAIL,showTimeInMillis);
    }

    private void temporarilyShowTipDialog(String tipWord,int iconType,  int showTimeInMillis){
        QMUITipDialog tipDialog=buildDialog(tipWord,iconType);
        tipDialog.show();
        dismissAfter(tipDialog,showTimeInMillis);
    }

    private QMUITipDialog buildDialog(String tipWord,int iconType){
        return new QMUITipDialog.Builder(getContext())
                .setIconType(iconType)
                .setTipWord(tipWord)
                .create();
    }

    private void dismissAfter(QMUITipDialog dialog, int showTimeInMillis){
        getView().postDelayed(dialog::dismiss,showTimeInMillis);
    }
}
