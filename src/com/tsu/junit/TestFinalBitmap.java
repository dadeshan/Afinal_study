package com.tsu.junit;

import android.test.AndroidTestCase;
import android.widget.ImageView;
import net.tsz.afinal.FinalBitmap;

public class TestFinalBitmap extends AndroidTestCase{

	private FinalBitmap finalBitmap;
	/**
	 * �������췽����˽�еģ�������Ȼ�ǵ���ģʽ
	 * 	private FinalBitmap(Context context) {
		mContext = context;
		mConfig = new FinalBitmapConfig(context);
		//���û���·����disk ���ȣ����û��sd������ cache Ŀ¼��
		configDiskCachePath(Utils.getDiskCacheDir(context, "afinalCache").getAbsolutePath());//���û���·��
		
		//�����ں������
		configDisplayer(new SimpleDisplayer());//������ʾ��
		configDownlader(new SimpleDownloader());//����������
		
		// �ɼ� FinalBitmap��ȫ�ֵ�����
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
			//1.���û���·�� 2.���û����С��3.�����ڴ�˫���������4.������� 5.ͼƬ��ʾ�Ĳ���
			//1.�ڴ滺����� һ�� map 2.disk ����һ���ļ���
			//1.�̳߳������� BitmapProcess
			//1.BitmapDisplayConfig �����ʾ
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
			//����Ĭ��ͼƬ
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
