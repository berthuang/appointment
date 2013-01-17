package com.android.appointment.ui;

import com.android.appointment.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class AppointmentMainActivity extends Activity {

    private GridView mGrid;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_main);
        mContext = getApplicationContext();
        mGrid = (GridView)findViewById(R.id.grid);
        TextView textView= (TextView)findViewById(R.id.textView1);
        textView.requestFocus();

        int[] iconResList = new int[] {
                R.drawable.add, R.drawable.received_invitation,
                R.drawable.home_manage, R.drawable.look
        };
        String[] title = {"约饭局","看请贴","地址管理","查看属性"};
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 4; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", iconResList[i]);
            map.put("ItemText", title[i]);
            lstImageItem.add(map);
        }

        SimpleAdapter saImageItems = new SimpleAdapter(this, lstImageItem, R.layout.shortcut_item,
                new String[] {
                        "ItemImage", "ItemText"
                }, new int[] {
                        R.id.shortcut_icon, R.id.shortcut_label
                });
        mGrid.setAdapter(saImageItems);
        mGrid.setOnItemClickListener(new OnItemClickListener(){

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0:
                        startActivity(new Intent(mContext,
                                AppointmentEditActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(mContext,AppointmentListActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(mContext, SelectAddressActivity.class));
                }


            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
       } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }
}
