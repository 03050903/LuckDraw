package cn.fanrunqi.luckdrawlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LuckDraw extends View {


    private  Paint mBackPaint = new Paint();
    private String mText = "";
    private Rect mTextBound = new Rect();
    private String TextColor = "#dddddd";
    private int TextSize = 38;

    private Paint mPaint = new Paint();
    private int strokeWidth = 50;
    private Path mPath = new Path();
    private Canvas mCanvas;
    private Bitmap mBitmap;

    private int pictureId;
    private boolean isInit;

    int width;
    int height;

    private boolean isComplete;

    private int mLastX;
    private int mLastY;

    public LuckDraw(Context context)
    {
        this(context, null);
    }
    public LuckDraw(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }
    public LuckDraw(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        if(isInit){
            initDraw();
        }
    }


    public interface CompleteListener{
       void complete();
    }
    private CompleteListener listener;
    public void setOnCompleteListener(CompleteListener listener){
        this.listener = listener;
    }


    public void Init(String text,int drawableResId){
        mText = text;
        pictureId = drawableResId;
        isInit =true;
    }
    public void setText(String TextColor,int TextSize){
        this.TextColor = TextColor;
        this.TextSize = TextSize;
    }
    public void setStrokeWidth(int strokeWidth){
        this.strokeWidth = strokeWidth;
    }

    private void initDraw() {

        mPath = new Path();
        mPaint.setColor(Color.parseColor("#c0c0c0"));
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mBitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPaint.setStyle(Paint.Style.FILL);
        mCanvas.drawRoundRect(new RectF(0, 0, width, height), 30, 30,mPaint);

        mCanvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
                pictureId), null, new RectF(0, 0, width, height), null);
    }
    @Override
    protected void onDraw(Canvas canvas)
    {
        mBackPaint.setStyle(Paint.Style.FILL);
        mBackPaint.setTextScaleX(2f);
        mBackPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
        mBackPaint.setColor(Color.parseColor(TextColor));
        mBackPaint.setTextSize(TextSize);

        mPaint.setStrokeWidth(strokeWidth);

            canvas.drawText(mText, getWidth() / 2 - mTextBound.width() / 2,
                    getHeight() / 2 + mTextBound.height() / 2, mBackPaint);
            if (!isComplete)
            {

                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                mCanvas.drawPath(mPath, mPaint);
                canvas.drawBitmap(mBitmap, 0, 0, null);
            }
        }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mPath.moveTo(mLastX, mLastY);
                break;
            case MotionEvent.ACTION_MOVE:

                int dx = Math.abs(x - mLastX);
                int dy = Math.abs(y - mLastY);

                if (dx > 3 || dy > 3)
                    mPath.lineTo(x, y);

                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                new Thread(mRunnable).start();
                break;
        }

        invalidate();
        return true;
    }


    private Runnable mRunnable = new Runnable()
    {
        private int[] mPixels;

        @Override
        public void run()
        {
            Looper.prepare();

            int w = getWidth();
            int h = getHeight();

            float wipeArea = 0;
            float totalArea = w * h;

            Bitmap bitmap = mBitmap;

            mPixels = new int[w * h];


            bitmap.getPixels(mPixels, 0, w, 0, 0, w, h);


            for (int i = 0; i < w; i++)
            {
                for (int j = 0; j < h; j++)
                {
                    int index = i + j * w;
                    if (mPixels[index] == 0)
                    {
                        wipeArea++;
                    }
                }
            }


            if (wipeArea > 0 && totalArea > 0)
            {
                int percent = (int) (wipeArea * 100 / totalArea);

                if (percent > 50)
                {
                    if(!isComplete){

                        listener.complete();
                        isComplete = true;
                        postInvalidate();
                    }
                }
            }

            Looper.loop();
        }

    };
}
