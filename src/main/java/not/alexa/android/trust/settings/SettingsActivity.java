package not.alexa.android.trust.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import not.alexa.android.trust.R;
import not.alexa.android.trust.UnlockApp;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ChooseView<ChallengeItem> list= findViewById(R.id.chooser_list);
        list.setProvider((UnlockApp)getApplication());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_advanced_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.itemGrants) {
            startActivity(new Intent(this, AdvancedSettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
