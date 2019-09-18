package com.example.win.easy.view.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.win.easy.R;
import com.example.win.easy.web.service.LoginService;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <p>点app图标进来后见到的主界面</p>
 */
public class MainActivityFragment extends Fragment {
    @BindView(R.id.listview) QMUIGroupListView itemGroup;
    @BindView(R.id.topbar) QMUITopBar qmuiTopBar;
    @BindString(R.string.textOnItemToAllSong) String textOnItemToAllSong;
    @BindString(R.string.textOnItemToAllSongList) String textOnItemToAllSongList;
    @BindString(R.string.mainActivityFragmentTopBarTitle) String topBarTitle;

    private LoginService loginService;

    public MainActivityFragment(LoginService loginService){
        this.loginService=loginService;
    }

    /**
     * <ol>
     *     <li>初始化顶栏（包括标题和按钮）</li>
     *     <li>初始化前往所有歌单的item</li>
     *     <li>...........所有歌曲的....</li>
     * </ol>
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View thisView=inflater.inflate(R.layout.fragment_main_activity,container,false);
        ButterKnife.bind(this,thisView);

        setUpTopBar();

        QMUICommonListItemView itemToAllSong = initItemToAllSong();
        QMUICommonListItemView itemToAllSongList = initItemToAllSongList();
        showItems(itemToAllSong, itemToAllSongList);

        return thisView;
    }

    private void setUpTopBar(){
        qmuiTopBar.setTitle(topBarTitle);
        setUpButtonToLogin();
    }

    private void setUpButtonToLogin(){
        QMUIAlphaImageButton loginButton= qmuiTopBar.addLeftImageButton(R.drawable.ic_action_cloud, R.id.loginButtonId);
        loginButton.setOnClickListener(v->{
            if (!loginService.hasLogin())
                Navigation.findNavController(getView()).navigate(MainActivityFragmentDirections.actionMainActivityFragmentToLoginFragment());
        });
    }

    private QMUICommonListItemView initItemToAllSong(){
        QMUICommonListItemView itemView=newItem(textOnItemToAllSong, v -> Navigation.findNavController(getView()).navigate(MainActivityFragmentDirections.actionMainActivityFragmentToAllSongsFragment()));
        ImageButton buttonToAddLocalSong = initButtonToAddLocalSong();
        addButtonTo(buttonToAddLocalSong,itemView);
        return itemView;
    }

    private QMUICommonListItemView newItem(String text, View.OnClickListener onClickListener){
        QMUICommonListItemView itemView= itemGroup.createItemView(text);
        itemView.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        itemView.setOnClickListener(onClickListener);
        return itemView;
    }

    private ImageButton initButtonToAddLocalSong(){
        return newImageButton(R.drawable.ic_action_name,v-> Navigation.findNavController(getView()).navigate(MainActivityFragmentDirections.actionMainActivityFragmentToPlaceholder()));
    }

    private ImageButton newImageButton(int imageResId, View.OnClickListener onClickListener){
        ImageButton imageButton=new ImageButton(getContext());
        imageButton.setImageResource(imageResId);
        imageButton.setOnClickListener(onClickListener);
        return imageButton;
    }

    private void addButtonTo(ImageButton imageButton,QMUICommonListItemView itemView){
        itemView.addAccessoryCustomView(imageButton);
    }

    private QMUICommonListItemView initItemToAllSongList(){
        QMUICommonListItemView itemView=newItem(textOnItemToAllSongList, v -> Navigation.findNavController(getView()).navigate(MainActivityFragmentDirections.actionMainActivityFragmentToAllSongListsFragment()));
        ImageButton buttonToCreateSongList = initButtonToCreateSongList();
        addButtonTo(buttonToCreateSongList,itemView);
        return itemView;
    }

    private ImageButton initButtonToCreateSongList(){
        return newImageButton(R.drawable.ic_action_name,v->Navigation.findNavController(getView()).navigate(MainActivityFragmentDirections.actionMainActivityFragmentToSongListCreationFragment()));
    }

    private void showItems(QMUICommonListItemView... itemViews){
        QMUIGroupListView.Section section= QMUIGroupListView.newSection(getContext());
        for (QMUICommonListItemView itemView:itemViews)
            section.addItemView(itemView,null);
        section.addTo(itemGroup);
    }
}
