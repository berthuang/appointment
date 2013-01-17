package com.android.appointment.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

import com.android.appointment.R;

public class AppointmentEditActivity extends Activity {
    private Button mCancelButton;
    private Button mSendButton;
    private ImageButton mPickContactButton;
    private ImageButton mPickLocationButton;
    private EditText mRecipientEditor;

    private ArrayList<Recipient> mRecipients = new ArrayList<Recipient>();;

    private static final int REQUEST_CODE_PICK_CONTACT = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
        initUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                returnToMainActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initUI() {
        setContentView(R.layout.appointment_editor_layout);

        mCancelButton = (Button)findViewById(R.id.btn_cancel);
        if (mCancelButton != null) {
            mCancelButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnToMainActivity();
                }

            });
        }

        mSendButton = (Button)findViewById(R.id.btn_send);

        mPickContactButton = (ImageButton)findViewById(R.id.btn_add_recipients);
        if (mPickContactButton != null) {
            mPickContactButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startContactPicker();
                }

            });
        }

        mPickLocationButton = (ImageButton)findViewById(R.id.btn_add_location);

        mRecipientEditor = (EditText)findViewById(R.id.recipients_editor);
        updateRecipientEditor();
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void returnToMainActivity() {
        this.finish();
    }

    private void startContactPicker() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_PICK);
        i.setData(ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, REQUEST_CODE_PICK_CONTACT);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PICK_CONTACT:
                if (!(resultCode == RESULT_OK) || (data == null)) {
                    return;
                }
                Uri uri = data.getData();
                GetContactFromUriAsyncTask getContactTask = new GetContactFromUriAsyncTask(this, uri);
                getContactTask.execute();

                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void updateRecipientEditor() {
        if (mRecipientEditor != null) {
            StringBuilder displayTextBuilder = new StringBuilder();
            if (mRecipients != null) {
                for (Recipient recipient : mRecipients) {
                    displayTextBuilder.append(recipient.toString()).append(",");
                }
            }

            mRecipientEditor.setText(displayTextBuilder.toString());
        }
    }

    private class Recipient {
        public String mContactId = null;
        public String mPersonName = null;
        public String mPersonAddress = null;
        public String mPhotoId = null;

        public Recipient(String id, String personName, String personAddress, String photoId) {
            mContactId = id;
            mPersonName = personName;
            mPersonAddress = personAddress;
            mPhotoId = photoId;
        }

        public String toString() {
            if (!TextUtils.isEmpty(mPersonName)) {
                return mPersonName;
            } else {
                return mPersonAddress;
            }
        }
    }

    private class GetContactFromUriAsyncTask extends AsyncTask<Void, Void, Boolean> {
        Context mContext;
        Uri mUri;

        public GetContactFromUriAsyncTask(Context context, Uri uri) {
            mContext = context;
            mUri = uri;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Cursor cursor = getContentResolver().query(mUri, null, null, null, null);

            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        String contactId = cursor.getString(cursor
                                .getColumnIndex(ContactsContract.Contacts._ID));
                        String personName = cursor.getString(cursor
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String photoId = cursor.getString(cursor
                                .getColumnIndex(ContactsContract.Contacts.PHOTO_ID));

                        String hasPhone = cursor.getString(cursor
                                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        String phoneNumber = "";
                        if (hasPhone.equalsIgnoreCase("1")) {
                            hasPhone = "true";
                        } else {
                            hasPhone = "false";
                        }
                        if (Boolean.parseBoolean(hasPhone)) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                            + " = " + contactId, null, null);
                            if (phones.moveToNext()) {
                                phoneNumber = phones
                                        .getString(phones
                                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            }
                            phones.close();
                        }
                        Recipient recipient = new Recipient(contactId, personName, phoneNumber, photoId);
                        mRecipients.add(recipient);
                    }
                } catch(Exception e) {
                    return false;
                } finally {
                    cursor.close();
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                updateRecipientEditor();
            }
        }
    }
}
