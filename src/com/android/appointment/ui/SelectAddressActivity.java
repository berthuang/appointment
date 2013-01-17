
package com.android.appointment.ui;

import com.android.appointment.constant.Constant;
import com.android.appointment.overlay.DestinationOverlay;
import com.android.appointment.overlay.TransparentOverlay;
import com.android.appointment.overlay.TransparentOverlay.OnOverlayTapListener;
import com.android.appointment.provider.DatabaseExtra.CONVERSATION_TABLE;
import com.android.appointment.provider.DatabaseExtra.RECIPIENT_TABLE;
import com.android.appointment.utils.MySearchListener;
import com.android.appointment.AppointmentApp;
import com.android.appointment.R;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKLocationManager;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.OverlayItem;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class SelectAddressActivity extends MapActivity {

    private static final int SHOW_DESTINATION_OVERLAY = 1;

    View mDumyView;
    EditText mDestinationText;
    ImageButton mSelectDestinationBtn;
    Button mSelectDestinationPoint;
    Button mSelectDestinationKeywordsBtn;
    Button mSelectDestinationFavoriteBtn;

    MapView mMapView;
    public MKSearch mMKSearch;

    BMapManager mbMapManager;
    private MKLocationManager mkLocationManager;
    private MapController mMapController;
    private TransparentOverlay mTransparentOverlay;

    View mPopView;

    String mKeyWords;
    OverlayItem mDestinationItem;

    Handler mWorker = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case SHOW_DESTINATION_OVERLAY:
                    showDestinationOverlay();
                    break;
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        initManager();
        initMapActivity(mbMapManager);
        initMapView();
        initBtnListener();
    }

    private void initView() {
        mDumyView = findViewById(R.id.dumy_layout);
        mDestinationText = (EditText)findViewById(R.id.appointment_destination_text_view);
        mSelectDestinationBtn = (ImageButton)findViewById(R.id.select_destination_from_map_btn);
        mSelectDestinationPoint = (Button)findViewById(R.id.btn_navsearch_mapselect);
        mSelectDestinationKeywordsBtn = (Button)findViewById(R.id.btn_navsearch_keywrds);
        mSelectDestinationFavoriteBtn = (Button)findViewById(R.id.btn_navsearch_favorite);

        mMapView = (MapView)findViewById(R.id.bmapsView);

    }

    private void initBtnListener() {
        mSelectDestinationKeywordsBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                removeOverlays();
                showKeywordsView();
            }
        });

        mSelectDestinationPoint.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                removeOverlays();
                showSelectPointLayer();
            }
        });

        mSelectDestinationFavoriteBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ContentResolver provider = SelectAddressActivity.this.getContentResolver();
                ContentValues values = new ContentValues();
                long id = System.currentTimeMillis();
                values.put(CONVERSATION_TABLE.CONVERSATION_ID, id);
                values.put(CONVERSATION_TABLE.DATE, id);
                values.put(CONVERSATION_TABLE.LONGITUDE, 12.1235);
                values.put(CONVERSATION_TABLE.LATITUDE, 56.2589);
                values.put(CONVERSATION_TABLE.LOCATION_DESCRIPTION, "plac");
                values.put(CONVERSATION_TABLE.DESCRIPTION, "A new appointment");
                provider.insert(CONVERSATION_TABLE.CONTENT_URI, values);

                values.clear();
                values.put(RECIPIENT_TABLE.CONVERSATION_ID, id);
                values.put(RECIPIENT_TABLE.SHARE_WITH_OTHERS, RECIPIENT_TABLE.NO_SHARE_LOCATION_WITH_OTHERS);
                values.put(RECIPIENT_TABLE.PARTICIPANT, "13832612589");
                provider.insert(RECIPIENT_TABLE.CONTENT_URI, values);

                values.clear();
                values.put(RECIPIENT_TABLE.CONVERSATION_ID, id);
                values.put(RECIPIENT_TABLE.SHARE_WITH_OTHERS, RECIPIENT_TABLE.SHARE_LOCATION_WITH_OTHERS);
                values.put(RECIPIENT_TABLE.PARTICIPANT, "18710253648");
                provider.insert(RECIPIENT_TABLE.CONTENT_URI, values);
            }
        });

        mSelectDestinationBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
//                mMapView.getOverlays().clear();
//                mMapView.getOverlays().add(mTransparentOverlay);
//                mMapView.invalidate();
                SelectAddressActivity.this.startActivity(new Intent(SelectAddressActivity.this,
                        AppointmentEditActivity.class));
            }
        });
    }

    private void initMapView() {
        mMapController = mMapView.getController();
        mMapController.setZoom(15);
        mMapView.setBuiltInZoomControls(true);
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    public void initManager() {
        AppointmentApp app = (AppointmentApp)this.getApplication();
        if (app.mapManager == null) {
            app.mapManager = new BMapManager(getApplication());
            app.mapManager.init(Constant.BAIDU_MAP_KEY, new AppointmentApp.MyGeneralListener());
        }
        mbMapManager = app.mapManager;
        mkLocationManager = mbMapManager.getLocationManager();
        mkLocationManager.requestLocationUpdates(mLocationListener);
        mkLocationManager.setNotifyInternal(30, 5);
        mMKSearch = new MKSearch();
        Drawable marker = getResources().getDrawable(R.drawable.point_start);
        marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
        mTransparentOverlay = new TransparentOverlay(marker);

        mMKSearch.init(mbMapManager, new MySearchListener(this, mMapView, mTransparentOverlay));
    }

    private void showKeywordsView() {
        final EditText textEntryView = new EditText(SelectAddressActivity.this);
        textEntryView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        textEntryView.setHint("请输入关键字后点击地图搜索");
        new AlertDialog.Builder(SelectAddressActivity.this)
            .setIconAttribute(android.R.attr.alertDialogIcon)
            .setTitle("请输入关键字")
            .setView(textEntryView)
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    hideInputMethod(false);
                    String text = textEntryView.getText().toString();
                    if (!TextUtils.isEmpty(text)) {
                        // add layer to mapview to let user search address
                        mKeyWords = text;
                        mTransparentOverlay.setonOverlayTapListener(new OnOverlayTapListener() {

                            public void onTap(OverlayItem overlayItem) {
                                popupView(overlayItem);
                            }


                            public void onTap(GeoPoint p, MapView mapView) {
                                removePopupView();
                                mMKSearch.poiSearchNearBy(mKeyWords, p, 200);
                            }
                        });
                        mTransparentOverlay.clearOverlay();
                        mMapView.getOverlays().add(mTransparentOverlay);
                        removePopupView();
                        mMapView.invalidate();
                        mMapView.requestFocus();
                    }
                }
            })
            .create().show();
    }

    private void showSelectPointLayer() {
        mTransparentOverlay.setonOverlayTapListener(new OnOverlayTapListener() {
            public void onTap(final OverlayItem overlayItem) {
                new AlertDialog.Builder(SelectAddressActivity.this)
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setTitle(overlayItem.getTitle())
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mDestinationText.setText(overlayItem.getTitle() + " " + overlayItem.getSnippet());
                        mDestinationItem = overlayItem;
                        Message msg = mWorker.obtainMessage(SHOW_DESTINATION_OVERLAY);
                        mWorker.dispatchMessage(msg);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Now just do nothing
                    }
                }).create()
                .show();
            }

            public void onTap(GeoPoint p, MapView mapView) {
                mTransparentOverlay.clearOverlay();
                mTransparentOverlay.addOverlay(new OverlayItem(p, "地图上的点", ""));
                mapView.invalidate();
            }
        });
        mTransparentOverlay.clearOverlay();
        mMapView.getOverlays().add(mTransparentOverlay);
        mMapView.invalidate();
        mMapView.requestFocus();
    }

    private void popupView(final OverlayItem overlayItem) {
        if (mPopView != null) {
            mMapView.removeViewInLayout(mPopView);
        }
        mPopView = this.getLayoutInflater().inflate(R.layout.popview, null);
        mPopView.setClickable(true);
        mMapView.addView(mPopView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, null, MapView.LayoutParams.BOTTOM_CENTER));
        Button button = (Button)mPopView.findViewById(R.id.overlay_pop);
        button.setText(overlayItem.getTitle());
        button.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                removePopupView();
                new AlertDialog.Builder(SelectAddressActivity.this)
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setTitle(overlayItem.getTitle())
                .setMessage(overlayItem.getSnippet())
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mDestinationText.setText(overlayItem.getTitle() + " " + overlayItem.getSnippet());
                        mDestinationItem = overlayItem;
                        Message msg = mWorker.obtainMessage(SHOW_DESTINATION_OVERLAY);
                        mWorker.dispatchMessage(msg);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Now just do nothing
                    }
                }).create()
                .show();
            }
        });
        mMapView.updateViewLayout(mPopView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, overlayItem.getPoint(),
                MapView.LayoutParams.BOTTOM_CENTER));
        mMapView.invalidate();
    }

    private void removeOverlays () {
        mMapView.getOverlays().clear();
        mMapView.requestFocus();
        mDestinationText.setText("");
    }

    private void removePopupView() {
        if (mPopView != null) {
            mMapView.removeViewInLayout(mPopView);
            mMapView.requestFocus();
            mPopView = null;
        }
    }

    private void showDestinationOverlay() {
        mMapView.getOverlays().clear();
        mMapView.invalidate();
        Drawable marker = getResources().getDrawable(R.drawable.icon_nav_end);
        marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
        DestinationOverlay destinationOverlay = new DestinationOverlay(marker);
        destinationOverlay.setOverlayItem(new OverlayItem(mDestinationItem.getPoint(), "", ""));
        mMapView.getOverlays().add(destinationOverlay);
        mMapView.invalidate();
        mMapView.requestFocus();
        mMapController.animateTo(mDestinationItem.getPoint());
    }

    @Override
    protected void onPause() {
        if (mbMapManager != null) {
            mbMapManager.stop();
        }
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mbMapManager != null) {
            mbMapManager.start();
        }
    }

    /**
     * Hide the text input method. This is necessary in order to maintain good
     * UX for IMs, where objects "behind" the IM can be interacted with, e.g.
     * the top panel, send button etc.
     *
     * @param checkFullScreen if it set to true,when the input method status is
     *            not in full screen mode,should hide InputMethod. If it set to
     *            false,the InputMethod should be hide whether the input method
     *            status is in full screen mode or not(eg: tap the menu:edit
     *            recipient)
     */
    public void hideInputMethod(boolean checkFullScreen) {
        // TODO check getActivity is null
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (!checkFullScreen || !imm.isFullscreenMode()) {
            View view = getCurrentFocus();
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private LocationListener mLocationListener = new LocationListener() {

        public void onLocationChanged(Location location) {
            if (location != null) {
//                double lat = location.getLatitude();
//                double lon = location.getLongitude();
//                if (lat != 0 && lon != 0) {
//                    GeoPoint point = new GeoPoint((int)(lat * 1E6), (int)(lon * 1E6 ));
//                    mMapController.animateTo(point);
//                    mMapView.invalidate();// Invalidate map view
//                } else {
//                    Toast.makeText(MainActivity.this, "Haven't got you address", Toast.LENGTH_LONG).show();
//                }
            }
        }
    };
}