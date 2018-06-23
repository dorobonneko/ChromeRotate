package com.moe.chromeorentation;
import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.View;
import android.view.Window;
import android.graphics.PixelFormat;
import android.content.pm.ActivityInfo;
import android.view.KeyEvent;
import android.view.Display;

public class Service extends AccessibilityService
{
	private WindowManager wm;
	private View view;
	private WindowManager.LayoutParams param;
	private Rect rect=new Rect();
	private boolean state;
	@Override
	public void onCreate()
	{
		// TODO: Implement this method
		super.onCreate();
		wm=((WindowManager)getSystemService(WINDOW_SERVICE));
	}
	
	@Override
	public void onAccessibilityEvent(AccessibilityEvent p1)
	{
		if(p1.getSource()==null)return;
		AccessibilityNodeInfo info=p1.getSource();
		if(info.getClassName().equals("android.widget.FrameLayout")){
			if(info.getChildCount()==1&&(info=info.getChild(0))!=null){
				info.getBoundsInScreen(rect);
				setOrietation(rect.top==0);
			}else{
				setOrietation(false);
			}
		}else if(info.getClassName().equals("android.webkit.WebView")){
			setOrietation(false);
		}
	}

	@Override
	public void onInterrupt()
	{
		setOrietation(false);
	}
	public void setOrietation(boolean land){
		if(state==land)return;
		state=land;
		if(param==null){
		param=new WindowManager.LayoutParams(0,0);
		param.type=WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
		param.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		param.format=PixelFormat.RGBA_8888;
		param.alpha=0;
		}
		if(view==null){
			view=new View(getApplicationContext());
			wm.addView(view,param);
		}
		try{
		if(land){
			param.screenOrientation=4;
			wm.updateViewLayout(view,param);
			}else{
				param.screenOrientation=-1;
			wm.updateViewLayout(view,param);
			}}catch(Exception e){}
	}
	
}
