package com.android.appointment.overlay;

import com.android.appointment.R;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MKRoute;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.RouteOverlay;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import java.util.ArrayList;

public class CustomRouteOverLay extends RouteOverlay {

    public Activity mActivity;
    private MapView mMapView;

    static ArrayList<View> overlayviews = new ArrayList<View>();

    public CustomRouteOverLay(Activity activity, MapView mapView) {
        super(activity, mapView);
        mActivity = activity;
        mMapView = mapView;
    }

    @Override
    protected boolean onTap(int arg0) {
        return true;
    }

    @Override
    public void setData(MKRoute mkRoute) {
        super.setData(mkRoute);
        addHint(mkRoute);
    }

    private void addHint(MKRoute routes) {
        mMapView.getOverlays().clear();
        // mapView.removeAllViewsInLayout();
        View mPopView = mActivity.getLayoutInflater().inflate(R.layout.popview, null);
        for (int i = 0; i < overlayviews.size(); i++) {
            System.out.println("remove &" + i);
            mMapView.removeViewInLayout(overlayviews.get(i));
            overlayviews.remove(i);
        }

        mMapView.invalidate();
        for (int i = 0; i < routes.getNumSteps(); i++) {

            Drawable marker = mActivity.getResources().getDrawable(R.drawable.pop);
            marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
            GeoPoint pt = routes.getStep(i).getPoint();

            if (i != 0 && i != routes.getNumSteps() - 1) {
                mPopView = mActivity.getLayoutInflater().inflate(R.layout.popview, null);
                mMapView.addView(mPopView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT, null, MapView.LayoutParams.TOP_LEFT));
                mPopView.setVisibility(View.GONE);
                mMapView.updateViewLayout(mPopView, new MapView.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, pt,
                        MapView.LayoutParams.BOTTOM_CENTER));
                mPopView.setVisibility(View.VISIBLE);
                Button button = (Button)mPopView.findViewById(R.id.overlay_pop);
                button.setText(routes.getStep(i).getContent());
                overlayviews.add(mPopView);
                overlayviews.add(button);
            } else {
                mPopView = mActivity.getLayoutInflater().inflate(R.layout.popview, null);
                mMapView.addView(mPopView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT, null, MapView.LayoutParams.TOP_LEFT));
                mPopView.setVisibility(View.GONE);
                mMapView.updateViewLayout(mPopView, new MapView.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, pt,
                        MapView.LayoutParams.BOTTOM_CENTER));
                mPopView.setVisibility(View.VISIBLE);
                Button button = (Button)mPopView.findViewById(R.id.overlay_pop);
                button.offsetTopAndBottom(100);
                button.setTextColor(Color.BLUE);
                button.setBackgroundColor(Color.TRANSPARENT);
                button.setText(routes.getStep(i).getContent());
                overlayviews.add(mPopView);
                overlayviews.add(button);
            }
        }

    }

    public void addHints(MKRoute routes) {
        for (int i = 0; i < routes.getNumSteps(); i++) {
            Drawable marker = mActivity.getResources().getDrawable(R.drawable.pop);
            marker.setBounds(0, 0, marker.getIntrinsicWidth(),
                    marker.getIntrinsicHeight());
            OverItemT overitem = new OverItemT(marker,mActivity, routes.getStep(i).getContent(),routes.getStep(i).getPoint());
//          OverlayItem over=new OverlayItem(routes.GET, null, null);
            mMapView.getOverlays().add(overitem);
        }
        mMapView.invalidate();
    }
}
