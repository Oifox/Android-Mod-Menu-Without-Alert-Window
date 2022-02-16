package il2cpp;

import android.content.Context;
import android.os.Handler;
import android.widget.LinearLayout;
import il2cpp.typefaces.Menu;
import il2cpp.typefaces.Watermark;
import il2cpp.typefaces.Crosshair;
import android.widget.Toast;
import java.util.ArrayList;
import android.location.Criteria;
import android.graphics.drawable.GradientDrawable;
import android.graphics.Color;

public class Main {
	protected static Context context;
	protected LinearLayout childOfScroll;
        private static native String[] getFeatures();
	public static boolean hide;
	private static native void onSwitchTap(int page, int feature, int checked);
	private static native void onCheckTap(int page, int feature, int checked);
	private static native void onButtonTap(int page, int feature);
	private static native void onSliderChange(int page, int feature, int value);
	private static native void onTextChange(int page, int feature, String text);
	
	public static void start(final Context context) {
        System.loadLibrary("gvraudio");
        Handler handler = new Handler();
	   	handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   new Main().MenuMain(context);
                }
            }, 3000);
	}
    
    
	public final void MenuMain(final Context context) {
		Main.context = context;
		
		final Crosshair crosshair = new Crosshair(context);
		crosshair.setWidth(crosshair.dpi(30));
		crosshair.setHeight(crosshair.dpi(30));
		crosshair.isVisible(false);
		
		final Watermark watermark = new Watermark(context);
		watermark.setWidth(watermark.dpi(170));
		watermark.setHeight(watermark.dpi(30));
		watermark.isVisible(false);
		
		final Menu menu = new Menu(context);
		menu.setWidth(menu.dpi(410));
		menu.setHeight(menu.dpi(280));
        
		String[] listFT = getFeatures();
		int pageId = 0;
        for (int i = 0; i < listFT.length; i++) {
            final int feature = i;
            String str = listFT[i];
            String[] split = str.split("`");
			
			String typetext = split[0];
			
			if (typetext.equals("NEWPAGE")) {
				menu.addPage(split[1], split[2], pageId);
				pageId++;
			} else if (typetext.equals("TEXT")) {
				menu.addTextPage(Integer.parseInt(split[1]), Integer.parseInt(split[2]), split[3]);
			} else if (typetext.equals("LINK")) {
				menu.addLinkButton(Integer.parseInt(split[1]), split[2], split[3]);
			} else if (typetext.equals("SWITCH")) {
				menu.addSwitch(Integer.parseInt(split[1]), Integer.parseInt(split[2]), split[3], new Menu.callbackSwitch() {
					public void onClickTap(int page, int feature, int checked) {
						onSwitchTap(page, feature, checked);
						//Toast.makeText(context, Integer.toString(page), Toast.LENGTH_LONG).show();
					}
				});
			} else if (typetext.equals("CHECK")) {
				menu.addCheckbox(Integer.parseInt(split[1]), Integer.parseInt(split[2]), split[3], new Menu.callbackCheck() {
					public void onClickTap(int page, int feature, int checked) {
						onCheckTap(page, feature, checked);
						//Toast.makeText(context, Integer.toString(page), Toast.LENGTH_LONG).show();
					}
				});
			} else if (typetext.equals("BUTTON")) {
				menu.addButton(Integer.parseInt(split[1]), Integer.parseInt(split[2]), split[3], new Menu.callbackButton() {
					public void onClickTap(int page, int feature) {
						onButtonTap(page, feature);
						//Toast.makeText(context, Integer.toString(page), Toast.LENGTH_LONG).show();
					}
				});
			} else if (typetext.equals("SLIDER")) {
				menu.addSlider(Integer.parseInt(split[1]), Integer.parseInt(split[2]), split[3], Integer.parseInt(split[4]), Integer.parseInt(split[5]), Integer.parseInt(split[6]), new Menu.callbackSlider() {
					public void onChange(int page, int feature, int value) {
						onSliderChange(page, feature, value);
					}
				}); 
			} else if (typetext.equals("INPUT_STR")) {
				menu.addInputStr(Integer.parseInt(split[1]), Integer.parseInt(split[2]), split[3], new Menu.callbackText() {
					public void onText(int page, int feat, String text) {
						onTextChange(page, feat, text);
					}
				});
			}
     	}
		int setting = menu.createSettings();
		menu.addTextPage(setting, 0, "Settings menu");
		menu.addCheckbox(setting, 1, "Hide icon menu", new Menu.callbackCheck() {
				public void onClickTap(int page, int feat, int ischeck) {
					menu.setIsVisible(ischeck);
				}
			});
		menu.addSlider(setting, 0, "Menu scale", 65, 100, 100, new Menu.callbackSlider() {
			public void onChange(int page, int feat, int value) {
				menu.setScale((float) value / 100);
			}
		});
		menu.addSlider(setting, 0, "Menu alpha", 10, 100, 100, new Menu.callbackSlider() {
				public void onChange(int page, int feat, int value) {
					menu.setAlpha((float) value / 100);
				}
			});
			
			
		menu.addTextPage(setting, 0, "Watermark");
		menu.addCheckbox(setting, 1, "Show watermark", new Menu.callbackCheck() {
				public void onClickTap(int page, int feat, int ischecked) {
					if (ischecked == 0) {
						watermark.isVisible(false);
					} else {
						watermark.isVisible(true);
					}
				}
			});
		
		menu.addTextPage(setting, 0, "Crosshair");
		menu.addCheckbox(setting, 1, "Show crosshair", new Menu.callbackCheck() {
				public void onClickTap(int page, int feat, int ischecked) {
					if (ischecked == 0) {
						crosshair.isVisible(false);
					} else {
						crosshair.isVisible(true);
					}
				}
			});
			
		menu.addSlider(setting, 0, "Crosshair size", 10, 100, 100, new Menu.callbackSlider() {
			public void onChange(int page, int feat, int value) {
				crosshair.setSize((float) value / 100);
			}
		});
		menu.addSlider(setting, 0, "Red Color", 1, 255, 0, new Menu.callbackSlider() {
				public void onChange(int page, int feat, int value) {
					crosshair.setCR(value);
					crosshair.updateColor();
				}
			});
		menu.addSlider(setting, 0, "Green Color", 1, 255, 255, new Menu.callbackSlider() {
				public void onChange(int page, int feat, int value) {
					crosshair.setCG(value);
					crosshair.updateColor();
				}
			});
		menu.addSlider(setting, 0, "Blue Color", 1, 255, 0, new Menu.callbackSlider() {
				public void onChange(int page, int feat, int value) {
					crosshair.setCB(value);
					crosshair.updateColor();
				}
			});
		menu.addCH(crosshair);

		menu.addTextPage(setting, 0, "Insineteam socials");

		menu.addLinkButton(setting, "INSINETEAM TELEGRAM", "https://t.me/norkaware");
		menu.addLinkButton(setting, "INSINETEAM TG PM", "https://t.me/norka_lua");
		
		menu.addCallback();
	}
}

