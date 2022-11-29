package not.alexa.android.trust.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Checkable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import not.alexa.android.trust.R;
import not.alexa.android.trust.UnlockApp;

public class AdvancedSettingsActivity extends AppCompatActivity {
	SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs=((UnlockApp)getApplication()).getPreferences();
        setContentView(R.layout.advanced_settings_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        register(findViewById(R.id.optionAllowEscrow),"allow.escrowtoken");
        register(findViewById(R.id.optionGrantSecret),"lockscreen.grant.secret");
        register(findViewById(R.id.optionGrantBiometric),"lockscreen.grant.inherent");
        register(findViewById(R.id.optionGrantLocation),"lockscreen.grant.location");
        register(findViewById(R.id.optionGrantPossession),"lockscreen.grant.possession");
    }
    
    private void register(Checkable grant,String prop) {
    	grant.setChecked(prefs.getBoolean(prop,false));
        ((View)grant).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				prefs.edit().putBoolean(prop,grant.isChecked()).commit();
			}
		});    	
    }
}
