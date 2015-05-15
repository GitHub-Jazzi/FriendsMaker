package com.example.mfriends;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dialog.SelectBirthday;
import com.example.dialog.SelectHeight;
import com.example.dialog.SelectPicture;
import com.example.dialog.SelectText;
import com.example.utils.Constant;
import com.example.utils.DownImage;
import com.example.utils.UploadUtil;
import com.example.utils.DownImage.ImageCallBack;
import com.example.utils.UploadUtil.OnUploadProcessListener;

import com.tencent.weibo.oauthv2.OAuthV2;


public class AlbumActivity extends Activity implements OnUploadProcessListener{
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
	 * ȥ�ϴ��ļ�
	 */
	protected static final int TO_UPLOAD_FILE = 1;  
	/**
	 * ��ȡ��Ƭ·��
	 */
	private static final int PHOTO_URL=103;

	/**��ȡ����ͼƬ·��*/
	private String picPath;
		
	private Uri photoUri;
	
	//�˵�����ť
	private TextView back;
	private ImageView add;
	


	//�Զ���ı�������
	SelectPicture selectPicture;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.album_info);
		

	    back = (TextView) findViewById(R.id.back_btn);
		add = (ImageView) findViewById(R.id.add_btn);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
				//uploadImage(ModifyInfoActivity.this);
			}
		});
		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
						showPictureDialog(AlbumActivity.this,add);
	
				
				
				
				//uploadImage2(ModifyInfoActivity.this);
			}
		});

		

	}

	
	public void showPictureDialog(final Activity context,View view){
		selectPicture = new SelectPicture(context,handler);
		// ��ʾ����
		View showview = view;
		// ���������ƫ����
		int xoffInPixels = selectPicture.getWidth() - showview.getWidth() + 10;
		selectPicture.showAsDropDown(showview, -xoffInPixels, 0);
		
	};

	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK)
		{
			doPhoto(requestCode,data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * ѡ��ͼƬ�󣬻�ȡͼƬ��·��
	 * @param requestCode
	 * @param data
	 */
	private void doPhoto(int requestCode,Intent data)
	{
		if(requestCode == SELECT_PIC_BY_PICK_PHOTO )  //�����ȡͼƬ����Щ�ֻ����쳣�������ע��
		{
			if(data == null)
			{
				Toast.makeText(this, "ѡ��ͼƬ�ļ�����", Toast.LENGTH_LONG).show();
				return;
			}
			photoUri = data.getData();
			if(photoUri == null )
			{
				Toast.makeText(this, "ѡ��ͼƬ�ļ�����", Toast.LENGTH_LONG).show();
				return;
			}
		}
		String[] pojo = {MediaStore.Images.Media.DATA};
		Cursor cursor = managedQuery(photoUri, pojo, null, null,null);   
		if(cursor != null )
		{
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			picPath = cursor.getString(columnIndex);
			cursor.close();
		}
		Log.i("AlbumActivity", "imagePath = "+picPath);
		if(picPath != null && ( picPath.endsWith(".png") || picPath.endsWith(".PNG") ||picPath.endsWith(".jpg") ||picPath.endsWith(".JPG")  ))
		{
			Log.i("AlbumActivity", "����ѡ���ͼƬ="+picPath);
			Bitmap bm = BitmapFactory.decodeFile(picPath);
			/*��Ӵ���ͼƬ���� begin*/
			
			//imageView.setImageBitmap(bm);
			/*��Ӵ���ͼƬ���� end*/
/*			lastIntent.putExtra(KEY_PHOTO_PATH, picPath);
			setResult(Activity.RESULT_OK, lastIntent);
			finish();*/
		}else{
			Toast.makeText(this, "ѡ��ͼƬ�ļ�����ȷ", Toast.LENGTH_LONG).show();
		}
	}
	private void toUploadFile()
	{
		//uploadImageResult.setText("�����ϴ���...");
		//progressDialog.setMessage("�����ϴ��ļ�...");
		//progressDialog.show();
		String fileKey = "img";
		UploadUtil uploadUtil = UploadUtil.getInstance();;
		uploadUtil.setOnUploadProcessListener(this);  //���ü����������ϴ�״̬
		
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("orderId", "11111");
		params.put("uploadNum", "1");
		File file = new File(picPath);
		params.put("filename",uploadUtil.generateFileName()+file.getName() );
		uploadUtil.uploadFile( picPath,fileKey, Constant.uploadImage,params);
	}
	/**
	 * �ϴ���������Ӧ�ص�
	 */
	@Override
	public void onUploadDone(int responseCode, String message) {
		//progressDialog.dismiss();
		Message msg = Message.obtain();
		//msg.what = UPLOAD_FILE_DONE;
		msg.arg1 = responseCode;
		msg.obj = message;
		
		String imageUrl=message;
		//updateUserImage(imageUrl);
		//handler.sendMessage(msg);
	}
	private Handler handler = new Handler(){//δʹ��
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TO_UPLOAD_FILE:
				toUploadFile();
				break;
			case 10:
				Toast.makeText(AlbumActivity.this, "�ϴ��ɹ�",Toast.LENGTH_SHORT).show();
				break;
			case 11:
				Toast.makeText(AlbumActivity.this, "�ϴ�ʧ��",Toast.LENGTH_SHORT).show();
				break;
			case 12:
				Toast.makeText(AlbumActivity.this, "����ʱ,��������!",Toast.LENGTH_SHORT).show();
				break;

			case PHOTO_URL:
				picPath=msg.obj.toString();
				Toast.makeText(AlbumActivity.this, picPath,Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	@Override
	public void onUploadProcess(int uploadSize) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void initUpload(int fileSize) {
		// TODO Auto-generated method stub
		
	}
}
