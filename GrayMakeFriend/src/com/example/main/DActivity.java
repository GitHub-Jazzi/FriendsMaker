package com.example.main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.example.dialog.SelectPicture;
import com.example.mfriends.ConditionActivity;
import com.example.mfriends.ConfigActivity;
import com.example.mfriends.LifeActivity;
import com.example.mfriends.MainActivity;
import com.example.mfriends.ModifyInfoActivity;
import com.example.mfriends.OnViewChangeListener;
import com.example.mfriends.R;
import com.example.model.UserInfo;
import com.example.utils.Constant;
import com.example.utils.DownImage;
import com.example.utils.HandlerConstants;
import com.example.utils.HttpEngine;
import com.example.utils.ImageLoader;
import com.example.utils.JSONUtils;
import com.example.utils.ProgressDialogUtils;
import com.example.utils.UploadUtil;
import com.example.utils.DownImage.ImageCallBack;
import com.example.utils.UploadUtil.OnUploadProcessListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DActivity extends Activity implements OnUploadProcessListener{
	//�������Ŀؼ�����Ϣ���� begin --------------------------------------added by mrxu 3-13	
		private UserInfo userinfo=new UserInfo();
		LinearLayout myalbum_btn;
		LinearLayout condition_btn;
		LinearLayout userinfo_btn;
		LinearLayout config_btn;
		SelectPicture selectPicture;
		private TextView Center_TV_username;
		private TextView Center_TV_email;
		private TextView Center_TV_intro;
		private ImageView avatar;
		private ImageLoader imageLoader;
		
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

		/**��ȡ����ͼƬ·��*/
		private String picPath;
			
		private Uri photoUri;
		
		/**
		 * ��ȡ��Ƭ·��
		 */
		private static final int PHOTO_URL=103;
		 public static final int  EXIT_APPLICATION = 0x0001;  
		//�������Ŀؼ�����Ϣ���� end  --------------------------------------added by mrxu 3-13
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	/*	TextView tv = new TextView(this);
		tv.setText("This is D Activity!");
		tv.setGravity(Gravity.CENTER);*/
		setContentView(R.layout.individualcenter);
		//�������Ŀؼ��¼����� begin ---added by mrxu 3-13
		myalbum_btn=(LinearLayout)findViewById(R.id.myalbum_btn);
		condition_btn=(LinearLayout)findViewById(R.id.condition_btn);
		userinfo_btn=(LinearLayout)findViewById(R.id.userinfo_btn);
		config_btn=(LinearLayout)findViewById(R.id.config_btn);				
		avatar=(ImageView)findViewById(R.id.avatar);
		Center_TV_username=(TextView)findViewById(R.id.Center_TV_username);
		Center_TV_email=(TextView)findViewById(R.id.Center_TV_email);
		Center_TV_intro=(TextView)findViewById(R.id.Center_TV_intro);
        
		
		 imageLoader = new ImageLoader(DActivity.this);
		
		
		myalbum_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
			 	Intent intent=new Intent();
            	intent.setClass(DActivity.this,LifeActivity.class);
            	startActivity(intent);
			}
		});
		condition_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
			 	Intent intent=new Intent();
            	intent.setClass(DActivity.this,ConditionActivity.class);
            	startActivity(intent);
			}
		});
		userinfo_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
			 	Intent intent=new Intent();
            	intent.setClass(DActivity.this,ModifyInfoActivity.class);
            	startActivity(intent);
			}
		});
		config_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
			 	Intent intent=new Intent();
            	intent.setClass(DActivity.this,ConfigActivity.class);
            	startActivity(intent);
			}
		});


		avatar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showPictureDialog(DActivity.this, avatar);
				//finish();
				//uploadImage(ModifyInfoActivity.this);
			}
		});

		//�������Ŀؼ��¼����� end
		
		
		httpGetUserinfo();//��ʼ���û����˻�����Ϣ	       
		
		
	}
	
	
	/*------------------------------------------------------------------------------------------------------------------�����������÷��� begin             added by mrxu*/
	/**��ʾͷ���޸Ĳ˵�
	 * @param context
	 * @param view
	 */
	public void showPictureDialog(final Activity context,View view){
		selectPicture = new SelectPicture(context,handler);
		// ��ʾ����
		View showview = view;
		// ���������ƫ����
		int xoffInPixels = selectPicture.getWidth() - showview.getWidth() + 10;
		selectPicture.showAsDropDown(showview, xoffInPixels, 0);
		
	};
	
	
	/**ѡ��ͼƬ�󷵻ش���
	 * @param context
	 * @param view
	 */
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
		if(requestCode == SELECT_PIC_BY_PICK_PHOTO)  //�����ȡͼƬ����Щ�ֻ����쳣�������ע��
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
		//photoUri =Uri.parse(data.getStringExtra(android.provider.MediaStore.EXTRA_OUTPUT));//added by mrxu 3-19
		String[] pojo = {MediaStore.Images.Media.DATA};
		Cursor cursor = managedQuery(photoUri, pojo, null, null,null);   
		if(cursor != null )
		{
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			picPath = cursor.getString(columnIndex);
			
			
			try  
            {  
                //4.0���ϵİ汾���Զ��ر� (4.0--14;; 4.0.3--15)  
                if(Integer.parseInt(Build.VERSION.SDK) < 14)  
                {  
                    cursor.close();  
                }  
            }catch(Exception e)  
            {  
                Log.i("DActivity", "error:" + e);  
            }  
			
			//cursor.close();
		}
		Log.i("AlbumActivity", "imagePath = "+picPath);
		if(picPath != null && ( picPath.endsWith(".png") || picPath.endsWith(".PNG") ||picPath.endsWith(".jpg") ||picPath.endsWith(".JPG")  ))
		{
			Log.i("AlbumActivity", "����ѡ���ͼƬ="+picPath);
			Bitmap bm = BitmapFactory.decodeFile(picPath);
			/*��Ӵ���ͼƬ���� begin*/
			
			avatar.setImageBitmap(bm);
			if(picPath!=null)
			{
				//handler.sendEmptyMessage(TO_UPLOAD_FILE);
				toUploadFile();
			}else{
				Toast.makeText(this, "�ϴ����ļ�·������", Toast.LENGTH_LONG).show();
			}
			
			/*��Ӵ���ͼƬ���� end*/
/*			lastIntent.putExtra(KEY_PHOTO_PATH, picPath);
			setResult(Activity.RESULT_OK, lastIntent);
			finish();*/
		}else{
			Toast.makeText(this, "ѡ��ͼƬ�ļ�����ȷ", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * ��ȡ�û�������Ϣ����ʾ
	 */
	public void httpGetUserinfo(){
		//ProgressDialogUtils.showProgressDialog(this, "���ݼ�����...");
		RequestParams params= new RequestParams();
		params.add("id",Constant.UserId);   
		HttpEngine.getHttpEngine().get(Constant.getBaseInfoById, params, new AsyncHttpResponseHandler(){
			@Override
		    public void onSuccess(String result) {
				// �رս�����
			//	ProgressDialogUtils.dismissProgressDialog();
				 if (result != null) {
					 //String info = JSON.parseObject(result).get("")
					//����ת����java bean
				       UserInfo re= JSONUtils.jsonToEntity(result,UserInfo.class);
				if(re.getUsername()!=null){/*Center_TV_username.setText(re.getUsername());*/userinfo.setUsername(re.getUsername());	}
				if(re.getEmail()!=null){/*Center_TV_email.setText(re.getEmail());*/userinfo.setEmail(re.getEmail());}
				if(re.getIntro()!=null){/*Center_TV_intro.setText(re.getIntro());*/userinfo.setIntro(re.getIntro());}
				//Toast.makeText(MainActivity.this, re.getUsername()+re.getEmail()+re.getIntro(),Toast.LENGTH_SHORT).show();
				// �ӿڻص��ķ��������ͼƬ�Ķ�ȡ;
				if(re.getUserImg()!=null){userinfo.setUserImg(re.getUserImg());}
	/*			DownImage downImage = new DownImage(re.getUserImg());
				downImage.loadImage(new ImageCallBack() {
					public void getDrawable(Drawable drawable) {

						// TODO Auto-generated method stub

						avatar.setImageDrawable(drawable);

					}

				});*/
                Message msg=new Message();
                msg.what=HandlerConstants.GET_USER_INFO_OK;
                msg.obj= "��ȡ���ݳɹ�!"+userinfo.getUsername()+userinfo.getEmail()+userinfo.getIntro()+"ҳ��|ԭʼ"+re.getUsername()+re.getEmail()+re.getIntro();
               // msg.obj= "��ȡ���ݳɹ�!"+userinfo.getUserImg();
                handler.sendMessage(msg);
				
				 } else {
	                   Message msg=new Message();
	                     msg.what=HandlerConstants.GET_USER_INFO_ERROR;
	                     msg.obj= "��ȡ���ݴ���!";
	                     handler.sendMessage(msg);
					 
						//Toast.makeText(MainActivity.this, "��ȡ���ݴ���",Toast.LENGTH_SHORT).show();
					}
		    }
			
		     @Override
             public void onFailure(Throwable error) {
		    	 if(Constant.Debug){
						userinfo.setUsername("mrxu");	
						userinfo.setEmail("mrxu@163.com");
						userinfo.setIntro("���˼��");
						userinfo.setUserImg("");
		    	 }
		    	// �رս�����
					//ProgressDialogUtils.dismissProgressDialog();
                     error.printStackTrace();
                     Message msg=new Message();
                     msg.what=HandlerConstants.GET_USER_INFO_ERROR;
                     msg.obj= "����ʱ,��������!";
                     handler.sendMessage(msg);
                    // Toast.makeText(MainActivity.this, "����ʱ,��������!",Toast.LENGTH_SHORT).show();
             }
		});
		
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
		updateUserImage(imageUrl);
		//handler.sendMessage(msg);
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
	
	
	//�߳�ִ�н����󷵻���Ϣ
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){//δʹ��
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HandlerConstants.TO_UPLOAD_FILE:
				toUploadFile();
				break;
			
			case HandlerConstants.UPLOAD_INIT_PROCESS:
				//progressBar.setMax(msg.arg1);
				break;
			case HandlerConstants.UPLOAD_IN_PROCESS:
				//progressBar.setProgress(msg.arg1);
				break;
			case HandlerConstants.UPLOAD_FILE_DONE:
				String result=(String) msg.obj;
				Log.i("*********", result);
				break;
			case 10:
				Toast.makeText(DActivity.this, "�ϴ��ɹ�",Toast.LENGTH_SHORT).show();
 				// �㲥֪ͨ  
		         Intent intent = new Intent();  
		         intent.setAction("action.refresh");  
		         sendBroadcast(intent); 
				break;
			case 11:
				Toast.makeText(DActivity.this, "�ϴ�ʧ��",Toast.LENGTH_SHORT).show();
				break;
			case 12:
				Toast.makeText(DActivity.this, "����ʱ,��������!",Toast.LENGTH_SHORT).show();
				break;
			case HandlerConstants.GET_USER_INFO_OK:
				Center_TV_username.setText(userinfo.getUsername());
			    Center_TV_email.setText(userinfo.getEmail());
				Center_TV_intro.setText(userinfo.getIntro());
				
				/*String url = userinfo.getUserImg();
				imageLoader.DisplayImage(url,avatar);*/
				DownImage downImage = new DownImage(userinfo.getUserImg());
				downImage.loadImage(new ImageCallBack() {
					public void getDrawable(Drawable drawable) {

						// TODO Auto-generated method stub

						avatar.setImageDrawable(drawable);

					}
				});
			//	Toast.makeText(DActivity.this, msg.obj.toString(),Toast.LENGTH_SHORT).show();
			break;
			case HandlerConstants.GET_USER_INFO_ERROR:
				Center_TV_username.setText(userinfo.getUsername());
			    Center_TV_email.setText(userinfo.getEmail());
				Center_TV_intro.setText(userinfo.getIntro());
				//Toast.makeText(MainActivity.this, msg.obj.toString(),Toast.LENGTH_SHORT).show();
				break;

			case PHOTO_URL:
				photoUri=(Uri) msg.obj;
				Toast.makeText(DActivity.this, photoUri.toString(),Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};

	@Override
	public void onUploadProcess(int uploadSize) {
		Message msg = Message.obtain();
		msg.what = HandlerConstants.UPLOAD_IN_PROCESS;
		msg.arg1 = uploadSize;
		//handler.sendMessage(msg );
	}

	@Override
	public void initUpload(int fileSize) {
		Message msg = Message.obtain();
		msg.what = HandlerConstants.UPLOAD_INIT_PROCESS;
		msg.arg1 = fileSize;
		//handler.sendMessage(msg );
	}
	public void updateUserImage(String imageUrl){//�����û�ͷ��
		/*UserInfo user=new UserInfo();
		user.setId(userinfo.getId());
		user.setUserImg(imageUrl);*/		
		//ProgressDialogUtils.showProgressDialog(this, "�����ϴ�ͷ��...");
		RequestParams params= new RequestParams();
		params.add("id",Constant.getUserId()); 
		params.add("userImg",imageUrl);  
		HttpEngine.getHttpEngine().get(Constant.updateUserImage, params, new AsyncHttpResponseHandler(){
			@Override
		    public void onSuccess(String result) {
				// �رս�����
				ProgressDialogUtils.dismissProgressDialog();
				 if (result != null) {					
					//����ת����java bean
					 Message msg = Message.obtain();
						msg.what = 10;
					 handler.sendMessage(msg);
					// Toast.makeText(MainActivity.this, "�ϴ��ɹ�",Toast.LENGTH_SHORT).show();
				 } else {
					 Message msg = Message.obtain();
						msg.what = 11;
					 handler.sendMessage(msg);
						//Toast.makeText(MainActivity.this, "��ȡ���ݴ���",Toast.LENGTH_SHORT).show();
					}
		    }						
		     @Override
             public void onFailure(Throwable error) {
		    	// �رս�����
					//ProgressDialogUtils.dismissProgressDialog();
                     error.printStackTrace();
                     Message msg = Message.obtain();
						msg.what = 12;
					 handler.sendMessage(msg);
                   //  Toast.makeText(MainActivity.this, "����ʱ,��������!",Toast.LENGTH_SHORT).show();
             }
		});	
	}


	
	
	
	/*-------------------------------------------------------------------------------------------------------------------------------�����������÷��� end*/
}
