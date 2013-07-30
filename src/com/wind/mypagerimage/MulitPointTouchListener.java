package com.wind.mypagerimage;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
 * 自定义图片放大的监听，限定了图片放大和缩小的拖动范围
 * 
 * @author wind ModifiedTime:二零一三年七月二十六日　星期五
 * 
 */
public class MulitPointTouchListener implements OnTouchListener {

	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private Matrix initMatrix = new Matrix();

	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;

	private int mode = NONE;

	private PointF start = new PointF();
	private PointF mid = new PointF();
	private float oldDist = 1f;

	/** 屏幕宽度 */
	private int dWidth = 0;
	/** 屏幕高度 */
	private int dHeight = 0;
	/** 图片的上下左右距离的类 */
	private ImageState state;
	private float[] values = new float[9];
	/** 图片宽度 */
	private float bWidth;// 图片宽度
	/** 图片高度 */
	private float bHeight;
	/** 初始原始数据的标志(像图片的原始高和宽，还有图片的原始Matrix,标题栏的高等),只执行一次的操作 */
	private boolean flag = false;
	/** 图片原始宽度（在放大和缩小的过程中会改变图片的高和宽） */
	private float initWidth = 0.0f;// 图片第一次显示时的初始宽�?
	/** 屏幕原始高度（在放大和缩小的过程中会改变图片的高和宽） */
	private float initHeight = 0.0f;
	/** 标题栏的高 */
	private float topHeight = 0.0f;

	public MulitPointTouchListener(int dWidth, int dHeight) {
		this.dHeight = dHeight;
		this.dWidth = dWidth;
		this.state = new ImageState();
	}

	private class ImageState {
		private float left;
		private float top;
		private float right;
		private float bottom;
	}

	// 刷新界面
	private void setView(ImageView imageView) {
		Rect rect = imageView.getDrawable().getBounds();
		imageView.getImageMatrix().getValues(values);
		bWidth = rect.width() * values[0];
		bHeight = rect.height() * values[0];

		state.left = values[2];
		state.top = values[5];
		state.right = state.left + bWidth;
		state.bottom = state.top + bHeight;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView imageView = (ImageView) v;
		imageView.setScaleType(ImageView.ScaleType.MATRIX);
		setView(imageView);
		if (!flag) {
			flag = true;

			/* 定义一个区域 */
			Rect frame = new Rect();
			/* 区域范围为该textview的区域范围 */
			imageView.getWindowVisibleDisplayFrame(frame);
			/* 获取状态栏高度。因为获取的区域不包含状态栏 */
			topHeight = frame.top;
			Log.d("MulitPointTouchListener", "topHeight= " + topHeight);

			float xScale = (float) dWidth / bWidth;
			float yScale = (float) dHeight / bHeight;
			float mScale = xScale <= yScale ? xScale : yScale;
			float scale = mScale < 1 ? mScale : 1;
			initMatrix.postTranslate((dWidth - bWidth) / 2,
					(dHeight - bHeight) / 2);
			float sX = dWidth / 2;
			float sY = dHeight / 2;
			initWidth = bWidth * scale;
			initHeight = bHeight * scale;
			initMatrix.postScale(scale, scale, sX, sY);
		}
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			matrix.set(imageView.getImageMatrix());
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			mode = DRAG;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
			}
			break;
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		case MotionEvent.ACTION_MOVE:
			// boolean b = event.getX() > state.left && event.getX() <
			// state.right
			// && event.getY() > state.top && event.getY() < state.bottom;
			// if (b) {//落点是否在图片上
			if (mode == DRAG) {// 拖动
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - start.x, event.getY()
						- start.y);
				// }
			} else if (mode == ZOOM) {// 缩放
				float newDist = spacing(event);
				if (newDist > 10f) {
					float scale = newDist / oldDist;
					if (scale < 1) {
						matrix.set(savedMatrix);
						matrix.postScale(scale, scale, mid.x, mid.y);
					} else {
						if (bWidth <= dWidth + dWidth / 2
								&& bHeight - topHeight <= dHeight - topHeight
										+ (dHeight - topHeight) / 2) {
							matrix.set(savedMatrix);
							matrix.postScale(scale, scale, mid.x, mid.y);
						}
					}
				}
			}
			// }
			break;
		case MotionEvent.ACTION_UP:
			if (bWidth <= dWidth && bHeight <= dHeight - topHeight) {// 放大后图的高和宽没有超出屏幕的高和宽
				if (state.left <= 0 && state.top <= 0) {// 左上角
					matrix.postTranslate(0 - state.left, 0 - state.top);
				} else if (state.top <= 0 && state.right >= dWidth) {// 右上角
					matrix.postTranslate(dWidth - state.right, 0 - state.top);
				} else if (state.right >= dWidth
						&& state.bottom >= dHeight - topHeight) {// 右下角
					matrix.postTranslate(dWidth - state.right, dHeight
							- topHeight - state.bottom);
				} else if (state.bottom >= dHeight - topHeight
						&& state.left <= 0) {// 左下角
					matrix.postTranslate(0 - state.left, dHeight - topHeight
							- state.bottom);
				} else {
					if (state.left <= 0) {// 左边
						matrix.postTranslate(0 - state.left, 0);
					} else if (state.top <= 0) {// 上边
						matrix.postTranslate(0, 0 - state.top);
					} else if (state.right >= dWidth) {// 右边
						matrix.postTranslate(dWidth - state.right, 0);
					} else if (state.bottom >= dHeight - topHeight) {// 下边
						matrix.postTranslate(0, dHeight - topHeight
								- state.bottom);
					}
				}
				if (bWidth <= initWidth && bHeight <= initHeight - topHeight) {
					matrix.set(initMatrix);
				}
			} else if (bWidth >= dWidth || bHeight >= dHeight - topHeight) {// 图片和高和宽有满足一个大于屏幕的高和宽

				if (bWidth >= dWidth && bHeight <= dHeight - topHeight) {// 放大后的图片宽大于屏幕的宽，放大后的高不大于屏幕的高时，即左右拖动时
					if (state.left >= 0 && state.right >= dWidth) {// 左边
						matrix.postTranslate(0 - state.left, (dHeight
								- topHeight - bHeight)
								/ 2 - state.top);
					} else if (state.right <= dWidth && state.left <= 0) {// 右边
						matrix.postTranslate(dWidth - state.right, (dHeight
								- topHeight - bHeight)
								/ 2 - state.top);
					} else if (state.top <= 0
							&& state.bottom <= dHeight - topHeight) {// 上边
						matrix.postTranslate(0, 0 - state.top);
					} else if (state.bottom >= dHeight - topHeight
							&& state.top >= 0) {// 下边
						matrix.postTranslate(0, dHeight - topHeight
								- state.bottom);
					}
				} else if (bHeight >= dHeight - topHeight && bWidth <= dWidth) {// 放大后的图片高大于屏幕的高，放大后的宽不大于屏幕的宽时，即上下拖动时
					if (state.top >= 0 && state.bottom >= dHeight - topHeight) {// 上边
						matrix.postTranslate(
								(dWidth - bWidth) / 2 - state.left,
								0 - state.top);
					} else if (state.bottom <= dHeight - topHeight
							&& state.top <= 0) {// 下边
						matrix.postTranslate(
								(dWidth - bWidth) / 2 - state.left, dHeight
										- topHeight - state.bottom);
					} else if (state.left <= 0 && state.right <= dWidth) {// 左边
						matrix.postTranslate(0 - state.left, 0);
					} else if (state.right >= dWidth && state.left >= 0) {// 右边
						matrix.postTranslate(dWidth - state.right, 0);
					}
				} else if (bWidth >= dWidth && bHeight >= dHeight) {// 放大后图片的高和宽都大于屏幕的高和宽时

					if (state.left >= 0 && state.top >= 0
							&& state.right >= dWidth && state.bottom >= dHeight) {// 左上角
						matrix.postTranslate(0 - state.left, 0 - state.top);
					} else if (state.top >= 0 && state.right <= dWidth
							&& state.left <= 0 && state.bottom >= dHeight) {// 右上角
						matrix.postTranslate(dWidth - state.right,
								0 - state.top);
					} else if (state.right <= dWidth
							&& state.bottom <= dHeight - topHeight
							&& state.left <= 0 && state.top <= 0) {// 右下角
						matrix.postTranslate(dWidth - state.right, dHeight
								- topHeight - state.bottom);
					} else if (state.bottom <= dHeight - topHeight
							&& state.left >= 0 && state.top <= 0
							&& state.right >= dWidth) {// 左下角
						matrix.postTranslate(0 - state.left, dHeight
								- topHeight - state.bottom);
					} else {
						if (state.left >= 0 && state.top <= 0
								&& state.right >= dWidth
								&& state.bottom >= dHeight) {// 左边
							matrix.postTranslate(0 - state.left, 0);
						} else if (state.top >= 0 && state.left <= 0
								&& state.right >= dWidth
								&& state.bottom >= dHeight) {// 上边
							matrix.postTranslate(0, 0 - state.top);
						} else if (state.right <= dWidth && state.left <= 0
								&& state.top <= 0 && state.bottom >= dHeight) {// 右边
							matrix.postTranslate(dWidth - state.right, 0);
						} else if (state.bottom <= dHeight - topHeight
								&& state.left <= 0 && state.top <= 0
								&& state.right >= dWidth) {// 下边
							matrix.postTranslate(0, dHeight - topHeight
									- state.bottom);
						}
					}

				}
			}
			break;
		}

		imageView.setImageMatrix(matrix);
		return true;
	}

	/**
	 * 计算两点之间的距离
	 * 
	 * @param event
	 * @return
	 */
	private float spacing(MotionEvent event) {
		if (event.getPointerCount() == 2) {
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			return (float) Math.sqrt(x * x + y * y);
		}
		return 0;
	}

	/**
	 * 缩放的中间点
	 * 
	 * @param point
	 * @param event
	 */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
}