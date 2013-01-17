package com.android.appointment.utils;

import com.baidu.mapapi.GeoPoint;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationUtils {
    private static final int MAX_ADDR_RESULT_NUM = 2;

    /**
     * Get address by geopoint
     **/
    public static String getAddressFromGeoPoint(GeoPoint p, Context context) {
        StringBuilder s = new StringBuilder();
        Geocoder gc = new Geocoder(context.getApplicationContext(), Locale.getDefault());
        try {
            // 根据坐标地址搜索地址
            List<Address> l = gc.getFromLocation(p.getLatitudeE6() / 1E6, p.getLongitudeE6() / 1E6, MAX_ADDR_RESULT_NUM);
            if (l != null && l.size() > 0) {
                for (Address a : l) {
                    for (int i = 0; i < a.getMaxAddressLineIndex(); i++) {
                        s.append(a.getAddressLine(i));
                    }
                    s.append("\n");
                }
            }
        } catch (IOException e) {
            Toast.makeText(context.getApplicationContext(), "Get address error", Toast.LENGTH_SHORT).show();
        }
        return s.toString();
    }

    /**
     * Get geopoint by address
     **/
    public List<GeoPoint> getGeoPointFromAddress(String address, Context context) {
        List<GeoPoint> gpList = new ArrayList<GeoPoint>();
        Geocoder gc = new Geocoder(context.getApplicationContext(), Locale.getDefault());
        try {
            // 根据具体位置搜索地址
            List<Address> l = gc.getFromLocationName(address, MAX_ADDR_RESULT_NUM);
            if (l != null && l.size() > 0) {
                for (Address a : l) {
                    for (int i = 0; i < a.getMaxAddressLineIndex(); i++) {
                        gpList.add(new GeoPoint((int)(a.getLatitude() * 1E6), (int)(a.getLongitude() * 1E6)));
                    }
                }
            }
        } catch (IOException e) {
            Toast.makeText(context.getApplicationContext(), "Get geo error", Toast.LENGTH_SHORT).show();
        }
        return gpList;
    }


    /**
     * 抓取当前的城市信息
     *
     * @return String 城市名
     */
    /*
    public String getCurrentCityName(){
        String city = "";
        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        GsmCellLocation glc = (GsmCellLocation) telManager.getCellLocation();

        if (glc != null){
            int cid = glc.getCid(); // value 基站ID号
            int lac = glc.getLac();// 写入区域代码
            String strOperator = telManager.getNetworkOperator();
            int mcc = Integer.valueOf(strOperator.substring(0, 3));// 写入当前城市代码
            int mnc = Integer.valueOf(strOperator.substring(3, 5));// 写入网络代码
            String getNumber = "";
            getNumber += ("cid:" + cid + "\n");
            getNumber += ("cid:" + lac + "\n");
            getNumber += ("cid:" + mcc + "\n");
            getNumber += ("cid:" + mnc + "\n");
            DefaultHttpClient client = new DefaultHttpClient();
            BasicHttpParams params = new BasicHttpParams();
            HttpConnectionParams.setSoTimeout(params, 20000);
            HttpPost post = new HttpPost("http://www.google.com/loc/json");

            try{
                JSONObject jObject = new JSONObject();
                jObject.put("version", "1.1.0");
                jObject.put("host", "maps.google.com");
                jObject.put("request_address", true);
                if (mcc == 460)
                    jObject.put("address_language", "zh_CN");
                else
                    jObject.put("address_language", "en_US");

                JSONArray jArray = new JSONArray();
                JSONObject jData = new JSONObject();
                jData.put("cell_id", cid);
                jData.put("location_area_code", lac);
                jData.put("mobile_country_code", mcc);
                jData.put("mobile_network_code", mnc);
                jArray.put(jData);
                jObject.put("cell_towers", jArray);
                StringEntity se = new StringEntity(jObject.toString());
                post.setEntity(se);

                HttpResponse resp = client.execute(post);
                BufferedReader br = null;
                if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    br = new BufferedReader(new InputStreamReader(resp
                            .getEntity().getContent()));
                    StringBuffer sb = new StringBuffer();

                    String result = br.readLine();
                    while (result != null){
                        sb.append(getNumber);
                        sb.append(result);
                        result = br.readLine();
                    }
                    String s = sb.toString();
                    s = s.substring(s.indexOf("{"));
                    JSONObject jo = new JSONObject(s);
                    JSONObject arr = jo.getJSONObject("location");
                    JSONObject address = arr.getJSONObject("address");
                    city = address.getString("city");
                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
            catch (ClientProtocolException e){
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            finally{
                post.abort();
                client = null;
            }
        }
        return city;
    }
    */
}
