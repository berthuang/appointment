package com.android.appointment.overlay;

import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.OverlayItem;

import android.graphics.drawable.Drawable;

public class DestinationOverlay extends ItemizedOverlay<OverlayItem> {
    OverlayItem mOverlayItem;
    public DestinationOverlay(Drawable marker) {
        super(boundCenterBottom(marker));
    }

    @Override
    protected OverlayItem createItem(int index) {
        return mOverlayItem;
    }

    @Override
    public int size() {
        return 1;
    }

    public void setOverlayItem(OverlayItem overlayItem) {
        mOverlayItem = overlayItem;
        populate();
    }
}
