package apps.charlesmuchene.com.eaglecrest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Charlo on 017 17 Aug.
 */
public class EagleCrestOverlayView extends LinearLayout
{
    private final CardboardOverlayEyeView leftView;
    private final CardboardOverlayEyeView rightView;
    private AlphaAnimation textFadeAnimation;

    public EagleCrestOverlayView(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        setOrientation(HORIZONTAL);

        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
        leftView = new CardboardOverlayEyeView(context, attributeSet);
        leftView.setLayoutParams(params);
        addView(leftView);

        rightView = new CardboardOverlayEyeView(context, attributeSet);
        rightView.setLayoutParams(params);
        addView(rightView);

        setDeptOffset(0.01f);
        setColor(Color.rgb(150, 255, 180));
        setVisibility(View.VISIBLE);

        textFadeAnimation = new AlphaAnimation(1.0f, 0.0f);
        textFadeAnimation.setDuration(5000);

    } // constructor

    public void show3DToast(String message)
    {
        setText(message);
        setTextAlpha(1f);
        textFadeAnimation.setAnimationListener(new EndAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation)
            {
                setTextAlpha(0f);
            } // on animation end
        });
    } // 3D toast

    private abstract class EndAnimationListener implements Animation.AnimationListener
    {
        @Override public void onAnimationStart(Animation animation) {}
        @Override public void onAnimationRepeat(Animation animation) {}
    } // end animation listener

    private void setDeptOffset(float offset)
    {
        leftView.setOffset(offset);
        rightView.setOffset(-offset);

    } // depth offset

    private void setText(String text)
    {
        leftView.setText(text);
        rightView.setText(text);

    } // set text

    private void setTextAlpha(float alpha)
    {
        leftView.setTextViewAlpha(alpha);
        rightView.setTextViewAlpha(alpha);

    } // set text alpha

    private void setColor(int color)
    {
        leftView.setColor(color);
        rightView.setColor(color);

    } // set color

    private class CardboardOverlayEyeView extends ViewGroup
    {
        private final ImageView imageView;
        private final TextView textView;
        private float offset;

        public CardboardOverlayEyeView(Context context, AttributeSet attributeSet)
        {
            super(context, attributeSet);
            imageView = new ImageView(context, attributeSet);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setAdjustViewBounds(true);
            addView(imageView);

            textView = new TextView(context, attributeSet);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14.0f);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setGravity(Gravity.CENTER);
            textView.setShadowLayer(3.0f, 0.0f, 0.0f, Color.DKGRAY);
            addView(textView);

        } // constructor

        public void setColor(int color)
        {
            imageView.setColorFilter(color);
            textView.setTextColor(color);

        } // set color

        public void setText(String text)
        {
            textView.setText(text);

        } // set text

        public void setTextViewAlpha(float alpha)
        {
            textView.setAlpha(alpha);

        } // text view alpha

        public void setOffset(float offset)
        {
            this.offset = offset;

        } // set offset

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom)
        {
            final int width = right - left;
            final int height = bottom - top;

            final float imageSize = 0.1f;
            final float verticalImageOffset = -0.07f;
            final float verticalTextPos = 0.52f;

            // Layout ImageView
            float adjustedOffset = offset;
            if (width > 1000)
                adjustedOffset = 3.8f * offset;

            float imageMargin = (1.0f - imageSize) / 2.0f;
            float leftMargin = (int) (width * (imageMargin + adjustedOffset));
            float topMargin = (int) (height * (imageMargin + verticalImageOffset));
            imageView.layout(
                    (int) leftMargin, (int) topMargin,
                    (int) (leftMargin + width * imageSize), (int) (topMargin + height * imageSize)
            );

            // Layout TextView
            leftMargin = adjustedOffset * width;
            topMargin = height * verticalTextPos;
            textView.layout(
                    (int) leftMargin, (int) topMargin,
                    (int) (leftMargin + width), (int) (topMargin + height * (1.0f - verticalTextPos))
            );

        } // on layout

    } // Overlay Eye view

} // Eagle Crest Overlay view
