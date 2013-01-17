package com.android.appointment.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View;

import com.android.appointment.R;

public class AppointmentAffirmActivity extends Activity {

    private TextView mInvitationTitleText;
    private TextView mAppointmentTimeText;
    private TextView mAppointmentDestinationText;
    private TextView mAttendeesText;
    private ImageButton mLaunchCalendarImageBtn;
    private ImageButton mLaunchMapImageBtn;
    private CheckBox mIsShareLocationChkBox;
    private Button mAcceptBtn;
    private Button mRejectBtn;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_affirm_layout);
    }
}