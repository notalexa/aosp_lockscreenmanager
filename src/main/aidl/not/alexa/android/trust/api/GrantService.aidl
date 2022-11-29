package not.alexa.android.trust.api;

import android.service.trust.AuthSet;

interface GrantService {
	boolean grant(in AuthSet set);
	AuthSet getCurrentSecurityState();
}