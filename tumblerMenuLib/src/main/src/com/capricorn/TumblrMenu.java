package com.capricorn;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class TumblrMenu extends RelativeLayout 
{

	private boolean isExpanded = false;
	private View view ;
	private Context mContext;
	public boolean isMenuClicked = false;

	public enum MenuAnchor{
		TOP_LEFT,
		TOP_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT
	}

	public static final MenuAnchor DEFAULT_MENU_ANCHOR = MenuAnchor.BOTTOM_RIGHT;


	private MenuAnchor mMenuAnchorAT = DEFAULT_MENU_ANCHOR;

	private TumblrLayout mTumblrLayout;

	private ImageView mHintView;



	public TumblrMenu(Context context) 
	{
		super(context);

		init(context);
	}

	public TumblrMenu(Context context, AttributeSet attrs) 
	{

		super(context, attrs);
		mContext = context;
		init(context);
	}

	public void isMenuOpen(boolean val)
	{
		isMenuClicked = val;
		setBackgroundColor(Color.parseColor("#00000000"));

	}
	
	
	
	private void init(Context context) {
		setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		setClipChildren(false);

		LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.tumblr_menu, this);

		mTumblrLayout = (TumblrLayout) findViewById(R.id.item_layout);

		final ViewGroup controlLayout = (ViewGroup) findViewById(R.id.control_layout);
		controlLayout.setClickable(true);
		controlLayout.setOnTouchListener(new OnTouchListener() {


			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN) 
				{
					
					if(!isMenuClicked)
					{
						isMenuClicked = true;
						setBackgroundColor(Color.parseColor("#99000000"));
						
					}
					else
					{
						isMenuClicked = false;
						setBackgroundColor(Color.parseColor("#00000000"));

					}
					mHintView.startAnimation(createHintSwitchAnimation(mTumblrLayout.isExpanded()));
					mTumblrLayout.switchState(true);

				}

				return false;
			}
		});

		mHintView = (ImageView) findViewById(R.id.control_hint);
	}

	public void addItem(View item,OnClickListener listener) 
	{
		view  =item;

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		item.setId(1);
		mTumblrLayout.addView(item,Integer.parseInt(""+item.getTag()));
		item.setOnClickListener(getItemClickListener(listener));
	}

	private OnClickListener getItemClickListener(final OnClickListener listener) 
	{
		return new OnClickListener() {

			@Override
			public void onClick(final View viewClicked) {
				Animation animation = bindItemAnimation(viewClicked, true, 400);
				animation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						postDelayed(new Runnable() {

							@Override
							public void run() {
								itemDidDisappear();
							}
						}, 0);
					}
				});

				final int itemCount = mTumblrLayout.getChildCount();
				for (int i = 0; i < itemCount; i++) {
					View item = mTumblrLayout.getChildAt(i);
					if (viewClicked != item) {
						bindItemAnimation(item, false, 300);
					}
				}

				mTumblrLayout.invalidate();
				mHintView.startAnimation(createHintSwitchAnimation(true));

				if (listener != null) {
					listener.onClick(viewClicked);
				}
			}
		};
	}

	private Animation bindItemAnimation(final View child, final boolean isClicked, final long duration) {
		Animation animation = createItemDisapperAnimation(duration, isClicked);
		child.setAnimation(animation);

		return animation;
	}

	private void itemDidDisappear() {
		final int itemCount = mTumblrLayout.getChildCount();
		for (int i = 0; i < itemCount; i++) {
			View item = mTumblrLayout.getChildAt(i);
			item.clearAnimation();
		}

		mTumblrLayout.switchState(false);
	}

	private static Animation createItemDisapperAnimation(final long duration, final boolean isClicked) {
		AnimationSet animationSet = new AnimationSet(true);
		animationSet.addAnimation(new ScaleAnimation(1.0f, isClicked ? 2.0f : 0.0f, 1.0f, isClicked ? 2.0f : 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
		animationSet.addAnimation(new AlphaAnimation(1.0f, 0.0f));
		animationSet.setDuration(duration);
		animationSet.setInterpolator(new DecelerateInterpolator());
		animationSet.setFillAfter(true);

		return animationSet;
	}

	private static Animation createHintSwitchAnimation(final boolean expanded) {
		Animation animation = new RotateAnimation(expanded ? 45 : 0, expanded ? 0 : 45, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setStartOffset(0);
		animation.setDuration(100);
		animation.setInterpolator(new DecelerateInterpolator());
		animation.setFillAfter(true);

		return animation;
	}

}
