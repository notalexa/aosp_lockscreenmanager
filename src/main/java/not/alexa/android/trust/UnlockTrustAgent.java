
package not.alexa.android.trust;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.UserHandle;
import android.service.trust.AuthSet;
import android.service.trust.TrustAgentService;
import android.util.Slog;

public class UnlockTrustAgent extends TrustAgentService {
    private static final String TAG="UnlockTrustAgent";
    private UnlockApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        app=(UnlockApp)getApplication();
        Slog.i(UnlockApp.TAG,"Create "+getClass().getSimpleName());
        setManagingTrust(true);
        app.register(this);
    }
    
    public void grantTrust(AuthSet set) {
    	grantTrust(set,TAG,TimeUnit.HOURS.toMillis(24),0);
    }


    @Override
    protected Intent nextChallenge() {
        return app.getCurrentSecurityState().getLockScreen();
    }
    
    public void initiateEscrowToken(int uid) {
        Slog.i(TAG,"Escrow token not present. Add escrow token for user "+uid+".");
        String pending=app.getPreferences().getString("escrow."+uid+".pending",null);
        if(pending==null) {
            pending=UUID.randomUUID().toString();
            addEscrowToken(pending.getBytes(),UserHandle.of(uid));
            app.getPreferences().edit().putString("escrow."+uid+".pending",pending).commit();
        } else {
            Slog.i(TAG,"Pending escrow token for user "+uid+" found. Do nothing.");
        }
    }

    protected void onCallbackSet() {
    	if(app.getPreferences().getBoolean("allow.escrowtoken",false)) {
	        int uid=ActivityManager.getCurrentUser();
	        String token=app.getPreferences().getString("escrow."+uid+".token",null);
	        if(token!=null) {
	            Slog.i(TAG,"Escrow token present for user "+uid+". Unlock user and grant trust");
	            long tokenHandle=app.getPreferences().getLong("escrow."+uid+".handle",-1);
	            unlockUserWithToken(tokenHandle,token.getBytes(), UserHandle.of(uid));
	        } else {
	        	initiateEscrowToken(uid);
	        }
    	}
    	update();
    }

	@Override
    public void onEscrowTokenAdded(byte[] token, long handle, UserHandle user) {
        String s=new String(token);
        String tok=app.getPreferences().getString("escrow."+user.getIdentifier()+".pending",null);
        if(s.equals(tok)) {
            Slog.i(TAG,"Pending escrow token added for user "+user.getIdentifier());
            app.getPreferences().edit().putString("escrow."+user.getIdentifier()+".token",s).putLong("escrow."+user.getIdentifier()+".handle",handle).remove("escrow."+user.getIdentifier()+".pending").commit();
        } else {
            Slog.w(TAG,"Pending token mismatch: Expected "+tok+", got "+s+". Ignore");
        }
        super.onEscrowTokenAdded(token, handle, user);
    }

    @Override
    public void onEscrowTokenStateReceived(long handle, int tokenState) {
        super.onEscrowTokenStateReceived(handle, tokenState);
    }

    @Override
    public void onEscrowTokenRemoved(long handle, boolean successful) {
        super.onEscrowTokenRemoved(handle, successful);
    }

    public synchronized void update() {
        grantTrust(app.getAuth());
    }


    @Override
    public void onTrustTimeout() {
    	// Timeout not yet considered
        super.onTrustTimeout();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        app.remove(this);
    }
}
