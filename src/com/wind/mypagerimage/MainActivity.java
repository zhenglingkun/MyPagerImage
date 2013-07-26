package com.wind.mypagerimage;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.app.Activity;
import android.graphics.Bitmap;

public class MainActivity extends Activity {
	
	private int dWidth = 0;
	private int dHeight = 0;

	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private ViewPager pager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去程序标题
		setContentView(R.layout.activity_main);
		
		 /* 取得屏幕分辨率大小 */   
        DisplayMetrics dm=new  DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        dWidth = dm.widthPixels;  
        dHeight = dm.heightPixels; 
		
		String[] imageUrls = {
				"http://hiphotos.baidu.com/baidu/pic/item/f603918fa0ec08fabf7a641659ee3d6d55fbda0d.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/43a7d933c895d143d011bf9273f082025aaf071f.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/63d0f703918fa0ec2ebf584b269759ee3d6ddb7f.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/5ab5c9ea15ce36d31ed8387f3af33a87e850b1a5.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/8601a18b87d6277f6e46217628381f30e924fc2c.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/b48f8c54acf9964c3a29350e.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/bd3eb13533fa828b48da6aabfd1f4134960a5af9.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/29381f30e924b899da3ce5706e061d950a7bf672.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/bd3eb13533fa828b48da6aabfd1f4134960a5af9.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/4bed2e738bd4b31cd73d63fd87d6277f9e2ff877.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/caef76094b36acaf92b619b87cd98d1001e99c24.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/8435e5dde71190efd6154d95ce1b9d16fcfa608a.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/b3de9c824ba1d4cd6d81190f.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/e0fe9925cc2c683834a80f11.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/0bd162d9f2d3572c65911a988a13632762d0c307.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/ac6eddc451da81cb2ac1708a5266d01609243155.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/1bd5ad6e8416d98080cb4a48.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/3c6d55fbb2fb43169d0508ca20a4462309f7d36c.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/faedab64034f78f0daf3664a79310a55b2191c8a.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/2fdda3cc7cd98d10b05af088213fb80e7aec90f9.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/b8014a90f603738d9536f39bb31bb051f819ec0f.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/2fdda3cc7cd98d10b05af088213fb80e7aec90f9.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/f603918fa0ec08fabf7a641659ee3d6d55fbda0d.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/43a7d933c895d143d011bf9273f082025aaf071f.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/63d0f703918fa0ec2ebf584b269759ee3d6ddb7f.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/5ab5c9ea15ce36d31ed8387f3af33a87e850b1a5.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/8601a18b87d6277f6e46217628381f30e924fc2c.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/b48f8c54acf9964c3a29350e.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/bd3eb13533fa828b48da6aabfd1f4134960a5af9.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/29381f30e924b899da3ce5706e061d950a7bf672.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/bd3eb13533fa828b48da6aabfd1f4134960a5af9.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/4bed2e738bd4b31cd73d63fd87d6277f9e2ff877.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/caef76094b36acaf92b619b87cd98d1001e99c24.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/8435e5dde71190efd6154d95ce1b9d16fcfa608a.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/b3de9c824ba1d4cd6d81190f.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/e0fe9925cc2c683834a80f11.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/0bd162d9f2d3572c65911a988a13632762d0c307.jpg",
				"http://hiphotos.baidu.com/baidu/pic/item/ac6eddc451da81cb2ac1708a5266d01609243155.jpg", };
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error).resetViewBeforeLoading()
				.cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		imageLoader.init(ImageLoaderConfiguration.createDefault(this));

		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new ImagePagerAdapter(imageUrls));

	}

	private class ImagePagerAdapter extends PagerAdapter {

		private String[] images;
		private LayoutInflater inflater;

		ImagePagerAdapter(String[] images) {
			this.images = images;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public void finishUpdate(View container) {
			
		}

		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = inflater.inflate(R.layout.item_pager_image,
					view, false);
			ImageView imageView = (ImageView) imageLayout
					.findViewById(R.id.image);
			final ProgressBar spinner = (ProgressBar) imageLayout
					.findViewById(R.id.loading);

			imageView.setScaleType(ImageView.ScaleType.CENTER);
			FrameLayout.LayoutParams flParams = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.MATCH_PARENT,
					FrameLayout.LayoutParams.MATCH_PARENT);
			imageView.setLayoutParams(flParams);
			imageView.setOnTouchListener(new MulitPointTouchListener(dWidth, dHeight));

			imageLoader.displayImage(images[position], imageView, options,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							spinner.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							String message = null;
							switch (failReason.getType()) {
							case IO_ERROR:
								message = "Input/Output error";
								break;
							case DECODING_ERROR:
								message = "Image can't be decoded";
								break;
							case NETWORK_DENIED:
								message = "Downloads are denied";
								break;
							case OUT_OF_MEMORY:
								message = "Out Of Memory error";
								break;
							case UNKNOWN:
								message = "Unknown error";
								break;
							}
							Toast.makeText(MainActivity.this, message,
									Toast.LENGTH_SHORT).show();

							spinner.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							spinner.setVisibility(View.GONE);
						}
					});

			((ViewPager) view).addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View container) {
		}
	}

}
