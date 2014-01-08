package com.tsu.junit;

import android.test.AndroidTestCase;
import android.widget.ImageView;
import net.tsz.afinal.FinalBitmap;

public class TestFinalBitmap extends AndroidTestCase{

	private FinalBitmap finalBitmap;
	/**
	 * 看看构造方法：私有的，看来必然是单例模式
	 * 	private FinalBitmap(Context context) {
		mContext = context;
		mConfig = new FinalBitmapConfig(context);
		//配置缓存路径，disk 优先，如果没有sd卡则在 cache 目录下
		configDiskCachePath(Utils.getDiskCacheDir(context, "afinalCache").getAbsolutePath());//配置缓存路径
		
		//我们在后面分析
		configDisplayer(new SimpleDisplayer());//配置显示器
		configDownlader(new SimpleDownloader());//配置下载器
		
		// 可见 FinalBitmap是全局单例的
		public static synchronized FinalBitmap create(Context ctx){
		if(mFinalBitmap == null){
			mFinalBitmap = new FinalBitmap(ctx.getApplicationContext());
		}
		return mFinalBitmap;
	}
	
	}
	 * 
	 */
	
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		finalBitmap = FinalBitmap.create(getContext());
	}
	

	
	
	/**
	 * private void doDisplay(View imageView, String uri, BitmapDisplayConfig displayConfig) {
		if(!mInit ){
			//1.配置缓存路径 2.配置缓存大小，3.磁盘内存双缓存的启用4.缓存个数 5.图片显示的参数
			//1.内存缓存就是 一个 map 2.disk 就是一个文件夹
			//1.线程池中运行 BitmapProcess
			//1.BitmapDisplayConfig 如何显示
			init();
		}
		
		if (TextUtils.isEmpty(uri) || imageView == null) {
			return;
		}
		
		if(displayConfig == null)
			displayConfig = mConfig.defaultDisplayConfig;
	
		Bitmap bitmap = null;
	
		if (mImageCache != null) {
			bitmap = mImageCache.getBitmapFromMemoryCache(uri);
		}
	
		if (bitmap != null) {
			if(imageView instanceof ImageView){
				((ImageView)imageView).setImageBitmap(bitmap);
			}else{
				imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
			}
			
			
		}else if (checkImageTask(uri, imageView)) {
			final BitmapLoadAndDisplayTask task = new BitmapLoadAndDisplayTask(imageView, displayConfig );
			//设置默认图片
			final AsyncDrawable asyncDrawable = new AsyncDrawable(mContext.getResources(), displayConfig.getLoadingBitmap(), task);
	       
			if(imageView instanceof ImageView){
				((ImageView)imageView).setImageDrawable(asyncDrawable);
			}else{
				imageView.setBackgroundDrawable(asyncDrawable);
			}
	        
	        task.executeOnExecutor(bitmapLoadAndDisplayExecutor, uri);
	    }
	}
	 * @param view
	 */
	
	
	public void testFinalBitmap(ImageView view){
		
		finalBitmap.display(view, "");
	
	};
	
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		finalBitmap.clearCache();
		finalBitmap = null;
	}
}
