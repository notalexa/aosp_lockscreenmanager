package not.alexa.android.trust.settings;

import android.annotation.NonNull;
import android.annotation.Nullable;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class Tiles extends Drawable {

    private Paint paint;
    private int layoutHeight=-1;
    private Drawable drawable;

    public Tiles(Drawable drawable) {
        this.drawable=drawable;
    }

    protected void setHeight(int height) {
        if((height!=this.layoutHeight&&height>0)||paint==null) {
            paint = new Paint();
            paint.setShader(new BitmapShader(getBitmap(height), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if(paint!=null) {
            canvas.drawPaint(paint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    private Bitmap getBitmap(int height) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int h=drawable.getIntrinsicHeight();
        int w=drawable.getIntrinsicWidth();
        if(height!=h) {
            w=(w*height)/h;
            h=height;
        }
        Bitmap bitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        layoutHeight=h;
        return bitmap;
    }
}
