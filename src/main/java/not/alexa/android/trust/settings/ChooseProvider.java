package not.alexa.android.trust.settings;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;

public interface ChooseProvider<T extends ChooseProvider.Entry> {

	public List<T> getEntries();
	public boolean isEntryEnabled(T entry);
	public void onEntryChanged(T entry,boolean enabled);
	public int getEntryView();
	
	public interface Entry {
		CharSequence getLabel(Context context);
		Drawable getLogo(Context context);
	}
}
