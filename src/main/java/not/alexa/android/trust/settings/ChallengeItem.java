package not.alexa.android.trust.settings;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class ChallengeItem implements ChooseProvider.Entry, Parcelable {
    public static final Creator<ChallengeItem> CREATOR = new Creator<ChallengeItem>() {
        @Override
        public ChallengeItem createFromParcel(Parcel in) {
            return new ChallengeItem(in);
        }

        @Override
        public ChallengeItem[] newArray(int size) {
            return new ChallengeItem[size];
        }
    };

    protected ServiceInfo info;
    protected boolean enabled;
    protected int count;
    protected int weight;

    /* Not parceled */
    protected Intent intent;

    public ChallengeItem(ServiceInfo info,boolean enabled) {
        this.info=info;
        count=1;
        weight=info.metaData!=null?info.metaData.getInt("challenges",1):1;
        System.out.println(info+"["+info.metaData+"]: weight is "+weight);
        this.enabled=enabled&&weight>0;
    }

    protected ChallengeItem(Parcel in) {
        info=in.readParcelable(ServiceInfo.class.getClassLoader());
        enabled=in.readBoolean();
        count=in.readInt();
        weight=in.readInt();
    }

    @Override
    public CharSequence getLabel(Context context) {
        return info.loadLabel(context.getPackageManager());
    }

    @Override
    public Drawable getLogo(Context context) {
        return info.loadIcon(context.getPackageManager());
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(info,0);
        dest.writeBoolean(enabled);
        dest.writeInt(count);
        dest.writeInt(weight);
    }

    public ComponentName getComponentName() {
        return info.getComponentName();
    }
    
    public int getWeight() {
    	return weight;
    }

    public void setEnabled(boolean enabled) {
        this.enabled=enabled;
    }

    public Intent getIntent() {
        if(intent==null) {
            Intent i=new Intent();
            i.setComponent(getComponentName());
            intent=i;
        }
        return intent;
    }
}
