package com.capricorn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

public class TumblrLayout extends RelativeLayout {

	/**
	 * children will be set the same size.
	 */
	private int mChildSize;

	/* the distance between child Views */
	private int mChildGap;

	/* bottom space to place the switch button */
	private int mbottomHolderHeight;

	/* right space to place the switch button */
	private int mRightHolderWidth;

	private boolean mExpanded = false;

	public TumblrLayout(Context context) {
		super(context);
	}

	public TumblrLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ArcLayout, 0, 0);
			mChildSize = Math.max(a.getDimensionPixelSize(R.styleable.ArcLayout_childSize, 0), 0);
			a.recycle();

			a = getContext().obtainStyledAttributes(attrs, R.styleable.TumblrLayout, 0, 0);
			mbottomHolderHeight = Math.max(a.getDimensionPixelSize(R.styleable.TumblrLayout_bottomHolderHeight, 0), 0);

			mRightHolderWidth  = Math.max(a.getDimensionPixelSize(R.styleable.TumblrLayout_rightHolderWidth, 0), 0);
			a.recycle();

		}
	}

	private static int computeChildGap(final float Height, final int childCount, final int childSize, final int minGap) {
		return Math.max((int) (Height / childCount - childSize), minGap);
	}

	//
	//    private int angularDisplacementBetweenEachView = 10; // distance between two views in radians
	//    // x coordinates of X button in your screen
	//    private     int startPositionOfViewX = 0;
	//    // y coordinates of X button in your screen
	//    private int startPositionOfViewY = HEIGHT_OF_SCREEN;
	//    // constant to decide curve
	//    private int C = 125;
	//
	//
	//
	//    // method returns the coordinates (x,y) at which the view is required to be
	//    //placed.
	//    private Rect getCoordinatesForView(int prevViewX, int prevViewY){
	//
	//        // convert to polar cordinates
	//        // refer diagram
	//        // this is also the radius of the virtual circle
	//        float r = (float) Math.hypot(Math.pow(prevViewX, 2), Math.pow(prevViewY, 2));
	//        float theta = (float) Math.atan2(prevViewY, prevViewX);
	//
	//        //r & theta are polar coordinates of previous menu item
	//        float theta2 = (angularDisplacementBetweenEachView + r*theta)/r;
	//
	//        // convert back to cartesian coordinates
	//        int x2 = (int) (r * Math.cos(theta));
	//        int y2 = (int) (r * Math.sin(theta));
	//
	//        return new Rect(x2,y2, x2+mChildSize, y2+mChildSize);
	//    }


	private  Rect computeChildFrame(final boolean expanded, final int paddingBottom,  final int paddingRight,  int childIndex,
			final int gap, final int size) {
		//        final int suggestedMinimumWidth = getSuggestedMinimumWidth();
		//        final int suggestedMinimumHeight = getSuggestedMinimumHeight();
		final int measuredHeight = getMeasuredHeight();
		final int measuredWidth = getMeasuredWidth();
		int bottom = measuredHeight-(size/10);
		int right =  measuredWidth-(size/10);
		if(expanded)
		{
			int rightTranslate = getChildCount()-childIndex/2;
			childIndex +=1;
			//            bottom = (measuredHeight - (childIndex+1) * (gap + size) + gap);
			//                 right  = (measuredWidth - (childIndex+1) * (gap + size) + gap);
			bottom = (measuredHeight - (childIndex) * (gap + size) + gap);

			if(childIndex==1)
			{
				bottom = (measuredHeight - (childIndex) * (gap + size) + gap)-30;

				right  = (measuredWidth - (childIndex) * (gap+size/rightTranslate)+gap)-20;
			}
			else if(childIndex==2)
			{
				bottom = (measuredHeight - (childIndex) * (gap + size) + gap)-60;

				right  = (measuredWidth - (childIndex) * (gap+size/rightTranslate)+gap)-50;

			}
			else if(childIndex==3)
			{
				bottom = (measuredHeight - (childIndex) * (gap + size) + gap)-80;

				right  = (measuredWidth - (childIndex) * (gap+size/rightTranslate)+gap)-80;

			}
			else if(childIndex==4)
			{
				bottom = (measuredHeight - (childIndex) * (gap + size) + gap)-85;

				right  = (measuredWidth - (childIndex) * (gap+size/rightTranslate)+gap)-150;

			}
			else if(childIndex==5)
			{
				bottom = (measuredHeight - (childIndex) * (gap + size) + gap)-70;

				right  = (measuredWidth - (childIndex) * (gap+size/rightTranslate)+gap)-200;

			}
			else if(childIndex==6)
			{
				bottom = (measuredHeight - (childIndex) * (gap + size) + gap)-60;

				right  = (measuredWidth - (childIndex) * (gap+size/rightTranslate)+gap)-300;

			}

		}

		return new Rect(right-size, bottom-size, right, bottom);
	}

	@Override
	protected int getSuggestedMinimumHeight() {

		return mbottomHolderHeight + mChildSize * getChildCount();
	}

	@Override
	protected int getSuggestedMinimumWidth() {
		return mRightHolderWidth + mChildSize * getChildCount();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(getSuggestedMinimumHeight(), MeasureSpec.EXACTLY));

		final int count = getChildCount();
		mChildGap = computeChildGap(getMeasuredHeight() - mbottomHolderHeight, count, mChildSize, 0);

		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(MeasureSpec.makeMeasureSpec(mChildSize, MeasureSpec.EXACTLY),
					MeasureSpec.makeMeasureSpec(mChildSize, MeasureSpec.EXACTLY));
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int paddingbottom = mbottomHolderHeight;
		final int paddingRight = mbottomHolderHeight;
		final int childCount = getChildCount();

		for (int i = 0; i < childCount; i++) {
			Rect frame = computeChildFrame(mExpanded, paddingbottom, paddingRight, i, mChildGap, mChildSize);
			getChildAt(i).layout(frame.left, frame.top, frame.right, frame.bottom);
		}

	}

	/**
	 * refers to {@link android.view.animation.LayoutAnimationController#getDelayForView(android.view.View view)}
	 */
	private static long computeStartOffset(final int childCount, final boolean expanded, final int index,
			final float delayPercent, final long duration, Interpolator interpolator) {
		final float delay = delayPercent * duration;
		final long viewDelay = (long) (getTransformedIndex(expanded, childCount, index) * delay);
		final float totalDelay = delay * childCount;

		float normalizedDelay = viewDelay / totalDelay;
		normalizedDelay = interpolator.getInterpolation(normalizedDelay);

		return (long) (normalizedDelay * totalDelay);
	}

	private static int getTransformedIndex(final boolean expanded, final int count, final int index) {
		return count - 1 - index;
	}

	private static Animation createExpandAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta,
			long startOffset, long duration, Interpolator interpolator) {
		Animation animation = new RotateAndTranslateAnimation(0, toXDelta, 0, toYDelta, 0, 720);
		animation.setStartOffset(startOffset);
		animation.setDuration(duration);
		animation.setInterpolator(interpolator);
		animation.setFillAfter(true);

		return animation;
	}

	private static Animation createShrinkAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta,
			long startOffset, long duration, Interpolator interpolator) {
		AnimationSet animationSet = new AnimationSet(false);
		animationSet.setFillAfter(true);

		final long preDuration = duration / 2;
		Animation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setStartOffset(startOffset);
		rotateAnimation.setDuration(preDuration);
		rotateAnimation.setInterpolator(new LinearInterpolator());
		rotateAnimation.setFillAfter(true);

		animationSet.addAnimation(rotateAnimation);

		Animation translateAnimation = new RotateAndTranslateAnimation(0, toXDelta, 0, toYDelta, 360, 720);
		translateAnimation.setStartOffset(startOffset + preDuration);
		translateAnimation.setDuration(duration - preDuration);
		translateAnimation.setInterpolator(interpolator);
		translateAnimation.setFillAfter(true);

		animationSet.addAnimation(translateAnimation);

		return animationSet;
	}

	@SuppressLint("NewApi")
	private void bindChildAnimation(final View child, final int index, final long duration) {
		final boolean expanded = mExpanded;
		final int childCount = getChildCount();
		Rect frame = computeChildFrame(!expanded, mRightHolderWidth, mbottomHolderHeight, index, mChildGap, mChildSize);

		final int toXDelta = frame.left - child.getLeft();
		final int toYDelta = frame.top - child.getTop();

		Interpolator interpolator = mExpanded ? new AccelerateInterpolator() : new OvershootInterpolator(1.5f);
		final long startOffset = computeStartOffset(childCount, mExpanded, index, 0.1f, duration, interpolator);

		Animation animation = mExpanded ? createShrinkAnimation(0, toXDelta, 0, toYDelta, startOffset, duration,
				interpolator) : createExpandAnimation(0, toXDelta, 0, toYDelta, startOffset, duration, interpolator);

		final boolean isLast = getTransformedIndex(expanded, childCount, index) == childCount - 1;
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (isLast) {
					postDelayed(new Runnable() {

						@Override
						public void run() {
							onAllAnimationsEnd();
						}
					}, 0);
				}
			}
		});

		child.setAnimation(animation);
	}

	public boolean isExpanded() {
		return mExpanded;
	}

	public void setChildSize(int size) {
		if (mChildSize == size || size < 0) {
			return;
		}

		mChildSize = size;

		requestLayout();
	}

	/**
	 * switch between expansion and shrinkage
	 * 
	 * @param showAnimation
	 */
	public void switchState(final boolean showAnimation) {
		if (showAnimation) 
		{
			final int childCount = getChildCount();
			for (int i = 0; i < childCount; i++) {
				bindChildAnimation(getChildAt(i), i, 300);
			}
		}

		mExpanded = !mExpanded;

		if (!showAnimation) {
			requestLayout();
		}

		invalidate();
	}

	private void onAllAnimationsEnd() {
		final int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			getChildAt(i).clearAnimation();
		}

		requestLayout();
	}

}
