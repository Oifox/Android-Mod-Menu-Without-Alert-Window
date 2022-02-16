package il2cpp.typefaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Watermark {
	protected int WIDTH,HEIGHT;
	protected Context context;
	
	protected FrameLayout parentBox;
	private Timer timer = new Timer();
	private TimerTask task;
	
	protected LinearLayout menulayout;
	
	protected TextView waterText;
	
	protected WindowManager wmManager;
	protected WindowManager.LayoutParams wmParams;
	
	public Typeface google(Context context) {
		Typeface font = Typeface.createFromAsset(context.getAssets(), "Font.ttf");
		return font; 
	}
	
	public final void setAss(ImageView image, String src) {
		try {
			InputStream ims = context.getAssets().open(src);
			Drawable d = Drawable.createFromStream(ims, null);
			image.setImageDrawable(d);
		}
		catch(IOException ex) {
			// mind coder
			
		}
	}
	
	public float getBatteryLevel() {
		Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

		// Error checking that probably isn't needed but I added just in case.
		if(level == -1 || scale == -1) {
			return 50.0f;
		}

		return ((float)level / (float)scale) * 100.0f; 
	}
	
	public void updateWater() {
		int batery = (int) getBatteryLevel();
		Date currtime = Calendar.getInstance().getTime();
		String hours = Integer.toString(currtime.getHours());
		String minutes = Integer.toString(currtime.getMinutes());
		String seconds = Integer.toString(currtime.getSeconds());
		if (hours.length() == 1) {
			hours = "0" + hours;
		}
		if (minutes.length() == 1) {
			minutes = "0" + minutes;
		}
		if (seconds.length() == 1) {
			seconds = "0" + seconds;
		}
		String time = String.format("%s:%s:%s", hours, minutes, seconds);
		waterText.setText("INSINE " + Integer.toString(batery) + "% | " + time);
	}
	
	protected void init(Context context) {
		this.context = context;
		
		menulayout = new LinearLayout(context);
		menulayout.setOrientation(LinearLayout.VERTICAL);
		
		waterText = new TextView(context);
		waterText.setGravity(Gravity.CENTER);
		waterText.setTextSize(15f);
		waterText.setTextColor(Color.WHITE);
		waterText.setTypeface(google(context));
		
		waterText.setText("HAHAHA, INSINE");
		final Context ctx = context;
		final Handler handler = new Handler();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				updateWater();
				handler.postDelayed(this, 1000);
			}
		};
	
		handler.postDelayed(runnable, 1000);
		
		GradientDrawable grad = new GradientDrawable();
		grad.setColor(Color.parseColor("#22222A"));
		grad.setCornerRadius(10f); // grad.setCornerRadii(new float[] {10, 10, 10, 10, 0, 0, 0, 0});
		menulayout.setBackgroundDrawable(grad);
		
		LinearLayout line = new LinearLayout(context);
		GradientDrawable grad2 = new GradientDrawable();
		grad2.setColor(Color.parseColor("#867DEA"));
		grad2.setCornerRadii(new float[] {10, 10, 10, 10, 0, 0, 0, 0});
		line.setBackgroundDrawable(grad2);
		
		menulayout.addView(line, -1, dpi(5));
		menulayout.addView(waterText, -1, -1);
		
		parentBox = new FrameLayout(context);
		
		int width = Resources.getSystem().getDisplayMetrics().widthPixels;
		int height = Resources.getSystem().getDisplayMetrics().heightPixels;
		wmManager = ((Activity)context).getWindowManager();
		int aditionalFlags=0;
		if (Build.VERSION.SDK_INT >= 11)
			aditionalFlags = WindowManager.LayoutParams.FLAG_SPLIT_TOUCH;
		if (Build.VERSION.SDK_INT >=  3)
			aditionalFlags = aditionalFlags | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
		wmParams = new WindowManager.LayoutParams(
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.WRAP_CONTENT,
			70,//initialX
			50,//initialy
			WindowManager.LayoutParams.TYPE_APPLICATION,
			WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
			WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN |
			WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
			WindowManager.LayoutParams.FLAG_FULLSCREEN |
			aditionalFlags,
			PixelFormat.TRANSPARENT
		);
		wmParams.gravity = Gravity.TOP | Gravity.LEFT;
	}

	public void setIconImage() {
		//iconView = new ImageView(context);
		//setAss(iconView, "icon.png");
	}
	
	public void isVisible(boolean iss) {
		if (iss) {
			menulayout.setVisibility(View.VISIBLE);
		} else {
			menulayout.setVisibility(View.GONE);
		}
	}
	
	public void setWidth(int px) {
		FrameLayout.LayoutParams lp=(FrameLayout.LayoutParams)menulayout.getLayoutParams();
		lp.width = px;
		menulayout.setLayoutParams(lp);
		WIDTH=px;
	}
	
	public void setHeight(int px) {
		FrameLayout.LayoutParams lp=(FrameLayout.LayoutParams)menulayout.getLayoutParams();
		lp.height = px;
		menulayout.setLayoutParams(lp);
		HEIGHT=px;
	}
	
	public int getWidth(int px) {return WIDTH;}
	public int getHeight(int px) {return HEIGHT;}

	public int dpi(float dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
	
	
	public Watermark(Context context) {
		init(context);
		
		parentBox.removeAllViews();
		parentBox.addView(menulayout, WIDTH, HEIGHT);
		
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
