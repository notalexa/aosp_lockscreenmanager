package not.alexa.android.trust.settings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class ChallengeToolbar extends Toolbar {
    public ChallengeToolbar(Context context) {
        super(context);
    }

    public ChallengeToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChallengeToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(new Tiles(background) {
            @Override
            public void draw(@NonNull Canvas canvas) {
                setHeight(ChallengeToolbar.this.getHeight());
                super.draw(canvas);
            }
        });
    }
}
