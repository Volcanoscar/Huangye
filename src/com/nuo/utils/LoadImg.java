package com.nuo.utils;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.nuo.net.DownBitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

/**

 * */
public class LoadImg {

	//����ͼƬ������߳���
	private static final int Max = 5;
	//ͼƬ��һ������,���������ǳ����ڲ�
	private Map<String,SoftReference<Bitmap>> imageCaches = null;

	//�鿴���ػ��湤����
	private FileUtiles fileUtiles;
	//android �ṩ�����ǵ�һ���̳߳�,ʹ�÷���
	private ExecutorService threadPools = null;

	//��ʼ���������صı���
	public LoadImg(Context ctx){
		imageCaches = new HashMap<String, SoftReference<Bitmap>>();
		fileUtiles = new FileUtiles(ctx);
	}

	//����ͼƬʱ�����
	public Bitmap loadImage(final ImageView imageView,
			final String imageUrl,
			final ImageDownloadCallBack imageDownloadCallBack){
		//imageUrl ������Ψһ�ͣ�������Ϊ����map���е�key
		//ͼƬ����
		final String filename = imageUrl.substring(imageUrl.lastIndexOf("/")+1,
				imageUrl.length());
		//ͼƬ���浽����ʱ�ĵ�ַ
		String filepath = fileUtiles.getAbsolutePath()+"/"+filename;
		//����һ�����棬�����Ƿ�������ͼƬ
		//���map���������key����һ��true
		if(imageCaches.containsKey(imageUrl)){
			//�ҵ���ӦͼƬ�����õķ�װ
			SoftReference<Bitmap> soft = imageCaches.get(imageUrl);
			//�������õ��л�ȡͼƬ
			Bitmap bit = soft.get();
			if(bit != null)
				return bit;
			//�����ǵ�һ�����棨�����ڲ���ȡͼƬ��
		}
		//�Ӷ������浱�л�ȡͼƬ
		if(fileUtiles.isBitmap(filename)){
			Bitmap bit = BitmapFactory.decodeFile(filepath);
			//�ڶ��������ȡ��ʱ��ֱ����ӵ�һ�����浱��
			imageCaches.put(imageUrl, new SoftReference<Bitmap>(bit));
			return bit;
		}

		//һ�����棬�������涼�����ڣ�ֱ�ӵ��������
		if(imageUrl != null && !imageUrl.equals("")){
			if(threadPools == null){
				//ʵ�������ǵ��̳߳�
				threadPools = Executors.newFixedThreadPool(Max);
			}

			//���ػ�ͼƬ�ص�Handler
			final Handler hand = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					//���ͼƬ���سɹ������һص�����Ϊ��ʱ
					if(msg.what == 111 && 
							imageDownloadCallBack != null){
						Bitmap bit = (Bitmap) msg.obj;
						//���ûص��Զ����������Ľӿڷ�����������
						imageDownloadCallBack.onImageDownload(imageView, bit);
					}
					super.handleMessage(msg);
				}
			};

			//����ͼƬ�߳�
			Thread thread = new Thread(){
				public void run(){
					//��������ʱ���ֽ���
					InputStream inputStream = DownBitmap.
							getInstance().
							getInputStream(imageUrl);
					//ͼƬѹ��Ϊԭ����һ��
					BitmapFactory.Options op = new BitmapFactory.Options();
					op.inSampleSize = 2;
					Bitmap bit = BitmapFactory.decodeStream(inputStream, null, op);
					if(bit != null){
						//��ӵ�һ�����浱��
						imageCaches.put(imageUrl,new SoftReference<Bitmap>(bit));
						//��ӵ���������
						fileUtiles.saveBitmap(filename, bit);
						//���ݸ�Handler
						Message msg = hand.obtainMessage();
						msg.what = 111;
						msg.obj = bit;
						hand.sendMessage(msg);
					}
				}
			};

			threadPools.execute(thread);
		}

		return null;
	}


	//ͨ���ص���������ͼƬʱ�Ľӿ�(������Button��Onclick)
	public interface ImageDownloadCallBack{
		//ImageView ������Ҫ�趨��imageview Bitmap ��Ҫ�趨��ͼƬ
		void onImageDownload(ImageView imageView,Bitmap bitmap);
	}

}
