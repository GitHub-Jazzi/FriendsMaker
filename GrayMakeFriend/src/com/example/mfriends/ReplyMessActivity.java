package com.example.mfriends;

import com.example.utils.Constant;
import com.example.utils.HttpEngine;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ReplyMessActivity extends Activity {

	private ImageView backto;
	private TextView TV_username;
	private String id,username,userid;
	private EditText title,content;
	private Button btncancel,btnsend;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reply_mess);
		
		
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reply_mess, menu);
		return true;
	}
	
	
	
	public void init()
	{
		//�Ȼ�ȡ��ת����ǰ���棬������������
		Intent intent=this.getIntent();
		id = Constant.getUserId();
		username = intent.getStringExtra("username");
		userid=intent.getStringExtra("userid");
		TV_username=(TextView) findViewById(R.id.username);
		TV_username.setText(username);
		title=(EditText) findViewById(R.id.title);
		content=(EditText) findViewById(R.id.content);
		//��ȡ���ذ�ť��ʱ��
		backto = (ImageView) findViewById(R.id.backto);
		backto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btncancel = (Button) findViewById(R.id.btncancel);
		btncancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnsend = (Button) findViewById(R.id.btnsend);
		btnsend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RequestParams params= new RequestParams();
				params.add("senderId",id);
				params.add("userid",userid);
				params.add("username",username);
				params.add("msgTitle",title.getText().toString());	
				params.add("msgContent",content.getText().toString());	
				HttpEngine.getHttpEngine().post(Constant.sendMessageById, params, new AsyncHttpResponseHandler(){
					@Override
				    public void onSuccess(String result) {
						// �رս�����
						//ProgressDialogUtils.dismissProgressDialog();
						 if (result != null) {
							 Toast.makeText(ReplyMessActivity.this, "���ͳɹ�",Toast.LENGTH_SHORT).show();
							 finish();
							 
							 
						 } else {
								Toast.makeText(ReplyMessActivity.this, "����������",Toast.LENGTH_SHORT).show();
							}						 
					
				    }
					
				     @Override
		             public void onFailure(Throwable error) {
				    	// �رս�����
							//ProgressDialogUtils.dismissProgressDialog();
		                     error.printStackTrace();
		                     Toast.makeText(ReplyMessActivity.this, "����ʱ,��������!",Toast.LENGTH_SHORT).show();
		             }
					
				});
				
			}
		});
	}

}
