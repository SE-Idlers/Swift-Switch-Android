package com.example.win.easy.tool;

import android.content.Context;
import android.content.DialogInterface;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

public class DialogTool {

    private DialogTool(){}

    public static void createMenuDialog(Context context, String title,String[] contents, DialogInterface.OnClickListener listener,int style){
        new QMUIDialog.MenuDialogBuilder(context)
                .setTitle(title)
                .addItems(contents, listener)
                .create(style)
                .show();
    }

    public static void createMultiCheckDialog(
            QMUIDialog.MultiCheckableDialogBuilder builder,
            String title,
            String[] contents,
            DialogInterface.OnClickListener itemListener,
            String pActionText,
            QMUIDialogAction.ActionListener pActionListener,
            String nActionText,
            QMUIDialogAction.ActionListener nActionListener,
            int style
            ){
        builder.setTitle(title).addItems(contents, itemListener);
        if (nActionText!=null)
            builder.addAction(nActionText,nActionListener);
        if (pActionText!=null)
            builder.addAction(pActionText,pActionListener);
        builder
                .create(style)
                .show();
    }
}
