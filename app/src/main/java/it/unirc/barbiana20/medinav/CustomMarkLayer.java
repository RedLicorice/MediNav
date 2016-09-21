package it.unirc.barbiana20.medinav;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.onlylemi.mapview.library.MapView;
import com.onlylemi.mapview.library.layer.MapBaseLayer;
import com.onlylemi.mapview.library.layer.MarkLayer;
import com.onlylemi.mapview.library.utils.MapMath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giuse on 21/09/2016.
 */

public class CustomMarkLayer extends MapBaseLayer {

    private List<Mark> marks;

    private MarkLayer.MarkIsClickListener listener;

    private List<Bitmap> markIcons;
    //bmpMark, bmpMarkTouch, bmpEndpoint;

    private float radiusMark;
    private boolean isClickMark = false;
    private int num = -1;

    private Paint paint;

    public CustomMarkLayer(MapView mapView) {
        this(mapView, null);
    }

    public CustomMarkLayer(MapView mapView, List<Mark> marks) {
        super(mapView);
        this.marks = marks;
        initLayer();
    }
    private Bitmap scaleImage(Bitmap image, float factor)
    {
        return Bitmap.createScaledBitmap(image, (int)(image.getWidth() * factor),(int)(image.getHeight() * factor), true);
    }
    private void initLayer() {
        radiusMark = setValue(14f);
        //Load mark icons
        markIcons = new ArrayList<Bitmap>();
        float factor = 0.65F;
        markIcons.add(scaleImage(BitmapFactory.decodeResource(mapView.getResources(), R.drawable.endpoint),factor));
        markIcons.add(scaleImage(BitmapFactory.decodeResource(mapView.getResources(), R.drawable.waypoint),factor));
        markIcons.add(scaleImage(BitmapFactory.decodeResource(mapView.getResources(), R.drawable.entrance),factor));
        markIcons.add(scaleImage(BitmapFactory.decodeResource(mapView.getResources(), R.drawable.stair),factor));
        markIcons.add(scaleImage(BitmapFactory.decodeResource(mapView.getResources(), R.drawable.elevator),factor));
        markIcons.add(scaleImage(BitmapFactory.decodeResource(mapView.getResources(), R.drawable.toilet),factor));
        markIcons.add(scaleImage(BitmapFactory.decodeResource(mapView.getResources(), R.drawable.first_aid),factor));
        markIcons.add(scaleImage(BitmapFactory.decodeResource(mapView.getResources(), R.drawable.fire_extinguisher),factor));
        markIcons.add(scaleImage(BitmapFactory.decodeResource(mapView.getResources(), R.drawable.ramp),factor));
        markIcons.add(scaleImage(BitmapFactory.decodeResource(mapView.getResources(), R.drawable.emergency_exit),factor));
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    public void onTouch(MotionEvent event) {
        if (marks != null) {
            if (!marks.isEmpty()) {
                float[] goal = mapView.convertMapXYToScreenXY(event.getX(), event.getY());
                for (int i = 0; i < marks.size(); i++) {
                    Mark mark = marks.get(i);
                    Bitmap markImage = markIcons.get(mark.type.ordinal());
                    if (MapMath.getDistanceBetweenTwoPoints(goal[0], goal[1],
                            mark.pos.x - markImage.getWidth() / 2, mark.pos.y - markImage.getHeight() / 2) <= 50) {
                        num = i;
                        isClickMark = true;
                        break;
                    }

                    if (i == marks.size() - 1) {
                        isClickMark = false;
                    }
                }
            }

            if (listener != null && isClickMark) {
                listener.markIsClick(num);
                mapView.refresh();
            }
        }
    }

    @Override
    public void draw(Canvas canvas, Matrix currentMatrix, float currentZoom, float
            currentRotateDegrees) {
        if (isVisible && marks != null) {
            canvas.save();
            if (!marks.isEmpty()) {
                for (int i = 0; i < marks.size(); i++) {
                    Mark mark = marks.get(i);
                    float[] goal = {mark.pos.x, mark.pos.y};
                    currentMatrix.mapPoints(goal);

                    paint.setColor(Color.BLACK);
                    paint.setTextSize(radiusMark);

                    //mark ico
                    Bitmap bmpDraw = markIcons.get(mark.type.ordinal());
                    canvas.drawBitmap(bmpDraw, goal[0] - bmpDraw.getWidth() / 2, goal[1] - bmpDraw.getHeight() / 2, paint);
                    if (mapView.getCurrentZoom() > 1.0 ) {
                        canvas.drawText(mark.name, goal[0] - radiusMark, goal[1] - radiusMark / 2, paint);
                    }
                    //Show a mark touched icon, i don't care bout this. leave aesthetics to the architects, lol
                    /*if (i == num && isClickMark) {
                        canvas.drawBitmap(bmpMarkTouch, goal[0] - bmpMarkTouch.getWidth() / 2,
                                goal[1] - bmpMarkTouch.getHeight(), paint);
                    }*/
                }
            }
            canvas.restore();
        }
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public void setMarks(List<Mark> marks) {
        this.marks = marks;
    }

    public boolean isClickMark() {
        return isClickMark;
    }

    public void setMarkIsClickListener(MarkLayer.MarkIsClickListener listener) {
        this.listener = listener;
    }

    public interface MarkIsClickListener {
        void markIsClick(int num);
    }
}