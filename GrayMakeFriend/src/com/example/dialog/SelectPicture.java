package com.example.dialog;


import java.text.MessageFormat;

import com.example.mfriends.ModifyInfoActivity;
import com.example.mfriends.R;
import com.example.mfriends.SaveDate;
import com.tencent.weibo.oauthv2.OAuthV2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

public class SelectPicture extends PopupWindow {

	/***
	 * ʹ����������ջ�ȡͼƬ
	 */
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	/***
	 * ʹ������е�ͼƬ
	 */
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
	
	/***
	 * ��Intent��ȡͼƬ·����KEY
	 */
	public static final String KEY_PHOTO_PATH = "photo_path";
	/**
	 * ��ȡ��Ƭ·��
	 */
	private static final int PHOTO_URL=103;
	/**��ȡ����ͼƬ·��*/
	private String picPath;
	
	private Intent lastIntent ;
	
	private Uri photoUri;
	
	
	private Button  btn_cancel;
	private View mMenuView;
	private LinearLayout my_photo,my_album,my_file;
	private Handler mHandler;

	public SelectPicture(final Activity context,Handler handler) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.selectpic_dialog, null);
		mHandler=handler;
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
/*		btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
		//ȡ����ť
		btn_cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//���ٵ�����
				SaveDate.saveDate(context, new OAuthV2()); 
				context.finish();
			}
		});*/
		my_photo = (LinearLayout) mMenuView.findViewById(R.id.my_photo);
		my_photo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i("SelectPicPopupWindow", "����");
				takePhoto(context);
				dismiss();
			}
		});
		my_album = (LinearLayout) mMenuView.findViewById(R.id.my_album);
		my_album.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i("SelectPicPopupWindow", "�������");
				pickPhoto(context);
/*				Handler handler=(Handler) mMenuView.getTag();
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg); */
				dismiss();
			}
		});
		
/*		my_file = (LinearLayout) mMenuView.findViewById(R.id.my_file);
		my_file.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i("SelectPicPopupWindow", "�����ļ�");
				dismiss();
			}
		});*/
		//���ð�ť����
		//����SelectPicPopupWindow��View
		this.setContentView(mMenuView);
		//����SelectPicPopupWindow��������Ŀ�
		this.setWidth(w/2+50);
		//����SelectPicPopupWindow��������ĸ�
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//����SelectPicPopupWindow��������ɵ��
		this.setFocusable(true);
		//����SelectPicPopupWindow�������嶯��Ч��
		this.setAnimationStyle(R.style.mystyle);
		//ʵ����һ��ColorDrawable��ɫΪ��͸��
		ColorDrawable dw = new ColorDrawable(000000);
		//����SelectPicPopupWindow��������ı���
		this.setBackgroundDrawable(dw);
		//mMenuView���OnTouchListener�����жϻ�ȡ����λ�������ѡ������������ٵ�����
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}				
				return true;
			}
		});

	}
	
	/**
	 * ���ջ�ȡͼƬ
	 */
	private void takePhoto(Activity context) {
		//ִ������ǰ��Ӧ�����ж�SD���Ƿ����
		String SDState = Environment.getExternalStorageState();
		if(SDState.equals(Environment.MEDIA_MOUNTED))
		{
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
			/***
			 * ��Ҫ˵��һ�£����²���ʹ����������գ����պ��ͼƬ����������е�
			 * ����ʹ�õ����ַ�ʽ��һ���ô����ǻ�ȡ��ͼƬ�����պ��ԭͼ
			 * �����ʵ��ContentValues�����Ƭ·���Ļ������պ��ȡ��ͼƬΪ����ͼ������
			 */
			ContentValues values = new ContentValues();  
			photoUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);  
			Message msg=new Message();
			msg.what=PHOTO_URL;
			msg.obj=photoUri;
			mHandler.sendMessage(msg);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			/**-----------------*/
			context.startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
		}else{
			Toast.makeText(context,"�ڴ濨������", Toast.LENGTH_LONG).show();
		}
	}

	/***
	 * �������ȡͼƬ
	 */
	private void pickPhoto(Activity context) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		context.startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
	}
	
}
