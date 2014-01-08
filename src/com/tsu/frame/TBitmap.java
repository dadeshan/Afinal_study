package com.tsu.frame;

import android.content.Context;


/**
 * 
* @Project: AfinalExceise
* @PackagName: com.tsu.frame
* @ClassName: TBitmap
* @Description: TODO
* @author tadeshanc@gmail.com
* @date 2014-1-7
 */
public class TBitmap {

	private Context context;
	private String cacheDir;
	//1. MemoryCache
	//2. DiskCache
	//3. download
	//4. display
	//5. BitmapHandler
	//5. config etc.
	
	
	/**
	 * 1. config 
	 * 2. work
	 */
	
	
	//
	public TBitmap(Context context){
		this.context = context;
		ImageCacheParams cacheParams = new ImageCacheParams(context,cacheDir);
		ImageCache cache = new ImageCache(cacheParams);
		ImageWork ImageWork = ImageWork();//传入别的
		ImageWork.setImageCache(cache);
	}
	
	
	private class ImageCacheParams{

		public ImageCacheParams(Context context, String cacheDir) {
			// TODO Auto-generated constructor stub
		}
		
	}
	
	private class ImageCache{
		
	}
	
	private class ImageWork{
		
	}
	
}
