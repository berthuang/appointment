
package com.android.appointment;

import com.android.appointment.constant.Constant;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;

import android.app.Application;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

public class AppointmentApp extends Application {

    public BMapManager mapManager = null;

    static AppointmentApp app;


    @Override
    public void onCreate() {
        mapManager = new BMapManager(this);
        mapManager.init(Constant.BAIDU_MAP_KEY, new MyGeneralListener());
        app = this;
        // Initial JPush service
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    public static class MyGeneralListener implements MKGeneralListener {


        public void onGetNetworkState(int arg0) {
            Toast.makeText(AppointmentApp.app.getApplicationContext(), "Net work error",
                    Toast.LENGTH_LONG).show();
        }


        public void onGetPermissionState(int iError) {
            if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
                // Key error
                Toast.makeText(AppointmentApp.app.getApplicationContext(), "Incorrect map key",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onTerminate() {
        if (mapManager != null) {
            mapManager.destroy();
            mapManager = null;
        }
        super.onTerminate();
    }

    public BMapManager getBMapManager() {
        return mapManager;
    }
}
