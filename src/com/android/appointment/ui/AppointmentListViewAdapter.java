package com.android.appointment.ui;

import com.android.appointment.R;
import com.android.appointment.provider.DatabaseExtra.CONVERSATION_TABLE;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class AppointmentListViewAdapter extends CursorAdapter{

    private LayoutInflater mLayoutInflater;

    private TextView mRecipientView;

    private TextView mReplyCountView;

    private TextView mAppoinmentContentView;

    public AppointmentListViewAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.listview_item, null);
        mRecipientView = (TextView)view.findViewById(R.id.list_item_receiver);
        mReplyCountView = (TextView)view.findViewById(R.id.list_item_reply_count);
        mAppoinmentContentView = (TextView)view.findViewById(R.id.list_item_content);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String secipientlist;
        int sReplycount;
        String sAppointmentContent;

        secipientlist = cursor.getString(cursor.getColumnIndex(CONVERSATION_TABLE.LATITUDE));
        sReplycount = cursor.getInt(cursor.getColumnIndex(CONVERSATION_TABLE.DATE));
        sAppointmentContent = cursor.getString(cursor.getColumnIndex(CONVERSATION_TABLE.DESCRIPTION));

        mRecipientView.setText(secipientlist);
        mReplyCountView.setText(sReplycount);
        mAppoinmentContentView.setText(sAppointmentContent);
    }

}
