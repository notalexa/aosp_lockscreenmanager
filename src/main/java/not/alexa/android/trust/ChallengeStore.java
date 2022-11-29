package not.alexa.android.trust;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.Manifest.permission;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Parcel;
import android.os.Parcelable;
import not.alexa.android.trust.settings.ChallengeItem;

public class ChallengeStore implements Parcelable {
    private List<ChallengeItem> challenges=new ArrayList<>();
    private List<ChallengeItem> enabled;
    private int challengeCount;

    protected ChallengeStore(Parcel in) {
        in.readTypedList(challenges,ChallengeItem.CREATOR);
    }

    public ChallengeStore() {
    }
    
    public boolean isEmpty() {
    	return getEnabledChallenges().isEmpty();
    }

    public void store(File prefFile) {
        Parcel parcel= Parcel.obtain();
        parcel.writeParcelable(this,0);
        try(OutputStream out=new FileOutputStream(prefFile)) {
            out.write(parcel.marshall());
        } catch(Throwable t) {
            t.printStackTrace();
        } finally {
            parcel.recycle();
        }
        enabled=null;
    }

    public static ChallengeStore load(File dataFile) {
        if(dataFile.exists()) {
            Parcel parcel=Parcel.obtain();
            if(dataFile.exists()) try {
                byte[] dataBytes= Files.readAllBytes(dataFile.toPath());
                parcel.unmarshall(dataBytes,0,dataBytes.length);
                parcel.setDataPosition(0);
                return parcel.readParcelable(ChallengeStore.class.getClassLoader());
            } catch(Throwable t) {
                t.printStackTrace();
            } finally {
                parcel.recycle();
            }
        }
        return new ChallengeStore();
    }

    public static final Creator<ChallengeStore> CREATOR = new Creator<ChallengeStore>() {
        @Override
        public ChallengeStore createFromParcel(Parcel in) {
            return new ChallengeStore(in);
        }

        @Override
        public ChallengeStore[] newArray(int size) {
            return new ChallengeStore[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(challenges);
    }

    public boolean update(Context context) {
        Map<ComponentName,ChallengeItem> knownItems=new HashMap<>();
        challenges.forEach((challenge) -> {
            knownItems.put(challenge.getComponentName(),challenge);
        });
        PackageManager pm=context.getPackageManager();
        List<ChallengeItem> result=new ArrayList<>();
        boolean changed=false;
        for(ResolveInfo info:pm.queryIntentServices(getSearchIntent(),PackageManager.GET_META_DATA )) {
            ChallengeItem item=knownItems.remove(info.getComponentInfo().getComponentName());
            if(pm.checkPermission(permission.KEYGUARD_CHALLENGE_WINDOW, info.getComponentInfo().getComponentName().getPackageName())==PackageManager.PERMISSION_GRANTED) {
                changed|=item==null;
                result.add(item==null||item.isEnabled()?new ChallengeItem(info.serviceInfo,item==null?false:item.isEnabled()):item);
            } else {
                changed|=item!=null;
            }
        }
        challenges=result;
        changed|=knownItems.size()>0;
        if(changed) {
        	enabled=null;
        }
        return changed;
    }

    public Intent getSearchIntent() {
        Intent intent=new Intent("android.service.trust.ChallengeService");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        return intent;
    }

    public List<ChallengeItem> getChallenges() {
        return challenges;
    }

    public List<ChallengeItem> getEnabledChallenges() {
        if(enabled==null) {
            synchronized (this) {
                if (enabled == null) {
                    List<ChallengeItem> result = new ArrayList<>();
                    challengeCount=0;
                    for (ChallengeItem item : challenges) {
                        if (item.isEnabled()) {
                            result.add(item);
                            challengeCount+=item.getWeight();
                        }
                    }
                    enabled = result;
                }
            }
        }
        return enabled;
    }

    public Intent selectRandom(Random random) {
        List<ChallengeItem> items=getEnabledChallenges();
        if(items.size()==0||challengeCount<=0) {
            return null;
        }
        int index=random.nextInt(challengeCount);
        int current=0;
        for(ChallengeItem item:items) {
        	current+=item.getWeight();
        	if(index<current) {
        		Intent intent=item.getIntent();
        		return intent;
        	}
        }
        ChallengeItem item=items.get(random.nextInt(items.size()));
        return item.getIntent();
    }
}
