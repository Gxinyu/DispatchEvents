package com.example.dispatchevents;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.dispatchevents.widget.DragViewGroup;

public class DragViewGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_view_group);
        final DragViewGroup dragviewgroup = findViewById(R.id.dragviewgroup);

        TextView textView=new TextView(this);


        dragviewgroup.setOnVideoDragListener(new OnVideoDragListener() {
            @Override
            public void onJoinRoom(String roomid) {
                //做跳转
            }

            @Override
            public void onCloseRoom(String roomid) {
                dragviewgroup.setVisibility(View.GONE);
            }
        });
    }
}
