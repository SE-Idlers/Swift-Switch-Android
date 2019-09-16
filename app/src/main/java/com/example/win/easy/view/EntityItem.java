package com.example.win.easy.view;

import android.content.Context;
import android.widget.LinearLayout;

import com.example.win.easy.R;
import com.example.win.easy.view.fragment.ListFragment;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;

/**
 * <p>主要拿来给{@link ListFragment}用的类，简化了下调用</p>
 * <p>在显示的时候，每个Item基本都是和相应的数据结构紧密相连的，比如歌曲，歌单这种。
 * 而“点击”后要执行的逻辑基本都要用到这个Item上的数据，原生的{@link android.view.View.OnClickListener}有个麻烦就是lambda函数只能丢一个参数
 * v（就是这个item本身的视图），也就是：
 * </p>
 * <p>v->{</p>
 * <p>&emsp;//logic</p>
 * <p>&emsp;//...</p>
 * <p>&emsp;//...</p>
 * <p>}</p>
 * <p>要想取用这个item上的数据就比较蛋疼，所以就做个封装，变成：</p>
 * <p>(data,v)->{</p>
 * <p>&emsp;//logic</p>
 * <p>&emsp;//...</p>
 * <p>&emsp;//...</p>
 * <p>}</p>
 * @param <E> 数据对象的类型
 */
public class EntityItem<E> extends QMUICommonListItemView {

    public EntityItem(Context context, E entity, OnClickFunc<E> onClickFunc) {
        super(context);
        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, QMUIResHelper.getAttrDimen(getContext(), R.attr.qmui_list_item_height)));
        setOnClickListener(v -> onClickFunc.onclick(entity,this));
    }
}
