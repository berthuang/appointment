
package com.android.appointment.overlay;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.OverlayItem;
import com.baidu.mapapi.Projection;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class OverItemT extends com.baidu.mapapi.ItemizedOverlay<OverlayItem> {
    private Drawable marker;

    private Context mContext;

    private GeoPoint p;

    private OverlayItem o;

    public OverItemT(Drawable marker, Context context, String title, GeoPoint p) {
        super(boundCenterBottom(marker));
        this.marker = marker;
        this.mContext = context;
        this.p = p;
        o = new OverlayItem(p, title, title);
        populate();
    }

    public void updateOverlay() {
        populate();
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {

        Projection projection = mapView.getProjection();
        for (int index = size() - 1; index >= 0; index--) {
            OverlayItem overLayItem = getItem(index);
            String title = overLayItem.getTitle();
            Point point = projection.toPixels(overLayItem.getPoint(), null);
            Paint paintText = new Paint();
            paintText.setColor(Color.BLUE);
            paintText.setTextSize(15);
            RectF rectF = new RectF();
            rectF.set(point.x - 30, point.y, point.x + 10, point.y + 20);
            canvas.drawRoundRect(rectF, 5, 5, paintText);
//            drawText(title, point.x - 30, point.y, paintText);
        }
        super.draw(canvas, mapView, shadow);

        boundCenterBottom(marker);
    }

    @Override
    protected OverlayItem createItem(int i) {
        // TODO Auto-generated method stub
        return o;
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    protected boolean onTap(int i) {
        return true;
    }

    @Override
    public boolean onTap(GeoPoint arg0, MapView arg1) {
        // ItemizedOverlayDemo.mPopView.setVisibility(View.GONE);
        return super.onTap(arg0, arg1);
    }
}
