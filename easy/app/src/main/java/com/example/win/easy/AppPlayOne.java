package com.example.win.easy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AppPlayOne extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appplayone);
        final Button btnplay1 = (Button) findViewById(R.id.list);
        //切换到
        btnplay1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent=new Intent(AppPlayOne.this,AppListOne.class);
                                            startActivity(intent);
                                        }
                                    }
        );
    }
}
