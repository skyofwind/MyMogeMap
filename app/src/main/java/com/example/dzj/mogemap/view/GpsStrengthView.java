package com.example.dzj.mogemap.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.dzj.mogemap.R;

/**
 * Created by dzj on 2018/2/8.
 */

public class GpsStrengthView extends View {
    private final static String TAG = "GpsStrengthView";
    private Context context;
    //view的宽度
    private int mWidth;
    //view的高度
    private int mHeight;
    //圆柱宽度
    private float columnWidth;
    private float columHeight;
    //圆柱间隔
    private float columnInterval;
    private float heights;
    private int strength = 0;

    public GpsStrengthView(Context context) {
        super(context);
        this.context = context;
    }

    public GpsStrengthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public GpsStrengthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        //setMeasuredDimension(widthSpecMode, heightSpecMode);
        log("conml "+widthSpecSize+" onMeasure "+heightSpecSize);
        int WIDTH_DEFAULT = 45;
        int HEIGHT_DEFAULT = 54;
        if (widthMeasureSpec == MeasureSpec.AT_MOST && heightSpecSize == MeasureSpec.AT_MOST) {
            setMeasuredDimension(WIDTH_DEFAULT, HEIGHT_DEFAULT);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(WIDTH_DEFAULT, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, HEIGHT_DEFAULT);
        }
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        log("conml"+"onSizeChanged");
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        columnWidth = 8;
        columHeight = 10;
        columnInterval = 3;
        heights = columHeight*3+columnWidth;
        log("mWidth="+mWidth+" herht="+mHeight);
        //widthView =
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        log("conml"+"onDraw");
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        drawAll(canvas, paddingLeft, paddingRight, paddingTop, paddingBottom, strength);

    }
    private void drawAll(Canvas canvas, int pl, int pr, int pt, int pb, int strength){
        for(int i=0;i<3;i++){
            if(i < strength){
                drawModule(canvas, (int)(pl+columnInterval*i), pr, pt, pb, i);
            }else {
                drawNullColumn(canvas, (int)(pl+columnInterval*i), pr, pt, pb, i);
            }
        }
    }

    private void drawModule(Canvas canvas, int pl, int pr, int pt, int pb, int position){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        //paint.setStrokeWidth(1f);
        paint.setColor(getColor(strength));
        float offsetLeft = pl+(columnWidth+columnInterval)*position;
        float height = columHeight*(position+1);
        RectF rectF = new RectF((float) (offsetLeft), (float)(pt+heights-height-columnWidth/2), (float)(offsetLeft+columnWidth), (float) (pt+heights-height+columnWidth/2));
        canvas.drawArc(rectF, 180, 180, false, paint);
        rectF = new RectF((float) (offsetLeft), (float)(pt+heights-height), (float)(offsetLeft+columnWidth), (float) (pt+heights));
        canvas.drawRect(rectF, paint);
        rectF = new RectF((float) (offsetLeft), (float) (pt+heights-columnWidth/2), (float)(offsetLeft+columnWidth), (float) (pt+columnWidth/2+heights+pb));
        canvas.drawArc(rectF, 180, -180, false, paint);

        //log("p="+position+" offset="+offsetLeft+" height="+height);
        //log("one="+(pt+heights-height-columnWidth/2)+" "+(pt+heights-height+columnWidth/2));
        //log("two="+(pt+heights-height)+" "+(pt+heights-height));
        //log("three="+(pt+heights-height-columnWidth/2)+" "+(pt+heights-height+columnWidth/2));
    }
    private void drawNullColumn(Canvas canvas, int pl, int pr, int pt, int pb, int position){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        //paint.setStrokeWidth(1f);
        paint.setColor(getMyColor(R.color.text_gray));
        float offsetLeft = pl+(columnWidth+columnInterval)*position;
        float height = columHeight*(position+1);
        RectF rectF = new RectF((float) (offsetLeft), (float)(pt+heights-height-columnWidth/2), (float)(offsetLeft+columnWidth), (float) (pt+heights-height+columnWidth/2));
        canvas.drawArc(rectF, 180, 180, false, paint);
        rectF = new RectF((float) (offsetLeft), (float)(pt+heights-height), (float)(offsetLeft+columnWidth), (float) (pt+heights));
        canvas.drawRect(rectF, paint);
        rectF = new RectF((float) (offsetLeft), (float) (pt+heights-columnWidth/2), (float)(offsetLeft+columnWidth), (float) (pt+columnWidth/2+heights+pb));
        canvas.drawArc(rectF, 180, -180, false, paint);
    }
    private float getOffset(float width){
        float offset = (width < columnInterval)?(columnInterval-width)/2:0;
        return offset;
    }
    private void log(String str){
        Log.i(TAG,str);
    }
    private int getColor(int strength){
        int id = -1;
        switch (strength){
            case 1:
                id = getMyColor(R.color.red);
                break;
            case 2:
                id = getMyColor(R.color.yellow_shit);
                break;
            case 3:
                id = getMyColor(R.color.green);
                break;
        }
        return id;
    }
    public void setStrength(int strength){
        this.strength = strength;
        invalidate();
    }
    private int getMyColor(int id){
        if(Build.VERSION.SDK_INT >= 23){
            return context.getColor(id);
        }else {
            return context.getResources().getColor(id);
        }
    }
}
