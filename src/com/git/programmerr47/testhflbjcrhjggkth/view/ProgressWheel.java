package com.git.programmerr47.testhflbjcrhjggkth.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import com.git.programmerr47.testhflbjcrhjggkth.R;

/**
 * An indicator of progress similar to Android's ProgressBar.
 */
public class ProgressWheel extends View {

    private int layout_height = 0;
    private int layout_width = 0;
    private int fullRadius = 100;
    private int circleRadius = 80;
    private int barLength = 60;
    private int barWidth = 10;
    private int rimWidth = 10;
    private int textSize = 20;

    private int paddingTop = 5;
    private int paddingBottom = 5;
    private int paddingLeft = 5;
    private int paddingRight = 5;

    private int barColor = 0xAA000000;
    private int circleColor = 0x00000000;
    private int rimColor = 0xAADDDDDD;
    private int textColor = 0xFF000000;

    private Paint barPaint = new Paint();
    private Paint circlePaint = new Paint();
    private Paint rimPaint = new Paint();
    private Paint textPaint = new Paint();

    private RectF rectBounds = new RectF();
    private RectF circleBounds = new RectF();

    private OnLoadingListener listener;

    private int spinSpeed = 2;
    private int delayMillis = 0;
    private Handler spinHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            invalidate();
            if(isSpinning) {
                progress+=spinSpeed;
                if(progress>360) {
                    progress = 0;
                }
                spinHandler.sendEmptyMessageDelayed(0, delayMillis);
            }
        }
    };
    int progress = 0;
    boolean isSpinning = false;

    private String text = "";
    private String[] splitText = {};

    public ProgressWheel(Context context, AttributeSet attrs) {
        super(context, attrs);

        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.ProgressWheel));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        layout_width = w;
        layout_height = h;

        setupBounds();
        setupPaints();
        invalidate();
    }

    private void setupPaints() {
        barPaint.setColor(barColor);
        barPaint.setAntiAlias(true);
        barPaint.setStyle(Style.STROKE);
        barPaint.setStrokeWidth(barWidth);

        rimPaint.setColor(rimColor);
        rimPaint.setAntiAlias(true);
        rimPaint.setStyle(Style.STROKE);
        rimPaint.setStrokeWidth(rimWidth);

        circlePaint.setColor(circleColor);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Style.FILL);

        textPaint.setColor(textColor);
        textPaint.setStyle(Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
    }

    private void setupBounds() {
        int minValue = Math.min(layout_width, layout_height);

        int xOffset = layout_width - minValue;
        int yOffset = layout_height - minValue;

        paddingTop = this.getPaddingTop() + (yOffset / 2);
        paddingBottom = this.getPaddingBottom() + (yOffset / 2);
        paddingLeft = this.getPaddingLeft() + (xOffset / 2);
        paddingRight = this.getPaddingRight() + (xOffset / 2);

        rectBounds = new RectF(paddingLeft,
                paddingTop,
                this.getLayoutParams().width - paddingRight,
                this.getLayoutParams().height - paddingBottom);

        circleBounds = new RectF(paddingLeft,
                paddingTop,
                this.getLayoutParams().width - paddingRight,
                this.getLayoutParams().height - paddingBottom);

        fullRadius = (this.getLayoutParams().width - paddingRight - barWidth)/2;
        circleRadius = (fullRadius - barWidth) + 1;
    }

    private void parseAttributes(TypedArray a) {
        barWidth = (int) a.getDimension(R.styleable.ProgressWheel_barWidth,
                barWidth);

        rimWidth = (int) a.getDimension(R.styleable.ProgressWheel_rimWidth,
                rimWidth);

        spinSpeed = (int) a.getDimension(R.styleable.ProgressWheel_spinSpeed,
                spinSpeed);

        delayMillis = (int) a.getInteger(R.styleable.ProgressWheel_delayMillis,
                delayMillis);
        if(delayMillis<0) {
            delayMillis = 0;
        }

        barColor = a.getColor(R.styleable.ProgressWheel_barColor, barColor);

        barLength = (int) a.getDimension(R.styleable.ProgressWheel_barLength,
                barLength);

        textSize = (int) a.getDimension(R.styleable.ProgressWheel_textSize,
                textSize);

        textColor = (int) a.getColor(R.styleable.ProgressWheel_textColor,
                textColor);

        rimColor = (int) a.getColor(R.styleable.ProgressWheel_rimColor,
                rimColor);

        circleColor = (int) a.getColor(R.styleable.ProgressWheel_circleColor,
                circleColor);

        a.recycle();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(circleBounds, 360, 360, false, rimPaint);

        if(isSpinning) {
            canvas.drawArc(circleBounds, progress - 90, barLength, false,
                    barPaint);
        } else {
            canvas.drawArc(circleBounds, -90, progress, false, barPaint);
        }

        canvas.drawCircle((circleBounds.width()/2) + rimWidth + paddingLeft,
                (circleBounds.height()/2) + rimWidth + paddingTop,
                circleRadius,
                circlePaint);

        float textHeight = textPaint.descent() - textPaint.ascent();
        float verticalTextOffset = (textHeight / 2) - textPaint.descent();

        for(String s : splitText) {
            float horizontalTextOffset = textPaint.measureText(s) / 2;
            canvas.drawText(s, this.getWidth() / 2 - horizontalTextOffset,
                    this.getHeight() / 2 + verticalTextOffset, textPaint);
        }
    }

    public void resetCount() {
        progress = 0;
//        invalidate();
    }

    public void stopSpinning() {
        isSpinning = false;
        progress = 0;
        spinHandler.removeMessages(0);
    }

    public void spin() {
        isSpinning = true;
        spinHandler.sendEmptyMessage(0);
    }

    public void incrementProgress() {
        isSpinning = false;
        progress++;
        if (progress >= 360) {
            if (listener != null) {
                listener.onComplete();
            }
        }
        spinHandler.sendEmptyMessage(0);
    }

    public void setProgress(int i) {
        isSpinning = false;
        progress=i;
        spinHandler.sendEmptyMessage(0);
    }

    public void setText(String text) {
        this.text = text;
        splitText = this.text.split("\n");
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public void setOnLoadingListener(OnLoadingListener listener) {
        this.listener = listener;
    }

    public interface OnLoadingListener {
        void onComplete();
    }
}
