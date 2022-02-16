package il2cpp.typefaces;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import il2cpp.Main;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Menu
{
	protected int WIDTH,HEIGHT;
    
    public Typeface getfont(Context context) {Typeface bruuh = Typeface.createFromAsset(context.getAssets(), "font.ttf"); return bruuh;}
	
	protected Context context;
	protected boolean isIconVisible;
	protected boolean isMenuVisible;
	protected ImageView iconView, logoView, closeView, settingsView;
	protected FrameLayout parentBox;
	protected LinearLayout menulayout, leftLayout, rightLayout, headerLayout, pageLayout, pagesLayout;
	protected ScrollView scrollItems;
	protected ScrollView scrollPages;    
	protected LinearLayout pages, pagess;
	protected ArrayList<LinearLayout> pagesList = new ArrayList<LinearLayout>();
	protected ArrayList<LinearLayout> pageList = new ArrayList<LinearLayout>();
	
	protected TextView title;
	
	protected int isMenuHide, isWatermarkHide, isCrosshairHide;

	protected WindowManager wmManager;
	protected WindowManager.LayoutParams wmParams;
	
	protected LinearLayout childOfScroll;
	protected LinearLayout childOfScrollPages;
    
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
	
	/* CALLBACKS */
	public static interface callbackSwitch {
		void onClickTap(int pageid, int feature, int checked);
	}
	
	public static interface callbackCheck {
		void onClickTap(int pageid, int feature, int checked);
	}
	
	public static interface callbackButton {
		void onClickTap(int pageid, int feature);
	}
	
	public static native String resultToken();
	
	public static interface callbackSlider {
		void onChange(int pageid, int featureid, int value);
	}
	
	public static interface callbackText {
		void onText(int pageid, int featureid, String text);
	}
	
	public static interface callbackNum {
		void onText(int pageid, int featureid, int value);
	}
	
	protected void init(Context context)
	{
		isCrosshairHide = 1;
		isWatermarkHide = 1;
		isMenuHide = 0;
		this.context = context;
		isIconVisible = false;
		isMenuVisible = false;
		iconView = new ImageView(context);
		
		parentBox = new FrameLayout(context);
		
		parentBox.setOnTouchListener(handleMotionTouch);
		wmManager = ((Activity)context).getWindowManager();
		int aditionalFlags=0;
		if (Build.VERSION.SDK_INT >= 11)
			aditionalFlags = WindowManager.LayoutParams.FLAG_SPLIT_TOUCH;
		if (Build.VERSION.SDK_INT >=  3)
			aditionalFlags = aditionalFlags | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
		wmParams = new WindowManager.LayoutParams(
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.WRAP_CONTENT,
			30,//initialX
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

	public void setIconImage()
	{
		iconView = new ImageView(context);
		setAss(iconView, "icon.png");
	}
	public void setWidth(int px)
	{
		FrameLayout.LayoutParams lp=(FrameLayout.LayoutParams)menulayout.getLayoutParams();
		lp.width = px;
		menulayout.setLayoutParams(lp);
		WIDTH=px;
	}
	public void setHeight(int px)
	{
		FrameLayout.LayoutParams lp=(FrameLayout.LayoutParams)menulayout.getLayoutParams();
		lp.height = px;
		menulayout.setLayoutParams(lp);
		HEIGHT=px;
	}
	public int getWidth(int px) {return WIDTH;}
	public int getHeight(int px) {return HEIGHT;}

	public void showIcon() {
		ObjectAnimator animation = ObjectAnimator.ofFloat(iconView, "alpha", 0, 1.0f);
		animation.setDuration(550);
		animation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {

				}

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
		animation.start();
		if (Main.hide) {
			iconView.setVisibility(View.INVISIBLE);
		} else if (!Main.hide) {
			iconView.setVisibility(View.VISIBLE);
		}
		if (isMenuHide == 1) {
			iconView.setVisibility(View.INVISIBLE);
		}
		if (!isIconVisible)
		{
			isMenuVisible = false;
			parentBox.removeAllViews();
			parentBox.addView(iconView, dpi(70),dpi(70));
			isIconVisible = true;
		}
	}

	public void showMenu() {
		ObjectAnimator animation = ObjectAnimator.ofFloat(menulayout, "alpha", 0, 1.0f);
		animation.setDuration(500);
		animation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
               
				}

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
		animation.start();
		if (!isMenuVisible)
		{
			isIconVisible = false;
			parentBox.removeAllViews();
			parentBox.addView(menulayout, WIDTH, HEIGHT);
			isMenuVisible = true;
		}
	}
	
	public void addCallback() {
		LinearLayout linep = new LinearLayout(context);
		linep.setLayoutParams(new LinearLayout.LayoutParams(-1, dpi(35)));
		linep.setPadding(0, 5, 0, 5);
		TextView textView = new TextView(context);
		textView.setText("Click");
		textView.setTextSize(12f);
		textView.setTextColor(Color.WHITE);
		textView.setGravity(Gravity.CENTER);
		textView.setTypeface(google(context));

		GradientDrawable grad = new GradientDrawable();
		grad.setColor(Color.parseColor("#211C20"));
		grad.setCornerRadius(10f);
		textView.setPadding(0, 0, 0, 0);
		final TextView text = textView;
		textView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String token = resultToken();
				text.setText(token);
				ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE); 
				ClipData clip = ClipData.newPlainText(token, token);
				clipboard.setPrimaryClip(clip);
			}
		});

		textView.setBackgroundDrawable(grad);
		linep.addView(textView, -1, -1);
		pageList.get(pageList.size()-1).addView(linep, -1, -2);
		
	}
	
	public int dpi(float dp)
	{
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
	
	public void goUrl(String url) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		context.startActivity(i);
	}
	
	public void addLinkButton(int pageid, String text, String link) {
		LinearLayout linep = new LinearLayout(context);
		linep.setLayoutParams(new LinearLayout.LayoutParams(-1, dpi(20)));
		Button goTelegram = new Button(context);
		linep.setPadding(0, 10, 0, 10);
		goTelegram.setText(text);
		goTelegram.setTextSize(15f);
		goTelegram.setTextColor(Color.WHITE);
		goTelegram.setTypeface(google(context));

		GradientDrawable grad = new GradientDrawable();
		grad.setColor(Color.parseColor("#867DEA"));
		grad.setCornerRadius(10f);
		goTelegram.setBackgroundDrawable(grad);
		goTelegram.setPadding(0,0,0,0);
		linep.addView(goTelegram, -1, -1);
		final String link2 = link;
		goTelegram.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goUrl(link2);
			}
		});
		pageList.get(pageid).addView(linep, -1, dpi(30));
	}
	
	public void addCheckbox(int pageid, int featureid, String text, final callbackCheck callback) {
		LinearLayout linep = new LinearLayout(context);
		linep.setOrientation(LinearLayout.HORIZONTAL);
		linep.setLayoutParams(new LinearLayout.LayoutParams(-1, dpi(30)));
		LinearLayout textLayout = new LinearLayout(context);
		LinearLayout buttonLayout = new LinearLayout(context);
		
		TextView featureText = new TextView(context);
		ImageView featureButton = new ImageView(context);
		
		textLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -1, 1));
		buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -1));
		buttonLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		
		featureText.setText(text);
		featureText.setTextSize(15f);
		featureText.setTextColor(Color.WHITE);
		featureText.setGravity(Gravity.CENTER_VERTICAL);
		featureText.setTypeface(google(context));
		textLayout.addView(featureText, -2, -1);
		
		featureButton.setLayoutParams(new LinearLayout.LayoutParams(dpi(30), dpi(30)));
		featureButton.setPadding(10, 10, 10, 10);
		setAss(featureButton, "uncheck.png");
		buttonLayout.addView(featureButton);
		
		final int pageid2 = pageid, featureid2 = featureid;
		final ImageView button = featureButton;
		featureButton.setOnClickListener(new OnClickListener() {
			private boolean isChekced = false;
			@Override
			public void onClick(View v) {
				isChekced = !isChekced;
				callback.onClickTap(pageid2, featureid2, isChekced ? 1 : 0);
				if (isChekced) {
					setAss(button, "check.png");
				} else {
					setAss(button, "uncheck.png");
				}
			}
		});
		
		linep.addView(textLayout);
		linep.addView(buttonLayout);
		
		pageList.get(pageid).addView(linep, -1, dpi(30));
	}
	
	public void addSwitch(int pageid, int featureid, String text, final callbackSwitch callback) {
		LinearLayout linep = new LinearLayout(context);
		linep.setOrientation(LinearLayout.HORIZONTAL);
		linep.setLayoutParams(new LinearLayout.LayoutParams(-1, dpi(30)));
		LinearLayout textLayout = new LinearLayout(context);
		LinearLayout buttonLayout = new LinearLayout(context);

		TextView featureText = new TextView(context);
		ImageView featureButton = new ImageView(context);

		textLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -1, 1));
		buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -1));
		buttonLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);

		featureText.setText(text);
		featureText.setTextSize(15f);
		featureText.setTextColor(Color.WHITE);
		featureText.setGravity(Gravity.CENTER_VERTICAL);
		featureText.setTypeface(google(context));
		textLayout.addView(featureText, -2, -1);

		featureButton.setLayoutParams(new LinearLayout.LayoutParams(dpi(40), dpi(30)));
		featureButton.setPadding(0, 0, 0, 0);
		setAss(featureButton, "uncheck2.png");
		buttonLayout.addView(featureButton);

		final int pageid2 = pageid, featureid2 = featureid;
		final ImageView button = featureButton;
		featureButton.setOnClickListener(new OnClickListener() {
				private boolean isChekced = false;
				@Override
				public void onClick(View v) {
					isChekced = !isChekced;
					callback.onClickTap(pageid2, featureid2, isChekced ? 1 : 0);
					if (isChekced) {
						setAss(button, "check2.png");
					} else {
						setAss(button, "uncheck2.png");
					}
				}
			});

		linep.addView(textLayout);
		linep.addView(buttonLayout);

		pageList.get(pageid).addView(linep, -1, dpi(30));
	}
	
	public void addSlider(int pageid, int featureid, String text, int min, int max, int progress, final callbackSlider callback) {
		LinearLayout linep = new LinearLayout(context);
		linep.setOrientation(LinearLayout.HORIZONTAL);
		linep.setLayoutParams(new LinearLayout.LayoutParams(-1, dpi(30)));
		LinearLayout textLayout = new LinearLayout(context);
		LinearLayout buttonLayout = new LinearLayout(context);

		TextView featureText = new TextView(context);
		
		textLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -1, 1));
		buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(dpi(130), -1));
		buttonLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		
		featureText.setText(text + ": " + Integer.toString(progress));
		featureText.setTextSize(15f);
		featureText.setTextColor(Color.WHITE);
		featureText.setGravity(Gravity.CENTER_VERTICAL);
		featureText.setTypeface(google(context));
		textLayout.addView(featureText, -2, -1);

		SeekBar slider = new SeekBar(context);
		slider.getProgressDrawable().setColorFilter(Color.parseColor("#867DEA"), PorterDuff.Mode.MULTIPLY);
        slider.getThumb().setColorFilter(Color.parseColor("#867DEA"), PorterDuff.Mode.MULTIPLY);
		slider.setPadding(25, 10, 35, 10);
		slider.setMin(min);
		slider.setMax(max);
		slider.setProgress(progress);
		
		buttonLayout.addView(slider, -1, -1);
		
		final int pageid2 = pageid, featureid2 = featureid;
		final TextView textview = featureText;
		final String name = text;
		slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onStartTrackingTouch(SeekBar seekBar) {}
			public void onStopTrackingTouch(SeekBar seekBar) {}
			
			public void onProgressChanged(SeekBar seekBar, int value, boolean isUser) {
				textview.setText(name + ": " + Integer.toString(value));
				callback.onChange(pageid2, featureid2, value);
			}
		});
		
		linep.addView(textLayout);
		linep.addView(buttonLayout);

		pageList.get(pageid).addView(linep, -1, dpi(30));
	}

	public void addButton(int pageid, int featureid, String text, final callbackButton callback) {
		LinearLayout linep = new LinearLayout(context);
		linep.setOrientation(LinearLayout.HORIZONTAL);
		linep.setLayoutParams(new LinearLayout.LayoutParams(-1, dpi(30)));
		LinearLayout textLayout = new LinearLayout(context);
		LinearLayout buttonLayout = new LinearLayout(context);

		TextView featureText = new TextView(context);
		ImageView featureButton = new ImageView(context);

		textLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -1, 1));
		buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -1));
		buttonLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);

		featureText.setText(text);
		featureText.setTextSize(15f);
		featureText.setTextColor(Color.WHITE);
		featureText.setGravity(Gravity.CENTER_VERTICAL);
		featureText.setTypeface(google(context));
		textLayout.addView(featureText, -2, -1);

		featureButton.setLayoutParams(new LinearLayout.LayoutParams(dpi(40), dpi(30)));
		featureButton.setPadding(0, 0, 0, 0);
		setAss(featureButton, "tap1.png");
		buttonLayout.addView(featureButton);

		final int pageid2 = pageid, featureid2 = featureid;
		final ImageView button = featureButton;
		featureButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					callback.onClickTap(pageid2, featureid2);
					setAss(button, "tap2.png");
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							setAss(button, "tap1.png");
						}
					}, 200);
				}
			});

		linep.addView(textLayout);
		linep.addView(buttonLayout);

		pageList.get(pageid).addView(linep, -1, dpi(30));
	}
	
	public void setScale(float scale) {
		menulayout.setScaleX(scale);
		menulayout.setScaleY(scale);
	}
	
	public void setAlpha(float val) {
		menulayout.setAlpha(val);
	}
	
	public void addInputStr(int pageid, int featureid, String text, final callbackText callback) {
		LinearLayout linep = new LinearLayout(context);
		linep.setOrientation(LinearLayout.HORIZONTAL);
		linep.setLayoutParams(new LinearLayout.LayoutParams(-1, dpi(30)));
		LinearLayout textLayout = new LinearLayout(context);
		LinearLayout buttonLayout = new LinearLayout(context);

		TextView featureText = new TextView(context);
		ImageView featureButton = new ImageView(context);

		try {
		
		textLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -1, 1));
		buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -1));
		buttonLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);

		featureText.setText(text + ": NULL");
		featureText.setTextSize(15f);
		featureText.setTextColor(Color.WHITE);
		featureText.setGravity(Gravity.CENTER_VERTICAL);
		featureText.setTypeface(google(context));
		textLayout.addView(featureText, -2, -1);

		featureButton.setLayoutParams(new LinearLayout.LayoutParams(dpi(40), dpi(30)));
		featureButton.setPadding(0, 0, 0, 0);
		setAss(featureButton, "text.png");
		buttonLayout.addView(featureButton);

		final String nameFeat = text;
		final int pageid2 = pageid, featureid2 = featureid;
		final ImageView button = featureButton;
		final TextView textt = featureText;
		featureButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					final AlertDialog.Builder dialog2 = new AlertDialog.Builder(context);
					//dialog2.setTitle(nameFeat);
					//dialog2.setMessage("Введите текст");
					final EditText myedittext2 = new EditText(context);

					myedittext2.setTextSize(15f);
					myedittext2.setTextColor(Color.WHITE);
					myedittext2.setHintTextColor(Color.WHITE);
					myedittext2.setTypeface(google(context));

					LinearLayout.LayoutParams myp2 = new LinearLayout.LayoutParams(-1, -2);

					LinearLayout line = new LinearLayout(context);
					line.setOrientation(LinearLayout.VERTICAL);
					TextView textView = new TextView(context);

					textView.setText(nameFeat);
					textView.setTextSize(15f);
					textView.setTextColor(Color.WHITE);
					textView.setGravity(Gravity.CENTER);
					textView.setTypeface(google(context));
					line.addView(textView, -1, -2);

					// BUTT
					LinearLayout linep2 = new LinearLayout(context);
					linep2.setLayoutParams(new LinearLayout.LayoutParams(-1, dpi(20)));
					Button okButt = new Button(context);
					linep2.setPadding(20, 20, 20, 20);
					okButt.setText("OKAY");
					okButt.setTextSize(15f);
					okButt.setTextColor(Color.WHITE);
					okButt.setTypeface(google(context));



					GradientDrawable grad = new GradientDrawable();
					grad.setColor(Color.parseColor("#867DEA"));
					grad.setCornerRadius(10f);
					okButt.setBackgroundDrawable(grad);
					okButt.setPadding(0,0,0,0);
					linep2.addView(okButt, -1, -1);

					
					line.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
					line.setBackgroundColor(Color.parseColor("#211C20"));
					myedittext2.setLines(1);

					myp2.setMargins(20, 0, 20, 0);
					myedittext2.setLayoutParams(myp2);

					line.addView(myedittext2);
					dialog2.setView(line);
					myedittext2.setHint(nameFeat);
					line.addView(linep2, -1, -2);
					//dialog2.show();
					final Dialog d = dialog2.show();
					okButt.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								String txt = myedittext2.getText().toString();
								if (txt.length() > 15) {
									txt = txt.substring(0, 15);
								}
								textt.setText(nameFeat + ": " + txt);
								callback.onText(pageid2, featureid2, myedittext2.getText().toString());
								d.dismiss();
							}
						});
				}
			});
		linep.addView(textLayout);
		linep.addView(buttonLayout);

		pageList.get(pageid).addView(linep, -1, dpi(30));
		} catch (Exception e) {}
	}
	
	public void addPage(String name, String icon, int pageId) {
		try{
		LinearLayout line = new LinearLayout(context);
		line.setLayoutParams(new LinearLayout.LayoutParams(-1, dpi(30)));
		LinearLayout page = new LinearLayout(context);
		page.setLayoutParams(new LinearLayout.LayoutParams(-1, dpi(25)));
		line.setGravity(Gravity.CENTER);
		line.setPadding(15, 0, 15, 0);
		pagesList.add(page);
		page.setOrientation(LinearLayout.HORIZONTAL);
		
		TextView pageName = new TextView(context);
		ImageView pageIcon = new ImageView(context);
		
		
		pageName.setText(name);
		pageName.setTextSize(12f);
		pageName.setTextColor(Color.WHITE);
		pageName.setTypeface(google(context));
		
		page.addView(pageName, -1, -2);
		page.setGravity(Gravity.CENTER_VERTICAL);
		page.setPadding(10, 0, 0, 0);
		
		GradientDrawable pgrad = new GradientDrawable();
		pgrad.setColor(Color.parseColor("#211C20"));
		page.setBackgroundDrawable(pgrad);
		pgrad.setCornerRadius(10f);
		line.addView(page);
		final LinearLayout pg = page;
		final ArrayList<LinearLayout> pgs = pagesList;
		final String nm = name;
		final int id = pageId;
		page.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (int index = 0; index < pgs.size(); index++) {
					LinearLayout last = pgs.get(index);
					GradientDrawable pgrad = new GradientDrawable();
					pgrad.setColor(Color.parseColor("#211C20"));
					last.setBackgroundDrawable(pgrad);
					pgrad.setCornerRadius(10f);
					title.setText(nm);
				}
				GradientDrawable pgrad = new GradientDrawable();
				pgrad.setColor(Color.parseColor("#867DEA"));
				pg.setBackgroundDrawable(pgrad);
				pgrad.setCornerRadius(10f);
				showPage(id);
			}
		});
		
		leftLayout.addView(line);
		LinearLayout page2 = new LinearLayout(context);
		page2.setOrientation(LinearLayout.VERTICAL);
		pageList.add(page2);
		pagess.addView(page2, -1, -1);
		page2.setVisibility(View.GONE);
		} catch (Exception e) {Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();}
	}

	public void setIsVisible(int a) {
		isMenuHide = a;
	}
	
	public int createSettings() {
		LinearLayout settings = new LinearLayout(context);
		settings.setOrientation(LinearLayout.VERTICAL);
		title.setText("Settings");
		
		pagess.addView(settings, -1, -1);
		pageList.add(settings);
		
		settingsView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "tg: @insineteamdev | Created by: MIND", Toast.LENGTH_LONG).show();
				showPage(pageList.size()-1);
			}
		});
		
		return pageList.size()-1;
	}
	
	public void addCH(final Crosshair ch) {
		LinearLayout line = new LinearLayout(context);
		
		LinearLayout linep = new LinearLayout(context);
		linep.setOrientation(LinearLayout.HORIZONTAL);
		linep.setLayoutParams(new LinearLayout.LayoutParams(-1, dpi(60)));
		GradientDrawable grad = new GradientDrawable();
		grad.setCornerRadius(15f);
		grad.setColor(Color.parseColor("#211C20"));
		linep.setBackgroundDrawable(grad);
		
		ImageView chs[] = {new ImageView(context), new ImageView(context), new ImageView(context), new ImageView(context), new ImageView(context)};
		String names[] = {"ch1.png", "ch2.png", "ch3.png", "ch4.png", "ch5.png"};
		for (int index = 0; index < chs.length; index++) {
			linep.addView(chs[index], new LinearLayout.LayoutParams(-2, -1, 1));
			chs[index].setPadding(15, 15, 15, 15);
			setAss(chs[index], names[index]);
			final String nms[] = names;
			final int idx = index;
			chs[index].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(context, "Прицел изменён!", Toast.LENGTH_SHORT).show();
					ch.setCross(nms[idx]);
				}
			});
		}
		
		line.setPadding(0, 10, 0, 10);
		line.addView(linep, -1, -1);
		pageList.get(pageList.size()-1).addView(line, -1, dpi(60));
	}
	
	public void showPage(int id) {
		for (int i = 0; i < pageList.size(); i++) {
			LinearLayout page = pageList.get(i);
			if (i != id) {
				page.setVisibility(View.GONE);
			} else {
				page.setVisibility(View.VISIBLE);
				ObjectAnimator animation = ObjectAnimator.ofFloat(page, "alpha", 0, 1.0f);
				animation.setDuration(500);
				animation.addListener(new Animator.AnimatorListener() {
						@Override
						public void onAnimationStart(Animator animation) {

						}

						@Override
						public void onAnimationEnd(Animator animation) {

						}

						@Override
						public void onAnimationCancel(Animator animation) {

						}

						@Override
						public void onAnimationRepeat(Animator animation) {

						}
					});
				animation.start();
			}
		}
	}
	
	public void addTextPage(int pageid, int featureid, String text) {
		
		LinearLayout linep = new LinearLayout(context);
		linep.setLayoutParams(new LinearLayout.LayoutParams(-1, dpi(35)));
		linep.setPadding(0, 5, 0, 5);
		TextView textView = new TextView(context);
		textView.setText(text);
		textView.setTextSize(12f);
		textView.setTextColor(Color.WHITE);
		textView.setGravity(Gravity.CENTER);
		textView.setTypeface(google(context));
		
		GradientDrawable grad = new GradientDrawable();
		grad.setColor(Color.parseColor("#211C20"));
		grad.setCornerRadius(10f);
		textView.setPadding(0, 0, 0, 0);
		
		textView.setBackgroundDrawable(grad);
		linep.addView(textView, -1, -1);
		pageList.get(pageid).addView(linep, -1, -2);
		
	}
	
	public Menu(Context context)
	{
		
		init(context);
		
		menulayout = new LinearLayout(context);
		menulayout.setOrientation(LinearLayout.VERTICAL);
		scrollItems = new ScrollView(context);
		scrollItems.setBackgroundColor(Color.TRANSPARENT);
		
		setIconImage();
		try {
			
			//menulayout.addView(scrollItems, -1, -1);
			wmManager.addView(parentBox, wmParams);
			showMenu();
			
			/* Menu layout */
			GradientDrawable grad = new GradientDrawable();
			grad.setCornerRadius(5f);
			grad.setColor(Color.parseColor("#211C20"));
			menulayout.setOrientation(LinearLayout.HORIZONTAL);
			menulayout.setBackgroundDrawable(grad);
			/* Menu layout */
			
			/* Left layout */
			leftLayout = new LinearLayout(context);
			GradientDrawable grad2 = new GradientDrawable();
			grad2.setCornerRadius(5f);
			grad2.setColor(Color.parseColor("#26252A"));
			leftLayout.setBackgroundDrawable(grad2);
			leftLayout.setGravity(Gravity.CENTER_HORIZONTAL);
			leftLayout.setPadding(0, 15, 0, 0);
			leftLayout.setOrientation(LinearLayout.VERTICAL);
			
			menulayout.addView(leftLayout, dpi(100), -1);
			/* Left layout */
			
			// Logo
			logoView = new ImageView(context);
			setAss(logoView, "logo.png");
			logoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			leftLayout.addView(logoView, dpi(90), dpi(90));
			
			/* Right layout */
			rightLayout = new LinearLayout(context);
			rightLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -1, 1));
			rightLayout.setOrientation(LinearLayout.VERTICAL);
			
			menulayout.addView(rightLayout);
			/* Right layout */
			
			/* Right down header */ 
			headerLayout = new LinearLayout(context);
			GradientDrawable grad3 = new GradientDrawable();
			grad3.setCornerRadius(5f);
			grad3.setColor(Color.parseColor("#26252A"));
			
			headerLayout.setBackgroundDrawable(grad3);
			headerLayout.setOrientation(LinearLayout.VERTICAL);
			
			closeView = new ImageView(context);
			settingsView = new ImageView(context);
			
			closeView.setPadding(3,3,3,3);
			settingsView.setPadding(3,3,3,3);
			
			closeView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showIcon();
				}
			});
			
			setAss(closeView, "close.png");
			setAss(settingsView, "settings.png");
			
			LinearLayout.LayoutParams iconP = new LinearLayout.LayoutParams(dpi(25), dpi(25));
			iconP.rightMargin = 5;
			rightLayout.addView(headerLayout, -1, dpi(33));
			LinearLayout purpleHead = new LinearLayout(context);
			purpleHead.setBackgroundColor(Color.parseColor("#867DEA"));
			
			// Textclose layout
			
			LinearLayout textClose = new LinearLayout(context);
			textClose.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			textClose.setPadding(10, 0, 0, 0);
			textClose.setOrientation(LinearLayout.HORIZONTAL);
			// Title
			title = new TextView(context);
			title.setTextSize(15f);
			title.setTextColor(Color.WHITE);
			title.setText("INSINETEAM - @insineteamdev");
			title.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			title.setTypeface(google(context));
			
			textClose.addView(title, new LinearLayout.LayoutParams(-2, dpi(30), 1));
			textClose.addView(settingsView, iconP);
			textClose.addView(closeView, iconP);
			headerLayout.addView(textClose, new LinearLayout.LayoutParams(-1, dpi(30)));
			headerLayout.addView(purpleHead, -1, dpi(3));
			
			/* Right down header */
			
			/* ru layout */
			LinearLayout rightUp = new LinearLayout(context);
			rightLayout.addView(rightUp, -1, -1);
			rightUp.setPadding(25, 25, 25, 25);
			
			/* Page layout */
			pages = new LinearLayout(context);
			pagess = new LinearLayout(context);
			GradientDrawable grad4 = new GradientDrawable();
			grad4.setColor(Color.parseColor("#26252A"));
			grad4.setCornerRadius(10f);
			pages.setBackgroundDrawable(grad4);
			pages.setElevation(15f);
			pages.setPadding(25,25,25,25);
			rightUp.addView(pages, -1, -1);
			pages.addView(scrollItems, -1, -1);
			scrollItems.addView(pagess, -1, -1);
			/* Page layout */
			
			showIcon();
		} catch (Exception e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		}
	}



	View.OnTouchListener handleMotionTouch = new View.OnTouchListener()
	{
		private float initX;          
		private float initY;
		private float touchX;
		private float touchY;

		double clock=0;
		@Override
		public boolean onTouch(View vw, MotionEvent ev)
		{

			switch (ev.getAction())
			{
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

				case MotionEvent.ACTION_UP:
					if (isIconVisible && (System.currentTimeMillis() < (clock + 200)))
					{
						showMenu();
					}
					break;
			}
			return true;
		}
	};
	
	private int convertDipToPixels(int i) {
        return (int) ((((float) i) * context.getResources().getDisplayMetrics().density) + 0.5f);
    }
}
