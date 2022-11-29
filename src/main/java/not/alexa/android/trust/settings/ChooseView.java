package not.alexa.android.trust.settings;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import not.alexa.android.trust.R;
import not.alexa.android.trust.settings.ChooseProvider.Entry;

public class ChooseView<T extends Entry> extends ListView {

	public ChooseView(Context context) {
		super(context);
	}

	public ChooseView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ChooseView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setProvider(ChooseProvider<T> provider) {
		Adapter<T> adapter=new Adapter<T>(getContext(),provider);
		setAdapter(adapter);
	}
	
	private class Adapter<E extends ChooseProvider.Entry> extends ArrayAdapter<E> /*implements ListView.OnItemClickListener*/ {
    	private ChooseProvider<E> provider;
    	private LayoutInflater inflater=LayoutInflater.from(getContext());
		public Adapter(Context context, ChooseProvider<E> provider) {
			super(context, -1, provider.getEntries());
			this.provider=provider;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view=convertView==null?inflater.inflate(provider.getEntryView(),parent,false):convertView;
			final E entry=getItem(position);
			CharSequence label=entry.getLabel(getContext());
			((TextView)view.findViewById(R.id.label)).setText(label==null?"":label);
			Drawable logo=entry.getLogo(getContext());
			View logoView=view.findViewById(R.id.logo);
			if(logo!=null) {
				((ImageView)logoView).setImageDrawable(logo);
				logoView.setVisibility(View.VISIBLE);
			} else {
				logoView.setVisibility(View.INVISIBLE);
			}
			Checkable enabledView=view.findViewById(R.id.enabled);
			boolean checked=provider.isEntryEnabled(entry);
			((View)enabledView).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					provider.onEntryChanged(entry,enabledView.isChecked());
					notifyDataSetChanged();
				}
			});
			((Checkable)enabledView).setChecked(checked);
			return view;
		}
    }
}
