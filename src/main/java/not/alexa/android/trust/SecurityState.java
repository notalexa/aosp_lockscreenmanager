package not.alexa.android.trust;

import java.security.SecureRandom;
import java.util.Random;

import android.content.Intent;
import android.service.trust.AuthSet;
import android.service.trust.AuthType;

public class SecurityState {
private static Random random=new SecureRandom();
	private AuthSet auth;
    private ChallengeStore store;
	public SecurityState(AuthSet auth, ChallengeStore store) {
		this.auth=auth;
        this.store=store;
        if(store.isEmpty()) {
            this.auth=auth.enable(AuthType.Challenge);
//        } else {
//        	// Hack: Remove this later
//        	this.auth=auth.enable(AuthType.Biometric);
        }
	}

	public AuthSet getAuth() {
		return auth;
	}
	
	public Intent getLockScreen() {
		return store.isEmpty()?null:store.selectRandom(random);
	}
	
	public String toString() {
		return "State["+auth+"]";
	}
}
