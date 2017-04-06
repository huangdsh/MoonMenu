package com.example.rotatemenu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.example.rotatemenu.R;


/**
 * @author anumbrella Created on 2015-7-2 上午12:21:39 定义自定义控件绘画类
 */


public class ArcMenu extends ViewGroup implements OnClickListener {

	/**
	 * 圆的半径
	 */
	private int mRadius;

	private Position mPosition = Position.LEFT_BOTTOM; // 采用枚举类型，默认显示在屏幕的右下方

	private OnMenuItemClickListener mMenuItemClickListener;

	private View mButton;

	// 设置菜单的状态，默认点击菜单时为关的状态
	private Status mCurrentStatus = Status.CLOSE;
	/**
	 * 定义菜单的位置属性值
	 */
	public static final int POS_LEFT_TOP = 0;
	public static final int POS_LEFT_BOTTOM = 1;
	public static final int POS_RIGHT_TOP = 2;
	public static final int POS_RIGHT_BOTTOM = 3;

	/**
	 * 定义枚举类型，菜单点击时菜单的开启或关闭状态
	 */
	public enum Status {
		CLOSE, OPEN;
	}

	/**
	 * 定义枚举类型，菜单的具体位置(相当于字符串设定位置)
	 */
	public enum Position {
		LEFT_BOTTOM, RIGHT_BOTTOM, LEFT_TOP, RIGHT_TOP;
	}

	/**
	 * 点击子菜单时的回调函数
	 */
	public interface OnMenuItemClickListener {
		public void onClick(View view, int position);
	}

	/**
	 * @param onMenuItemClickListener
	 *            设定子菜单选项点击的回调函数
	 */
	public void setOnMenuItemClickListener(
			OnMenuItemClickListener onMenuItemClickListener) {
		this.mMenuItemClickListener = onMenuItemClickListener;
	}

	// 在源码中实例化一个view会调用该构造方法
	public ArcMenu(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	// 在xml中实例化的view会调用该构造方法
	public ArcMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	// 由前两种方法显示调用
	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ArcMenu(Context context, AttributeSet attrs, int defStyle) {
		// 第三个参数为系统默认的参数主题
		super(context, attrs, defStyle);

		// 将mRadius设置为100dip的大小
		mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				100, getResources().getDisplayMetrics());

		/**
		 * 获得values文件下地attr文件的AreMenu的属性数组
		 * 
		 * 用法：
		 * 
		 * 　set：属性值的集合
		 * 
		 * 　attrs：我们要获取的属性的资源ID的一个数组，如同ContextProvider中请求数据库时的Projection数组，
		 * 就是从一堆属性中我们希望查询什么属性的值（values下的attr文件）
		 * 
		 * 　defStyleAttr：这个是当前Theme中的一个attribute，是指向style的一个引用，当在layout
		 * xml中和style中都没有为View指定属性时
		 * ，会从Theme中这个attribute指向的Style中查找相应的属性值，这就是defStyle的意思
		 * ，如果没有指定属性值，就用这个值，所以是默认值
		 * ，但这个attribute要在Theme中指定，且是指向一个Style的引用，如果这个参数传入0表示不向Theme中搜索默认值
		 * 
		 * 　defStyleRes：这个也是指向一个Style的资源ID，
		 * 但是仅在defStyleAttr为0或defStyleAttr不为0但Theme中没有为defStyleAttr属性赋值时起作用
		 * 
		 * */

		TypedArray typedArray = context.getTheme().obtainStyledAttributes(
				attrs, R.styleable.AreMenu, defStyle, 0);

		// 获取定义菜单的位置，默认值为右下
		int position = typedArray.getInt(R.styleable.AreMenu_position,
				POS_LEFT_BOTTOM);

		// 根据定义值，选择菜单的位置
		switch (position) {
		case POS_LEFT_TOP:
			mPosition = Position.LEFT_TOP;
			break;
		case POS_LEFT_BOTTOM:
			mPosition = Position.LEFT_BOTTOM;
			break;
		case POS_RIGHT_TOP:
			mPosition = Position.RIGHT_TOP;
			break;
		case POS_RIGHT_BOTTOM:
			mPosition = Position.RIGHT_BOTTOM;
			break;
		default:
			break;
		}

		// 从xml中获取定义的半径值，默认为100dip（xml给定为140dip）
		mRadius = (int) typedArray.getDimension(R.styleable.AreMenu_radius,
				TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
						getResources().getDisplayMetrics()));

		/*
		 * 在TypedArray后调用recycle主要是为了缓存。 当recycle被调用后，这就说明这个对象从现在可以被重用了。
		 * TypedArray内部持有部分数组，它们缓存在Resources类中的静态字段中，这样就不用每次使用前都需要分配内存
		 */

		typedArray.recycle();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onMeasure(int, int) 测量屏幕中得组件所占的空间大小
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int count = getChildCount(); // 获得布局中得子部件的个数

		// 测量子视图的大小
		for (int i = 0; i < count; i++) {
			measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		if (changed) {
			// 设定布局
			layoutOut();
			// 设定界面的布局
			int count = getChildCount(); // count = 6
			for (int i = 0; i < count-1; i++) {
				View viewChild = getChildAt(i+1);
				viewChild.setVisibility(View.GONE);

				int mX = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2)
						* i));
				int mY = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2)
						* i));

				int cWidth = viewChild.getMeasuredWidth();
				int cHeihgt = viewChild.getMeasuredHeight();

				if (mPosition == Position.LEFT_BOTTOM
						|| mPosition == Position.RIGHT_BOTTOM) {
					mY = getMeasuredHeight() - cHeihgt - mY;

				}

				if (mPosition == Position.LEFT_TOP
						|| mPosition == Position.RIGHT_TOP) {
					mX = getMeasuredWidth() - cWidth - mX;

				}

				viewChild.layout(mX, mY, mX + cWidth, mY + cHeihgt);

			}

		}

	}

	// 设置菜单的布局位置
	private void layoutOut() {
		// TODO Auto-generated method stub
		mButton = getChildAt(0); // 获得菜单按钮
		mButton.setOnClickListener(this);
		int y = 0; // x轴距离
		int x = 0; // y轴距离
		int width = mButton.getMeasuredWidth(); // 获得按钮的宽度
		int height = mButton.getMeasuredHeight(); // 获得按钮的高度

		switch (mPosition) {
		case LEFT_TOP:
			x = 0;
			y = 0;
			break;
		case LEFT_BOTTOM:
			x = 0;
			y = getMeasuredHeight() - height;
			break;
		case RIGHT_TOP:
			x = getMeasuredWidth() - width;
			y = 0;
			break;
		case RIGHT_BOTTOM:
			x = getMeasuredWidth() - width;
			y = getMeasuredHeight() - height;
			break;

		}
		/*
		 * 参数l、t、r和b分别用来描述当前视图的左上右下四条边与其父视图的左上右下四条边的距离(均是以左上角为坐标原点,[0,0])
		 */
		mButton.layout(x, y, x + width, y + height);

	}

	// 点击菜单，加入旋转的动画
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		rotatemButton(v, 0f, 360f, 300); // v,本身(view) ; 0f,开始 ;360f,结束 ;300，时间
		switchMenu(300); // 持续的时间

	}

	// 为菜单的子项添加平移的动画和旋转的动画
	private void switchMenu(int duration) {
		// TODO Auto-generated method stub
		int count = getChildCount(); // 获取所有的菜单子项(count为6)
		for (int i = 0; i < count-1; i++) {
			final View viewChild = getChildAt(i+1); // 具体的菜单项视图
			viewChild.setVisibility(View.VISIBLE);

			// 每个子选项距离x轴的距离
			int mX = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));

			// 每个子选项距离y轴的距离
			int mY = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));

			// 默认为左下的菜单，所以移动默认为向x轴负方向上移动
			int yFlog = 1;
			int xFlog = -1;

			//根据菜单的位置设置动画的方向
			if (mPosition == Position.LEFT_TOP
					|| mPosition == Position.LEFT_BOTTOM) {
				xFlog = -1;
			}
			if (mPosition == Position.LEFT_TOP
					|| mPosition == Position.RIGHT_TOP) {
				yFlog = -1;
			}

			// 设置动画
			AnimationSet mAnimationSet = new AnimationSet(true);
			Animation animation = null;

			// 点击时为关就打开菜单
			if (mCurrentStatus == Status.CLOSE) {

				/*
				 * 平移动画 fromXDelta :动画开始的点离当前view x坐标的距离差值 toXDelta
				 * :动画结束的点离当前view x坐标的距离差值 fromYDelta : 动画开始的点离当前view y坐标的距离差值
				 * toYDeltat : 动画结束的点离当前view y坐标的距离差值
				 */
				animation = new TranslateAnimation(mX * xFlog, 0, mY * yFlog, 0);

				// 选中 = 获取焦点 + 点击
				viewChild.setClickable(true); // 子菜单可以点击
				viewChild.setFocusable(true); // 可以获取焦点

			} else {
				// 关闭菜单

				animation = new TranslateAnimation(0, mX * xFlog, 0, mY * yFlog);
				viewChild.setClickable(false);
				viewChild.setFocusable(false);

			}

			animation.setFillAfter(true);
			animation.setDuration(duration);

			// // 设置延迟动画的执行，参数为时间为毫秒
			animation.setStartOffset(i * 100 / count);

			// 给动画设置监听器
			animation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					// 当动画全部结束后执行的处理
					if (mCurrentStatus == Status.CLOSE) {
						viewChild.setVisibility(View.GONE); // 改变onLayout哪里的布局显示情况
					}

				}
			});

			// 设置旋转的动画
			RotateAnimation rotateAnimation = new RotateAnimation(0, 720,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			rotateAnimation.setDuration(duration);
			rotateAnimation.setFillAfter(true);
			mAnimationSet.addAnimation(rotateAnimation);
			mAnimationSet.addAnimation(animation);
			viewChild.startAnimation(mAnimationSet); // 开启动画

			final int pos = i + 1;

			// 设置子项被点击时的事件处理逻辑
			viewChild.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					// 回调函数的使用
					if (mMenuItemClickListener != null) {
						mMenuItemClickListener.onClick(v, pos);
					}

					// 子项动画
					MenuItemAnimation(pos - 1);
					changeStatus(); // 点击子菜单，菜单的状态不变，只是缩小到看不见情况

				}

			});

		}

		changeStatus(); // 真正起切换菜单的作用

	}

	protected void changeStatus() {
		// TODO Auto-generated method stub
		mCurrentStatus = (mCurrentStatus == Status.CLOSE) ? Status.OPEN
				: Status.CLOSE;

	}

	//菜单子项的动画
	private void MenuItemAnimation(int position) {
		// TODO Auto-generated method stub
		int count = getChildCount(); // count = 6
		for (int i = 0; i < count - 1; i++) {
			View viewChild = getChildAt(i + 1);
			if (i == position) {
				// 放大显示
				viewChild.startAnimation(scaleBigAnimation(300));
			} else {
				// 缩小到没有
				viewChild.startAnimation(scaleSmallAnimation(300));
			}
			
			viewChild.setClickable(false);
			viewChild.setFocusable(false);

		}

	}

	// 缩小到没有
	private Animation scaleSmallAnimation(int duration) {
		// TODO Auto-generated method stub

		AnimationSet mAnimationSet = new AnimationSet(true);

		ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f,
				0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);

		AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

		mAnimationSet.addAnimation(alphaAnimation);
		mAnimationSet.addAnimation(scaleAnimation);
		mAnimationSet.setDuration(duration);
		mAnimationSet.setFillAfter(true);
		return mAnimationSet;

	}

	// 为当前点击的item设置变大和降低透明度的动画
	private Animation scaleBigAnimation(int duration) {
		// TODO Auto-generated method stub
		AnimationSet mAnimationSet = new AnimationSet(true);

		/**
		 * 
		 * float fromX 动画起始时 X坐标上的伸缩尺寸 float toX 动画结束时 X坐标上的伸缩尺寸 float fromY
		 * 动画起始时Y坐标上的伸缩尺寸 float toY 动画结束时Y坐标上的伸缩尺寸 int pivotXType 动画在X轴相对于物件位置类型
		 * float pivotXValue 动画相对于物件的X坐标的开始位置 int pivotYType 动画在Y轴相对于物件位置类型
		 * float pivotYValue 动画相对于物件的Y坐标的开始位置
		 **/
		ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 4.0f, 1.0f,
				4.0f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);

		AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

		mAnimationSet.addAnimation(alphaAnimation);
		mAnimationSet.addAnimation(scaleAnimation);
		mAnimationSet.setDuration(duration);
		mAnimationSet.setFillAfter(true);
		return mAnimationSet;
	}

	// 点击按钮的旋转
	private void rotatemButton(View v, float start, float end, int duration) {
		// TODO Auto-generated method stub
		// 缩放模式：RELATIVE_TO_SELF,相对于自己的缩放模式
		RotateAnimation animation = new RotateAnimation(start, end,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animation.setDuration(duration);
		animation.setFillAfter(true); // 动画执行完后是否停留在执行完的状态
		v.startAnimation(animation);

	}

}
