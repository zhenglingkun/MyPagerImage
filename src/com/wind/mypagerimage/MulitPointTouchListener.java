package com.wind.mypagerimage;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class MulitPointTouchListener implements OnTouchListener {

	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	// /** 初始状态 */
	// private Matrix initMatrix = null;

	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;

	private int mode = NONE;

	private PointF start = new PointF();
	private PointF mid = new PointF();
	private float oldDist = 1f;
	private float[] values = new float[9];
	private ImageState state = new ImageState();
	private float imageWidth = 0.0f;
	private float imageHeight = 0.0f;
	private int dWidth = 0;
	private int dHeight = 0;
	/** 初始宽 */
	private float initWidth = 0.0f;
	private boolean firstEvent = false;
	private int maxWidth = 0;
	private float oldStartX = 0.0f;

	// private final int MAX_WIDTH = 500;

	public MulitPointTouchListener(int dWidth, int dHeight) {
		this.dWidth = dWidth;
		this.dHeight = dHeight;
		this.maxWidth = dWidth + 500;
	}

	private class ImageState {
		private float left;
		private float top;
		private float right;
		private float bottom;
	}

	// 刷新界面
	public void setView(ImageView imageView) {
		Rect rect = imageView.getDrawable().getBounds();
		imageView.getImageMatrix().getValues(values);
		imageWidth = rect.width() * values[0];
		imageHeight = rect.height() * values[0];

		state.left = values[2];
		state.top = values[5];
		state.right = state.left + imageWidth;
		state.bottom = state.top + imageHeight;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView view = (ImageView) v;
		setView(view);
		// Log.d("onTouch", "imageWidth = " + imageWidth + " imageHeight = "
		// + imageHeight);
		// Log.d("ImageView", "view.width = " + view.getWidth() +
		// " view.height = "
		// + view.getHeight());
		if (!firstEvent) {
			
			/*定义一个区域*/
	        Rect frame = new Rect();  
	        /*区域范围为该textview的区域范围*/
	        view.getWindowVisibleDisplayFrame(frame);  
	        /*获取状态栏高度。因为获取的区域不包含状态栏*/
	        dHeight = dHeight - frame.top;
			firstEvent = true;
			Rect rect = view.getDrawable().getBounds();
			view.getImageMatrix().getValues(values);
			initWidth = rect.width() * values[0];
		}
		view.setScaleType(ImageView.ScaleType.MATRIX);
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			oldStartX = event.getX();
			matrix.set(view.getImageMatrix());
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
			if (mode == DRAG) {
				Log.d("mode == DRAG", "state.left = " + state.left
						+ " state.right = " + state.right + " state.top = "
						+ state.top + " state.bottom = " + state.bottom);
				Log.d("mode == DRAG", "dWidth = " + dWidth + " dHeight = " + dHeight);
				// Log.d("mode == DRAG","start.x = " + start.x + " start.y = " +
				// start.y);
//				int i = (int) (event.getX() / oldStartX); 
//				Log.d("mode == DRAG","i = " + i + " event.getX() = " + event.getX() + " oldStartX = " + oldStartX);
				if (imageWidth < maxWidth && imageWidth > dWidth ) {
					if (state.left <= 0) {
						matrix.set(savedMatrix);
						matrix.postTranslate(event.getX() - start.x, event.getY()
								- start.y);
					}
				} else {
//					Log.d("dWidth","dWidth");
					if (state.left >= 0 && state.top >=0 && state.right <= dWidth && state.bottom <= dHeight) {
							matrix.set(savedMatrix);
							matrix.postTranslate(event.getX() - start.x, event.getY()
									- start.y);
					}
				}
//				matrix.set(savedMatrix);
//				matrix.postTranslate(event.getX() - start.x, event.getY()
//						- start.y);
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					float scale = newDist / oldDist;
					if (scale >= 1 && imageWidth < (maxWidth)) {
						matrix.set(savedMatrix);
						matrix.postScale(scale, scale, mid.x, mid.y);
					} else if (scale <= 1 && imageWidth > initWidth) {
						matrix.set(savedMatrix);
						matrix.postScale(scale, scale, mid.x, mid.y);
					}
				}

			}

			break;
		}

		view.setImageMatrix(matrix);
		return true;
	}

	// m_weibo_image.setScaleType(ImageView.ScaleType.CENTER);
	// m_weibo_image.setOnTouchListener(new MulitPointTouchListener());

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
}
