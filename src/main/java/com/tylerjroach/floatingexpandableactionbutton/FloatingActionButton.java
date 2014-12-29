package com.tylerjroach.floatingexpandableactionbutton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class FloatingActionButton extends ImageButton {

  public static final int SIZE_NORMAL = 0;
  public static final int SIZE_MINI = 1;

  private static final int HALF_TRANSPARENT_WHITE = Color.argb(128, 255, 255, 255);
  private static final int HALF_TRANSPARENT_BLACK = Color.argb(128, 0, 0, 0);

  int mColorNormal;
  int mColorPressed;
  int mColorSelected;
  @DrawableRes
  private int mIcon;
  private int mSize;

  private float mCircleSize;
  private int mDrawableSize;

  public FloatingActionButton(Context context) {
    this(context, null);
  }

  public FloatingActionButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public FloatingActionButton(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs);
  }

  void init(Context context, AttributeSet attributeSet) {
    mColorNormal = getColor(android.R.color.holo_blue_dark);
    mColorPressed = getColor(android.R.color.holo_blue_light);
    mColorSelected = getColor(android.R.color.holo_blue_light);
    mIcon = 0;
    mSize = SIZE_NORMAL;
    if (attributeSet != null) {
      initAttributes(context, attributeSet);
    }

    mCircleSize = getDimension(mSize == SIZE_NORMAL ? R.dimen.fab_size_normal : R.dimen.fab_size_mini);
    mDrawableSize = (int) (mCircleSize);

    updateBackground();
  }

  int getColor(@ColorRes int id) {
    return getResources().getColor(id);
  }

  float getDimension(@DimenRes int id) {
    return getResources().getDimension(id);
  }

  private void initAttributes(Context context, AttributeSet attributeSet) {
    TypedArray attr = context.obtainStyledAttributes(attributeSet, R.styleable.FloatingActionButton, 0, 0);
    if (attr != null) {
      try {
        mColorNormal = attr.getColor(R.styleable.FloatingActionButton_colorNormal, getColor(android.R.color.holo_blue_dark));
        mColorPressed = attr.getColor(R.styleable.FloatingActionButton_colorPressed, getColor(android.R.color.holo_blue_light));
        mColorSelected = attr.getColor(R.styleable.FloatingActionButton_colorSelected, getColor(android.R.color.holo_blue_light));
        mSize = attr.getInt(R.styleable.FloatingActionButton_size, SIZE_NORMAL);
        mIcon = attr.getResourceId(R.styleable.FloatingActionButton_insetIcon, 0);
      } finally {
        attr.recycle();
      }
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    setMeasuredDimension(mDrawableSize, mDrawableSize);
  }

  void updateBackground() {

    final RectF circleRect = new RectF(0, 0, mCircleSize, mCircleSize);

    LayerDrawable layerDrawable = new LayerDrawable(
        new Drawable[] {
            createFillDrawable(circleRect),
            getIconDrawable()
        });

    float iconOffset = (mCircleSize - getDimension(R.dimen.fab_icon_size)) / 2f;

    int iconInsetHorizontal = (int) (iconOffset);
    int iconInsetTop = (int) (iconOffset);
    int iconInsetBottom = (int) (iconOffset);

    layerDrawable.setLayerInset(1, iconInsetHorizontal, iconInsetTop, iconInsetHorizontal, iconInsetBottom);

    setBackgroundCompat(layerDrawable);
  }


  public void FABChangeIcon(int id) {



      final RectF circleRect = new RectF(0, 0, mCircleSize, mCircleSize);

      LayerDrawable layerDrawable = new LayerDrawable(
              new Drawable[] {
                      createFillDrawable(circleRect),
                      getIconDrawable(id)
              });

      float iconOffset = (mCircleSize - getDimension(R.dimen.fab_icon_size)) / 2f;

      int iconInsetHorizontal = (int) (iconOffset);
      int iconInsetTop = (int) (iconOffset);
      int iconInsetBottom = (int) (iconOffset);

      layerDrawable.setLayerInset(1, iconInsetHorizontal, iconInsetTop, iconInsetHorizontal, iconInsetBottom);

      setBackgroundCompat(layerDrawable);

  }

  Drawable getIconDrawable(int id) {
      return getResources().getDrawable(id);
  }

  Drawable getIconDrawable() {
    if (mIcon != 0) {
      return getResources().getDrawable(mIcon);
    } else {
      return new ColorDrawable(Color.TRANSPARENT);
    }
  }

  private StateListDrawable createFillDrawable(RectF circleRect) {
    StateListDrawable drawable = new StateListDrawable();
    drawable.addState(new int[] { android.R.attr.state_pressed }, createCircleDrawable(circleRect, mColorPressed));
      drawable.addState(new int[] { android.R.attr.state_selected }, createCircleDrawable(circleRect, mColorSelected));
    drawable.addState(new int[] { }, createCircleDrawable(circleRect, mColorNormal));
    return drawable;
  }

  private Drawable createCircleDrawable(RectF circleRect, int color) {
    final Bitmap bitmap = Bitmap.createBitmap(mDrawableSize, mDrawableSize, Config.ARGB_8888);
    final Canvas canvas = new Canvas(bitmap);

    final Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setColor(color);

    canvas.drawOval(circleRect, paint);

    return new BitmapDrawable(getResources(), bitmap);
  }


  @SuppressWarnings("deprecation")
  @SuppressLint("NewApi")
  private void setBackgroundCompat(Drawable drawable) {
    if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
      setBackground(drawable);
    } else {
      setBackgroundDrawable(drawable);
    }
  }
}
