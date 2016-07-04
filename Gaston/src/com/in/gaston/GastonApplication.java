package com.in.gaston;

import android.app.Application;

import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;

public class GastonApplication extends Application {
	Permission[] permissions = new Permission[] {
		    Permission.USER_PHOTOS,
		    Permission.EMAIL,
		    Permission.PUBLISH_ACTION
		};
	
	@Override
	public void onCreate() {
		super.onCreate();
		SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
	    .setAppId("871635946236749")
	    .setNamespace("gastonapp")
	    .setPermissions(permissions)
	    .build();
		
		SimpleFacebook.setConfiguration(configuration);
	}
}
