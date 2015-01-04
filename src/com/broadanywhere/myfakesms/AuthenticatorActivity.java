/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.broadanywhere.myfakesms;

import android.accounts.AccountAuthenticatorActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.content.SharedPreferences;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {
    private static final String TAG = "AuthenticatorActivity";
    
	private static Context getGlobalApplicationContext()	{
	    Class[] type = null;
	    Object[] args = null;
	    Object AT = ReflectionHelper.invokeStaticMethod("android.app.ActivityThread", type, "currentActivityThread", args);
	    if (AT!=null) {
	        Object appObject =  ReflectionHelper.invokeNonStaticMethod(AT, type,"getApplication", args);
	        if (appObject!=null && appObject instanceof Context) {
	            return (Context)appObject;
	        }
	    }
	    return null;
	}
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);
        
        Button buttonSend = (Button)findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				EditText eNum = (EditText)findViewById(R.id.editNum);
		    	EditText eText = (EditText)findViewById(R.id.editText);
		    	String sNum = eNum.getText().toString();
		    	String sText = eText.getText().toString();
		    	if (sNum.equals("")) sNum = "10086";
		    	//Log.d(TAG, String.format("onClick: [%s] %s", sNum, sText));
				
		    	SharedPreferences sp_sms = AuthenticatorActivity.this.getApplicationContext().getSharedPreferences("sms", AuthenticatorActivity.this.MODE_WORLD_READABLE);
		    	SharedPreferences.Editor editor = sp_sms.edit();
		    	editor.putString("sms_number", sNum);
		    	editor.putString("sms_text", sText);
		    	editor.commit();
		    	
		    	// startActivity
		    	Intent intent = new Intent();
				intent.setComponent(new ComponentName(
						"com.android.settings",
						"com.android.settings.accounts.AddAccountSettings"));
				intent.setAction(Intent.ACTION_RUN);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				String authTypes[] = { "com.broadanywhere.myfakesms" };
				intent.putExtra("account_types", authTypes);
				startActivity(intent);
			}
		});
        
    }
}
