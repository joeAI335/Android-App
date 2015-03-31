package com.example.mymenu;

import android.R.integer;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

public class MainUI extends RelativeLayout{
    
	private Context context;
	private Scroller mScroller;
	private FrameLayout leftmenu;
	private FrameLayout middlemenu;
	private FrameLayout rightmenu;
	private FrameLayout middlemask;
	public static final int LEFT_ID = 0xaabbcc;
	public static final int MIDDLE_ID = 0xaabbdd;
	public static final int RIGHT_ID = 0Xccbbaa;
	
	public MainUI(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	public MainUI(Context context, AttributeSet attrs) {
		super(context,attrs);
		initView(context);
	}
    
	private void initView(Context context){
		this.context = context;
		mScroller = new Scroller(context, new DecelerateInterpolator());
		leftmenu = new FrameLayout(context);
		middlemenu = new FrameLayout(context);
		rightmenu = new FrameLayout(context);
		middlemask = new FrameLayout(context);
		leftmenu.setBackgroundColor(Color.RED);
		middlemenu.setBackgroundColor(Color.GREEN);
		rightmenu.setBackgroundColor(Color.RED);
		middlemask.setBackgroundColor(0x88000000);
		leftmenu.setId(LEFT_ID);
		middlemenu.setId(MIDDLE_ID);
		rightmenu.setId(RIGHT_ID);
		addView(leftmenu);
		addView(middlemenu);
		addView(rightmenu);
		addView(middlemask);
		middlemask.setAlpha(0);
		//onmiddlemask();
	}
	
	
	
	private float onmiddlemask(){
		System.out.println(middlemask.getAlpha());
		return middlemask.getAlpha();
	}
	@Override
	public void scrollTo(int x, int y) {
		// TODO Auto-generated method stub
		onmiddlemask();
		super.scrollTo(x, y);
		int curX = Math.abs(getScrollX());
		float scale = curX/(float)leftmenu.getMeasuredWidth();
		middlemask.setAlpha(scale);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//middlemenu.measure(widthMeasureSpec, heightMeasureSpec);
		middlemenu.measure(widthMeasureSpec, heightMeasureSpec);
		int realwidth = MeasureSpec.getSize(widthMeasureSpec);
		int tempWidthMeasure = MeasureSpec.makeMeasureSpec(
				(int)(realwidth*0.8f), MeasureSpec.EXACTLY);
		
		leftmenu.measure(tempWidthMeasure, heightMeasureSpec);
		rightmenu.measure(tempWidthMeasure, heightMeasureSpec);
		//middlemenu.measure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		middlemenu.layout(l, t, r, b);
		middlemask.layout(l, t, r, b);
		leftmenu.layout(l - leftmenu.getMeasuredWidth(), t, r, b);
		rightmenu.layout(
				l + middlemenu.getMeasuredWidth(), 
				t, 
				r + middlemenu.getMeasuredWidth() + rightmenu.getMeasuredWidth(), 
				b);
	}
	
	private boolean isTestComplete;
	private boolean isleftrightEvent;
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (!isTestComplete) {
			getEventType(ev);
			return true;
		}
		if (isleftrightEvent) {
			//System.out.println("1");
			switch (ev.getActionMasked()) {
			case MotionEvent.ACTION_MOVE:
				int curScrollX = getScrollX();
				int dis_X = (int) (ev.getX() - point.x);
				int expectX = -dis_X + curScrollX;
				int finalX = 0;
				if(expectX < 0){
					finalX = Math.max(expectX, -leftmenu.getMeasuredWidth());
				}else{
					finalX = Math.min(expectX, rightmenu.getMeasuredWidth());
				}
				scrollTo(finalX, 0);
				point.x = (int) ev.getX();
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				curScrollX = getScrollX();
				if (Math.abs(curScrollX)>leftmenu.getMeasuredWidth() >>1) {
					if (curScrollX<0) {
						mScroller.startScroll(curScrollX, 0, 
								-leftmenu.getMeasuredWidth() - curScrollX
								, 0, 200);
					}else{
						mScroller.startScroll(curScrollX, 0, 
								leftmenu.getMeasuredWidth() - curScrollX, 0, 200);
					}
				}else{
					mScroller.startScroll(curScrollX, 0, -curScrollX, 0);
				}
				invalidate();
				isleftrightEvent = false;
				isTestComplete = false;
			default:
				break;
			}
		}else{
			switch (ev.getAction()) {
			case MotionEvent.ACTION_UP:
				isleftrightEvent = false;
				isTestComplete = false;
				break;

			default:
				break;
			}
		}
		return super.dispatchTouchEvent(ev);
	}
    
	
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		super.computeScroll();
		if (!mScroller.computeScrollOffset()) {
			return;
		}
		int tempX = mScroller.getCurrX();
		scrollTo(tempX, 0);
	}
	private Point point = new Point();
	private static final int TEST_DIS = 20;
	private void getEventType(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			point.x = (int)ev.getX();
			point.y = (int)ev.getY();
			super.dispatchTouchEvent(ev);
			break;
		case MotionEvent.ACTION_MOVE:
			int dx = Math.abs((int)ev.getX() - point.x);
			int dy = Math.abs((int)ev.getY() - point.y);
			if (dx >= TEST_DIS && dx > dy) {
				isleftrightEvent = true;
				isTestComplete = true;
				point.x = (int) ev.getX();
				point.y = (int) ev.getX();
			}else if(dy >= TEST_DIS && dy > dx){
				isleftrightEvent = false;
				isTestComplete = true;
				point.x = (int) ev.getX();
				point.y = (int) ev.getX();
			}
			
			break;
		case MotionEvent.ACTION_UP:
			break;
		case MotionEvent.ACTION_CANCEL:
			super.dispatchTouchEvent(ev);
			isleftrightEvent = false;
			isTestComplete = false;
			break;
		default:
			break;
		}
		
	}
	
}
