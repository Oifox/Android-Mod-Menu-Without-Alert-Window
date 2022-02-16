package il2cpp.typefaces;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;

public class Crosshair {
	protected int WIDTH,HEIGHT;
	protected Context context;
	protected FrameLayout parentBox;
	public int greenColor = 255;
	public int redColor = 0;
	public int blueColor = 0;
	protected ImageView crosshair;
	protected WindowManager wmManager;
	protected WindowManager.LayoutParams wmParams;
	
	public final void setAss(ImageView image, String src) {
		try {
			InputStream ims = context.getAssets().open(src);
			Drawable d = Drawable.createFromStream(ims, null);
			image.setImageDrawable(d);
		}
		catch(IOException ex) {

		}
	}
	
	protected void init(Context context) {
		this.context = context;
		
		crosshair = new ImageView(context);
		setAss(crosshair, "ch1.png");
		
		parentBox = new FrameLayout(context);
		
		wmManager = ((Activity)context).getWindowManager();
		int aditionalFlags=0;
		if (Build.VERSION.SDK_INT >= 11)
			aditionalFlags = WindowManager.LayoutParams.FLAG_SPLIT_TOUCH;
		if (Build.VERSION.SDK_INT >=  3)
			aditionalFlags = aditionalFlags | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
		wmParams = new WindowManager.LayoutParams(
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.WRAP_CONTENT,
			0,//initialX
			0,//initialy
			WindowManager.LayoutParams.TYPE_APPLICATION,
			WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
			WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN |
			WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
			WindowManager.LayoutParams.FLAG_FULLSCREEN |
			aditionalFlags,
			PixelFormat.TRANSPARENT
		);
		wmParams.gravity = Gravity.CENTER;
	}

	public void setIconImage() {
		//iconView = new ImageView(context);
		//setAss(iconView, "icon.png");
	}
	
	public void setCross(String name) {
		setAss(crosshair, name);
	}
	
	public void setSize(float size) {
		crosshair.setScaleX(size);
		crosshair.setScaleY(size);
	}
	
	public void setCR(int red) {
		redColor = red;
	}
	
	public void setCG(int green) {
		greenColor = green;
	}
	
	public void setCB(int blue) {
		blueColor = blue;
	}
	
	public void updateColor() {
		crosshair.setColorFilter(Color.rgb(redColor, greenColor, blueColor));
	}
	
	public void isVisible(boolean iss) {
		if (iss) {
			crosshair.setVisibility(View.VISIBLE);
		} else {
			crosshair.setVisibility(View.GONE);
		}
	}
	
	public void setWidth(int px) {
		FrameLayout.LayoutParams lp=(FrameLayout.LayoutParams)crosshair.getLayoutParams();
		lp.width = px;
		crosshair.setLayoutParams(lp);
		WIDTH=px;
	}
	
	public void setHeight(int px) {
		FrameLayout.LayoutParams lp=(FrameLayout.LayoutParams)crosshair.getLayoutParams();
		lp.height = px;
		crosshair.setLayoutParams(lp);
		HEIGHT=px;
	}
	
	public int getWidth(int px) {return WIDTH;}
	public int getHeight(int px) {return HEIGHT;}
	
	public int dpi(float dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
	
	
	public Crosshair(Context context) {
		init(context);
		
		parentBox.removeAllViews();
		parentBox.addView(crosshair, WIDTH, HEIGHT);
		
		wmManager.addView(parentBox, wmParams);
	}

	View.OnTouchListener handleMotionTouch = new View.OnTouchListener() {
		private float initX;          
		private float initY;
		private float touchX;
		private float touchY;

		double clock=0;
		@Override
		public boolean onTouch(View vw, MotionEvent ev) {
			switch (ev.getAction()) {
				case MotionEvent.ACTION_DOWN:

					initX = wmParams.x;
					initY = wmParams.y;
					touchX = ev.getRawX();
					touchY = ev.getRawY();
					clock = System.currentTimeMillis();
					break;

				case MotionEvent.ACTION_MOVE:
					wmParams.x = (int)initX + (int)(ev.getRawX() - touchX);

					wmParams.y = (int)initY + (int)(ev.getRawY() - touchY);


					wmManager.updateViewLayout(vw, wmParams);
					break;
			}
			return true;
		}
	};
	
	private int convertDipToPixels(int i) {
            return (int) ((((float) i) * context.getResources().getDisplayMetrics().density) + 0.5f);
        }
}
