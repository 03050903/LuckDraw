package cn.fanrunqi.luckdraw;

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
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LuckDraw extends View {

    /**
     * -----------------------------------------------背景图层-----------------------
     *背景画笔
     */
    private  Paint mBackPaint = new Paint();
    /**
     * 背景显示的文字
     */
    private String mText = "$7000000";
    /**
     * 文字的四个顶点坐标
     */
    private Rect mTextBound = new Rect();
    /**
     * -----------------------------------------------前景图层-----------------------
     * 绘制手指路径的Paint
     */
    private Paint mPaint = new Paint();
    /**
     * 记录用户绘制的路径
     */
    private Path mPath = new Path();
    /**
     * 内存中创建的Canvas
     */
    private Canvas mCanvas;
    /**
     * mCanvas绘制内容在其上
     */
    private Bitmap mBitmap;


    /**
     * view的宽高
     */
    int width;
    int height;
    /**
     * 完成标志位
     */
    private boolean isComplete;

    /**
     * 记录上次触摸点
     */
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
        Init();
    }

    private void Init() {
        /**
         * -------------------------------背景相关---------------------------------
         */
        mBackPaint.setStyle(Paint.Style.FILL);
        mBackPaint.setTextScaleX(2f);
        mBackPaint.setColor(Color.DKGRAY);
        mBackPaint.setTextSize(38);
        mBackPaint.getTextBounds(mText, 0, mText.length(), mTextBound);

        /**
         * -------------------------------前景相关---------------------------------
         */
        mPath = new Path();
        mPaint.setColor(Color.parseColor("#c0c0c0"));
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        /**
         * 设置画笔宽度
         */
        mPaint.setStrokeWidth(30);

        mBitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPaint.setStyle(Paint.Style.FILL);
        mCanvas.drawRoundRect(new RectF(0, 0, width, height), 30, 30,mPaint);
        /**
         * 绘制遮盖的图片
         */
        mCanvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.luckdraw_fg), null, new RectF(0, 0, width, height), null);
    }
    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawText(mText, getWidth() / 2 - mTextBound.width() / 2,
                getHeight() / 2 + mTextBound.height() / 2, mBackPaint);
        if (!isComplete)
        {
            /**
             * 绘制线条
             */
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

    /**
     * 统计擦除区域任务
     */
    private Runnable mRunnable = new Runnable()
    {
        private int[] mPixels;

        @Override
        public void run()
        {

            int w = getWidth();
            int h = getHeight();

            float wipeArea = 0;
            float totalArea = w * h;

            Bitmap bitmap = mBitmap;

            mPixels = new int[w * h];

            /**
             * 拿到所有的像素信息
             */
            bitmap.getPixels(mPixels, 0, w, 0, 0, w, h);

            /**
             * 遍历统计擦除的像素个数
             */
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

            /**
             * 根据擦除百分比，判断是否直接显示背景图层
             */
            if (wipeArea > 0 && totalArea > 0)
            {
                int percent = (int) (wipeArea * 100 / totalArea);

                if (percent > 50)
                {
                    isComplete = true;
                    postInvalidate();
                }
            }
        }

    };
}
