
package com.android.appointment.ui;

import com.android.appointment.R;
import com.android.appointment.provider.DatabaseExtra.CONVERSATION_TABLE;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppointmentListActivity extends Activity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private CursorLoader mLoader;

    private ListView mAppointmentList;

    private AppointmentListViewAdapter mAppointmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        mAppointmentList = (ListView)findViewById(R.id.appointmentlist);
        if (mAppointmentList != null) {
            mAppointmentList.setItemsCanFocus(true);
            mAppointmentAdapter = new AppointmentListViewAdapter(getApplicationContext(), null,
                    false);
            mAppointmentList.setAdapter(mAppointmentAdapter);
            // mAppointmentList.setOnScrollListener(mConversationsAdapter);
            // mAppointmentList.setOnItemLongClickListener(this);
            // mAppointmentList.setOnItemClickListener(mNormalModeItemClickListener);
        }

        getLoaderManager().initLoader(0, null, this);
        initActionBar("约饭");
    }

    @SuppressLint("NewApi")
    private void initActionBar(String title) {
        ActionBar actionBar = getActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

            actionBar.setIcon(R.drawable.food);
            actionBar.setTitle(title);

        } else {
            setTitle(title);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appointment_list_menu, menu);
        Spinner spinner = (Spinner)menu.findItem(R.id.spinner).getActionView();
        ArrayList<String> list = new ArrayList<String>();
        list.add("我发出去饭局邀请");
        list.add("我收到的饭局邀请");
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return super.onCreateOptionsMenu(menu);
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] queryProjection = {
                CONVERSATION_TABLE._ID, CONVERSATION_TABLE.DATE, CONVERSATION_TABLE.DESCRIPTION,
                CONVERSATION_TABLE.LOCATION_DESCRIPTION, CONVERSATION_TABLE.LATITUDE
        };

        mLoader = new CursorLoader(getApplicationContext(), CONVERSATION_TABLE.CONTENT_URI,
                queryProjection, null, null, null);
        return mLoader;
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAppointmentAdapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
