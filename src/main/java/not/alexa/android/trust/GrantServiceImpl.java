package not.alexa.android.trust;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.service.trust.AuthSet;
import not.alexa.android.trust.api.GrantService;

/**
 * @author notalexa
 *
 */
public class GrantServiceImpl extends Service {
	private IBinder service;
	public GrantServiceImpl() {
		service=new GrantService.Stub() {
			@Override
			public boolean grant(AuthSet set) throws RemoteException {
				UnlockApp app=UnlockApp.getInstance();
				return app==null?false:app.sendGrantTrust(AuthSet.NONE);
			}

			@Override
			public AuthSet getCurrentSecurityState() throws RemoteException {
				UnlockApp app=UnlockApp.getInstance();
				return app==null?AuthSet.NONE:app.getCurrentSecurityState().getAuth();
			}
		};
	}

	@Override
	public IBinder onBind(Intent intent) {
		return service;
	}
}
