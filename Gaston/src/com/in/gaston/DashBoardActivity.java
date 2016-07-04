package com.in.gaston;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.capricorn.TumblrMenu;
import com.in.gaston.constant.AppPreferences;
import com.in.gaston.fragment.HomeFragment;
import com.in.gaston.fragment.NotificationFragment;
import com.in.gaston.fragment.ProfileFragment;
import com.in.gaston.fragment.SettingFragment;

public class DashBoardActivity extends FragmentActivity 
{
	private TumblrMenu tmblrMenu,mTumblrMenu;
	private RelativeLayout mMainRL;
	private AppPreferences mAppPreferences;
	private String back_press_val="";
	private HomeFragment myFragment;
	private boolean isActivity_feed = false;
	private static final int[] ITEM_DRAWABLES = { R.drawable.setting, R.drawable.notification,
		R.drawable.user, R.drawable.create_profile, R.drawable.home };

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dash_board);
		mAppPreferences = AppPreferences.getInstance(DashBoardActivity.this);
		initVariables();
		iniView();
		setMenuOption();
		mTumblrMenu =  new TumblrMenu(DashBoardActivity.this);
		mTumblrMenu.isMenuOpen(false);

		initialHomeFragment(false);

	}

	private void initVariables() 
	{
	}
	/**
	 * arc menu 
	 */
	private void setMenuOption() 
	{

		final int itemCount = ITEM_DRAWABLES.length;
		for (int i = 0; i < itemCount; i++) 
		{
			final int position = i;
			View linView =  LayoutInflater.from(DashBoardActivity.this).inflate(R.layout.xml_image,null);
			ImageView imageView = (ImageView) linView.findViewById(R.id.img_view);
			imageView.setImageResource(ITEM_DRAWABLES[i]);
			linView.setTag(i+"");
			tmblrMenu.addItem(linView,new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					initialHomeFragment(false);
					setSelection(position);
				}
			});
		}
	}
	private void iniView() 
	{
		tmblrMenu = (TumblrMenu) findViewById(R.id.tmblr_menu);
		mMainRL =  (RelativeLayout)findViewById(R.id.rl_view);
	}

	/**
	 * Fragment selection
	 */

	private void setSelection(int position) 
	{
		switch (position) 
		{
		case 0:
			tmblrMenu.isMenuOpen(false);
			openSettingTab();

			break;
		case 1:
			tmblrMenu.isMenuOpen(false);
			openNotificationTab();
			break;
		case 2:
			tmblrMenu.isMenuOpen(false);
			openProfileTab();
			break;

		case 3:
			tmblrMenu.isMenuOpen(false);
			Intent intent = new Intent(DashBoardActivity.this,CreateInterestActivity.class);
			startActivity(intent);
			finish();


			break;

		case 4:
			tmblrMenu.isMenuOpen(false);
			initialHomeFragment(false);
			break;

		default:
			break;
		}
	}


	public void initialHomeFragment(boolean isActivity_feed) 
	{

		 myFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("Home_Fragment");
		if (myFragment != null && myFragment.isVisible()&&!isActivity_feed) 
		{
//			Toast.makeText(DashBoardActivity.this,"Already home open", Toast.LENGTH_SHORT).show();
		}
		else
		{
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

			Fragment mFragment = new HomeFragment();
			String LOADED_FRAGMENT_TAG = "Home_Fragment";
			ft.replace(R.id.fl_frame_layout, mFragment, LOADED_FRAGMENT_TAG);
			ft.commit();
			getSupportFragmentManager().executePendingTransactions();
		}
	}

	// open profile tab
	public void openProfileTab() 
	{
		
		ProfileFragment myFragment = (ProfileFragment)getSupportFragmentManager().findFragmentByTag("Profile_Fragment");
		if (myFragment != null && myFragment.isVisible()) 
		{
		//	Toast.makeText(DashBoardActivity.this,"Already profile open", Toast.LENGTH_SHORT).show();
		}
		else
		{
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			Fragment mFragment = new ProfileFragment();
			String LOADED_FRAGMENT_TAG = "Profile_Fragment";
			ft.replace(R.id.fl_frame_layout, mFragment, LOADED_FRAGMENT_TAG);
			ft.commit();
			getSupportFragmentManager().executePendingTransactions();
		}
	}

	
	//open setting tab
	public void openSettingTab() 
	{
		
		SettingFragment myFragment = (SettingFragment)getSupportFragmentManager().findFragmentByTag("Setting_fragment");
		if (myFragment != null && myFragment.isVisible()) 
		{
		//	Toast.makeText(DashBoardActivity.this,"Already profile open", Toast.LENGTH_SHORT).show();
		}
		else
		{
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			Fragment mFragment = new SettingFragment();
			String LOADED_FRAGMENT_TAG = "Setting_fragment";
			ft.replace(R.id.fl_frame_layout, mFragment, LOADED_FRAGMENT_TAG);
			ft.commit();
			getSupportFragmentManager().executePendingTransactions();
		}
	}

	
	//open notification tab
		public void openNotificationTab() 
		{
			
			NotificationFragment myFragment = (NotificationFragment)getSupportFragmentManager().findFragmentByTag("Noti_fragment");
			if (myFragment != null && myFragment.isVisible()) 
			{
			//	Toast.makeText(DashBoardActivity.this,"Already profile open", Toast.LENGTH_SHORT).show();
			}
			else
			{
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				Fragment mFragment = new NotificationFragment();
				String LOADED_FRAGMENT_TAG = "Noti_fragment";
				ft.replace(R.id.fl_frame_layout, mFragment, LOADED_FRAGMENT_TAG);
				ft.commit();
				getSupportFragmentManager().executePendingTransactions();
			}
		}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) 
	{
		super.onActivityResult(arg0, arg1, arg2);

//		Toast.makeText(DashBoardActivity.this, "onActivityResult=>parent", Toast.LENGTH_SHORT).show();

	}
	
	
	public void back_press_val(String val)
	{
		this.back_press_val = val;
	}
	
	@Override
	public void onBackPressed() 
	{
		if(back_press_val.equalsIgnoreCase("activity_feeds"))
		{
			initialHomeFragment(true);
		}
		else if(back_press_val.equalsIgnoreCase("setting"))
		{

			initialHomeFragment(false);
		}
		else if(back_press_val.equalsIgnoreCase("notification"))
		{
			initialHomeFragment(false);
		}
		else if(back_press_val.equalsIgnoreCase("profile"))
		{
			initialHomeFragment(false);
		}
		else if (back_press_val.equalsIgnoreCase("interest")) 
		{
			finish();
		}
		else
		{
			finish();
		}
	}
	
	
	public void stop_listViewScroll()
	{
		
		 myFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("Home_Fragment");

		if (myFragment != null && myFragment.isVisible()) 
		{
			myFragment.disable_listview_scroll();
		}
	}
}