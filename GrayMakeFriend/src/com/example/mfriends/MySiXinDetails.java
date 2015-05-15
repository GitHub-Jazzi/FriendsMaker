package com.example.mfriends;

import java.text.SimpleDateFormat;

import com.alibaba.fastjson.JSON;
import com.example.model.MessageEntity;
import com.example.model.ResultForm;
import com.example.utils.Constant;
import com.example.utils.DownImage;
import com.example.utils.HttpEngine;
import com.example.utils.JSONUtils;
import com.example.utils.ProgressDialogUtils;
import com.example.utils.DownImage.ImageCallBack;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MySiXinDetails extends Activity {

	private Button btreply;
	private ImageView backto;
	private MessageEntity message;
	private ImageView userimage;
	private TextView username,useremail,userintro,title,sendtime,content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_si_xin_details);
		
		init();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_si_xin_details, menu);
		return true;
	}

	
	@SuppressLint("SimpleDateFormat")
	public void init(){
		Intent intent = this.getIntent(); 
		message=(MessageEntity)intent.getSerializableExtra("sixin.message");
		boolean status=message.getMsgStatus();
		if(!status)//δ��
		{
			readMessage();
			
		}
		
		//��ȡ���ذ�ť��ʱ��
		backto = (ImageView) findViewById(R.id.backto);
		
		username=(TextView)findViewById(R.id.sxusername);
		//useremail=(TextView) findViewById(R.id.useremail);
		//userintro=(TextView) findViewById(R.id.userintro);
		title=(TextView) findViewById(R.id.sxtheme);//����
		sendtime=(TextView) findViewById(R.id.sxsendtime);//����ʱ��
		content=(TextView) findViewById(R.id.sxcontent);//��������
		userimage=(ImageView) findViewById(R.id.imageViewsx);//�û�ͷ��
		
		username.setText(String.valueOf(message.getUsername()));
		title.setText(String.valueOf(message.getMsgTitle()));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		java.util.Date date=message.getSendTime(); 
		
		String str=""; 
		if(date!=null)
		{
			str=sdf.format(date);
		}
		sendtime.setText(String.valueOf(str));
		content.setText(String.valueOf(message.getMsgContent()));
		DownImage downImage = new DownImage(message.getUserImg());
		downImage.loadImage(new ImageCallBack() {
			public void getDrawable(Drawable drawable) {

				// TODO Auto-generated method stub

				userimage.setImageDrawable(drawable);
			}
		});
		
		backto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		//��ȡ�ظ�˽�ŵİ�ť�¼�
		btreply = (Button)findViewById(R.id.buttonrep);
		
		btreply.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//String userId=getIntent().getStringExtra("id");
				//�����ת���ظ�����
				Intent intentsx = new Intent(MySiXinDetails.this,ReplyMessActivity.class);
				//intentsx.putExtra("id", userId);
				intentsx.putExtra("userid", String.valueOf(message.getUserid()));
				intentsx.putExtra("username", message.getUsername());
				
				startActivity(intentsx);
				
			}
		});
	}
	
	public void readMessage(){//�ı�˽��״̬Ϊ�Ѷ�
		RequestParams params= new RequestParams();
		params.add("msgId",String.valueOf(message.getId()));  
		HttpEngine.getHttpEngine().get(Constant.readMessageById, params, new AsyncHttpResponseHandler(){
			@Override
		    public void onSuccess(String result) {
				 if (result != null) {					
					 ResultForm re= JSONUtils.jsonToEntity(result,ResultForm.class); 
					 String info = JSON.parseObject(result).getString("info");
					 String infoText = JSON.parseObject(result).getString("infoText");
					// �㲥֪ͨ  
			         Intent intent = new Intent();  
			         intent.setAction("action.refreshMessage");  
			         sendBroadcast(intent); 
				 } 
		    }						
		     @Override
             public void onFailure(Throwable error) {
		    	// �رս�����
					//ProgressDialogUtils.dismissProgressDialog();
                     error.printStackTrace();

                   Toast.makeText(MySiXinDetails.this, "����ʱ,��������!",Toast.LENGTH_SHORT).show();
             }
		});	
	}
	
	
	
}
