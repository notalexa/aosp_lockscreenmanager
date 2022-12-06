package not.alexa.android.trust;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.service.trust.AuthSet;
import android.service.trust.AuthType;
import android.util.Slog;
import not.alexa.android.trust.R;
import not.alexa.android.trust.settings.ChallengeItem;
import not.alexa.android.trust.settings.ChooseProvider;

public class UnlockApp extends Application implements OnSharedPreferenceChangeListener,ChooseProvider<ChallengeItem> {
    public static final String TAG = "UnlockTrustAgent";

    private File storeFile;
    private ChallengeStore store=new ChallengeStore();
    private Context protectedStorageContext;
    private SecurityState currentState;
	private static UnlockApp INSTANCE;
	private UnlockTrustAgent agent;
	private SharedPreferences prefs;
	private AuthSet externalAuths=AuthSet.NONE;
	
	public void register(UnlockTrustAgent agent) {
		this.agent=agent;
	}
	public void remove(UnlockTrustAgent agent) {
		if(this.agent==agent) {
			this.agent = null;
		}
	}

	public void update() {
		UnlockTrustAgent agent=this.agent;
		if(agent!=null) {
			agent.update();
		} else {
			Slog.w(TAG,"Cannot update: Agent not set.");
		}
	}

	@Override
	public void onCreate() {
        super.onCreate();
        protectedStorageContext=createDeviceProtectedStorageContext();
        storeFile=new File(protectedStorageContext.getFilesDir(),"challenges.dat");
        store=ChallengeStore.load(storeFile);
        INSTANCE=this;
        prefs=PreferenceManager.getDefaultSharedPreferences(protectedStorageContext);
        currentState=getStartComponentFromPreferences(prefs);
        Slog.i(TAG,"Current security state: "+currentState);
        prefs.registerOnSharedPreferenceChangeListener(this);
	}

	public SharedPreferences getPreferences() {
        return prefs;
	}
	

	@Override
	public void onTerminate() {
		super.onTerminate();
		if(prefs!=null) {
			prefs.unregisterOnSharedPreferenceChangeListener(this);
		}
	}
	
	public static UnlockApp getInstance() {
		return INSTANCE;
	}
	
    public boolean sendGrantTrust(AuthSet set) {
    	if(!set.equals(externalAuths)) {
        	AuthSet total=getAuth();
    		externalAuths=set;
    		if(!total.equals(getAuth())) {
    			agent.grantTrust(getAuth());
    		}
    	}
    	Slog.i(TAG,"Granting trust "+set);
    	return true;
    }

    public SecurityState getCurrentSecurityState() {
    	return currentState;
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if(key.startsWith("lockscreen.")) {
			AuthSet auth=currentState.getAuth();
			currentState=getStartComponentFromPreferences(sharedPreferences);
			Slog.i(TAG,"Changing state to "+currentState);
			if(!currentState.getAuth().equals(auth)) {
            	agent.grantTrust(getAuth());
			}
		} else if("allow.escrowtoken".equals(key)) {
			if(sharedPreferences.getBoolean(key,false)) {
				agent.initiateEscrowToken(ActivityManager.getCurrentUser());
			} else {
				agent.deleteEscrowToken(ActivityManager.getCurrentUser());
			}
		}
	}
	
	protected SecurityState getStartComponentFromPreferences(SharedPreferences preferences) {
		boolean grantSecret=preferences.getBoolean("lockscreen.grant.secret",false);
		boolean grantLocation=preferences.getBoolean("lockscreen.grant.location",false);
		boolean grantPossession=preferences.getBoolean("lockscreen.grant.possession",false);
		boolean grantInherent=preferences.getBoolean("lockscreen.grant.inherent",false);
		AuthSet auth= AuthSet.get(grantSecret? AuthType.Secret:null,grantLocation?AuthType.Location:null,grantPossession?AuthType.Possession:null,grantInherent?AuthType.Biometric:null);
        return new SecurityState(auth,store);
	}

    @Override
    public List<ChallengeItem> getEntries() {
        if(store.update(this)) {
        	store.store(storeFile);
            AuthSet old=currentState.getAuth();
        	currentState=getStartComponentFromPreferences(getPreferences());
            if(!old.equals(currentState.getAuth())) {
            	agent.grantTrust(getAuth());
            }
        }
        return store.getChallenges();
    }

    @Override
    public int getEntryView() {
        return R.layout.chooser_entry;
    }

    @Override
    public boolean isEntryEnabled(ChallengeItem entry) {
        return entry.isEnabled();
    }

    @Override
    public void onEntryChanged(ChallengeItem entry,boolean enabled) {
        if(entry.isEnabled()!=enabled) {
            entry.setEnabled(enabled);
            store.store(storeFile);
            AuthSet old=currentState.getAuth();
            currentState=getStartComponentFromPreferences(getPreferences());
            if(!old.equals(currentState.getAuth())) {
            	agent.grantTrust(getAuth());
            }
        }
    }
	public AuthSet getAuth() {
		// TODO Auto-generated method stub
		return currentState.getAuth().enable(externalAuths);
	}
}
