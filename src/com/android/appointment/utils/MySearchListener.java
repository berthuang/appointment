package com.android.appointment.utils;

import com.android.appointment.overlay.TransparentOverlay;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKPoiInfo;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.OverlayItem;
import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;

public class MySearchListener implements MKSearchListener{
    private Context mContext;
    private MapView mMapView;
    private TransparentOverlay mTransparentOverlay;

    public MySearchListener(Context context, MapView mapView, TransparentOverlay transparentOverlay) {
        this.mContext = context;
        this.mMapView = mapView;
        this.mTransparentOverlay = transparentOverlay;
    }


    public void onGetAddrResult(MKAddrInfo arg0, int arg1) {

    }


    public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
        // TODO Auto-generated method stub

    }


    public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
        // TODO Auto-generated method stub

    }


    public void onGetPoiDetailSearchResult(int arg0, int arg1) {

    }

    /**
     * POI搜索结果（范围检索、城市POI检索、周边检索）
     * @param result 搜索结果
     * @param type 返回结果类型（11,12,21:poi列表 7:城市列表）
     * @param iError 错误号（0表示正确返回）
     */

    public void onGetPoiResult(MKPoiResult result, int type, int error) {
        if (result == null) {
            return;
        }
        mTransparentOverlay.clearOverlay();
        // 设置搜索到的POI数据
        ArrayList<MKPoiInfo> poiInfoList = result.getAllPoi();
        StringBuilder poiDetailInfo = new StringBuilder();
        for (MKPoiInfo poiInfo : poiInfoList) {
            if (poiDetailInfo.toString().length() > 0) {
                poiDetailInfo.delete(0, poiDetailInfo.toString().length());
            }
            // Add city information
            if (!TextUtils.isEmpty(poiInfo.city)) {
                poiDetailInfo.append(poiInfo.city);
                poiDetailInfo.append(", ");
            }
            // Add address information
            if (!TextUtils.isEmpty(poiInfo.address)) {
                poiDetailInfo.append(poiInfo.address);
                poiDetailInfo.append("\n");
            }
            if (!TextUtils.isEmpty(poiInfo.phoneNum)) {
                poiDetailInfo.append(poiInfo.phoneNum);
            }
            mTransparentOverlay.addOverlay(new OverlayItem(poiInfo.pt, poiInfo.name, poiDetailInfo
                    .toString()));
        }
        if (poiInfoList.size() > 1) {
            mMapView.getController().animateTo(poiInfoList.get(0).pt);
        }
        mMapView.invalidate();
    }


    public void onGetRGCShareUrlResult(String arg0, int arg1) {
        // TODO Auto-generated method stub

    }


    public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
        // TODO Auto-generated method stub

    }


    public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
        // TODO Auto-generated method stub

    }


    public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
        // TODO Auto-generated method stub

    }

}
