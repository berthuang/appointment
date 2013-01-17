package com.android.appointment.overlay;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.OverlayItem;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;

/**
 * TransparentOverlay for address selection
 *
 */
public class TransparentOverlay extends ItemizedOverlay<OverlayItem> {
    private OnOverlayTapListener mOverlayTapListener;
    private ArrayList<OverlayItem> mOverlayItemList = new ArrayList<OverlayItem>();

    public TransparentOverlay(Drawable drawable) {
        super(boundCenterBottom(drawable));
    }
    @Override
    public boolean onTap(GeoPoint point, MapView mapView) {
        if(mOverlayTapListener != null){
            mOverlayTapListener.onTap(point, mapView);
        }
        return super.onTap(point, mapView);
    }

    @Override
    protected boolean onTap(int index) {
        OverlayItem item = mOverlayItemList.get(index);
        if(mOverlayTapListener != null){
            mOverlayTapListener.onTap(item);
        }
        return true;
    }

    @Override
    protected OverlayItem createItem(int i) {
        // TODO Auto-generated method stub
        return mOverlayItemList.get(i);
    }

    public void addOverlay(OverlayItem overlayItem) {
        mOverlayItemList.add(overlayItem);
        populate();
    }

    public void clearOverlay() {
        mOverlayItemList.clear();
        populate();
    }
    @Override
    public int size() {
        // TODO Auto-generated method stub
        return mOverlayItemList.size();
    }

    public OnOverlayTapListener getOnOverlayTapListener() {
        return mOverlayTapListener;
    }

    public void setonOverlayTapListener(OnOverlayTapListener listener) {
        mOverlayTapListener = listener;
    }

    public interface OnOverlayTapListener{
        public void onTap(OverlayItem overlayItem);

        public void onTap(GeoPoint p, MapView mapView);
    };
}
