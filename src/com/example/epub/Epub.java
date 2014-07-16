package com.example.epub;

import java.io.IOException;
import java.io.InputStream;

import android.os.Bundle;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;

public class Epub extends GraphicsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new PatternView(this));
    }

    private static Bitmap makeBitmap1() {
        Bitmap bm = Bitmap.createBitmap(40, 40, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bm);
        c.drawColor(Color.RED);
        Paint p = new Paint();
        p.setColor(Color.BLUE);
        c.drawRect(5, 5, 35, 35, p);
        return bm;
    }

    private static Bitmap makeBitmap2() {
        Bitmap bm = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.GREEN);
        p.setAlpha(0xCC);
        c.drawCircle(32, 32, 27, p);
        return bm;
    }

    public static class PatternView extends View {
        private final Shader mShader1 = null;
        private final Shader mShader2;
        private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final DrawFilter mFastDF;

        private float mTouchStartX;
        private float mTouchStartY;
        private float mTouchCurrX;
        private float mTouchCurrY;
        private DrawFilter mDF;

        public PatternView(Context context) {
            super(context);
            setFocusable(true);
            setFocusableInTouchMode(true);

            mFastDF = new PaintFlagsDrawFilter(Paint.FILTER_BITMAP_FLAG |
                                               Paint.DITHER_FLAG,
                                               0);

            Bitmap bm = Bitmap.createBitmap(40, 40, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bm);
            Typeface mFace = null;
            mFace = Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/Ubuntu-B.ttf");

            mPaint.setTextSize(64);

            
            canvas.drawColor(Color.WHITE);
            
            mPaint.setTypeface(null);
            canvas.drawText("Default", 10, 100, mPaint);
            mPaint.setTypeface(mFace);
            for (int i = 200; i <= 1000; i+=100) {
            	canvas.drawText("Custom", 10, i, mPaint);
            }
            
            mShader2 = new BitmapShader(makeBitmap2(), Shader.TileMode.REPEAT,
                                        Shader.TileMode.REPEAT);

            Matrix m = new Matrix();
            m.setRotate(30);
            mShader2.setLocalMatrix(m);

            //mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
        }

        @Override protected void onDraw(Canvas canvas) {
            canvas.setDrawFilter(mDF);

            //mPaint.setShader(mShader1);
            canvas.drawPaint(mPaint);

            canvas.translate(mTouchCurrX - mTouchStartX,
                             mTouchCurrY - mTouchStartY);

            mPaint.setShader(mShader2);
            canvas.drawPaint(mPaint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTouchStartX = mTouchCurrX = x;
                    mTouchStartY = mTouchCurrY = y;
                    mDF = mFastDF;
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mTouchCurrX = x;
                    mTouchCurrY = y;
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    mDF = null;
                    invalidate();
                    break;
            }
            return true;
        }
    }

    
    private static class SampleView extends View {
        private Paint    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private Typeface mFace;

        public SampleView(Context context) {
            super(context);

            mFace = Typeface.createFromAsset(getContext().getAssets(),
                                             "fonts/Ubuntu-B.ttf");

            mPaint.setTextSize(64);
        }

        @Override 
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);
            Bitmap bitmap = null;
			try {
				bitmap = getBitmapFromAsset("images/blankpage_normal.bmp", this.getContext());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            canvas.drawBitmap(bitmap, 0, 0, null);
            mPaint.setTypeface(null);
            canvas.drawText("Default", 10, 100, mPaint);
            mPaint.setTypeface(mFace);
            
            canvas.drawText("Custom", 10, 200, mPaint);
        }
    }
    
    private static Bitmap getBitmapFromAsset(String strName, Context myContext) throws IOException
    {
        AssetManager assetManager = myContext.getAssets();
        
        InputStream istr = null;
        
        try {
        	istr = assetManager.open(strName);
        }
        catch (IOException e)
        {
        	//throw e.printStackTrace();
        }
        
        Bitmap bitmap = BitmapFactory.decodeStream(istr);

        return bitmap;
    }
}


