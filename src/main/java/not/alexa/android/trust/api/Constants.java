package not.alexa.android.trust.api;

import android.content.ComponentName;
import android.content.Intent;

public class Constants {
	public static final Intent serviceIntent=new Intent();
	static {
		serviceIntent.setComponent(new ComponentName("com.trusting.unlocktrustagent","com.trusting.unlocktrustagent.GrantServiceImpl"));
	}
	public static final String TRUST_PERMISSION="com.trusing.unlocktrustagent.TRUST_PERMISSION";
	//public static final String ACTION_LOCK_SCREEN="com.trusting.unlocktrustagent.LOCK_SCREEN";
	//public static final String ACTION_SIM_UNLOCK="android.intent.ext.action.SIM_UNLOCK";
	//public static final String TOKEN="lockscreen.token";
	//public static final int SYSTEM_LOGIN=2;
	//public static final int LOCKSCREEN_LOGIN=1;
	//public static final int LOCKED=0;
	
	//public static final boolean GRANTED=true;
	//public static final boolean REVOKED=false;
	
}
