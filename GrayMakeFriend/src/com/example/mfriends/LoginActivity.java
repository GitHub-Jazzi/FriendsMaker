package com.example.mfriends;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.dialog.SelectBirthday;
import com.example.dialog.SelectHeight;
import com.example.dialog.SelectText;
import com.example.main.MainTabActivity;
import com.example.model.ResultForm;
import com.example.model.UserInfo;
import com.example.utils.Constant;
import com.example.utils.HttpEngine;
import com.example.utils.JSONUtils;
import com.example.utils.ProgressDialogUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.weibo.oauthv2.OAuthV2;


public class LoginActivity extends Activity {
	 private EditText username, password;  
	    private CheckBox cb_rem, cb_auto;  
	    private TextView login_btn,register_btn;  
	    private ImageView btnQuit;
	    private String userNameValue,passwordValue;  
	    private SharedPreferences sp;  
	    private Editor editor;
	    private SharedPreferences  share;
	    public void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	       share = getSharedPreferences("perference", MODE_PRIVATE);
			 editor = share.edit();// ȡ�ñ༭��	 
	        
	        
	        
	        
	        
	      //�ж��Ƿ��һ��ʹ��app
		      Boolean user_first = share.getBoolean("FIRST",true);
		      if(user_first){//��һ��
		    	  editor.putBoolean("FIRST", false).commit();
		        	//��һ��ʹ�ã���������
		            new  AlertDialog.Builder(LoginActivity.this)  
		            .setTitle("��ѡ������" )  
		            .setIcon(android.R.drawable.ic_dialog_info)                  
		            .setSingleChoiceItems(new  String[] {"����", "English", "Z"  },  0 ,   
		              new  DialogInterface.OnClickListener() {  
		                 public   void  onClick(DialogInterface dialog,  int  which) {  
		                    
		                    if(which==0){
		                    	editor.putString("language", "cn");// �洢���� ����ѡ������	
		                    }
		                    else if(which==1){
		                    	editor.putString("language", "en");// �洢���� ����ѡ��English
		                    }
		                    else if(which==2){
		                    	editor.putString("language", "ti");// �洢���� ����ѡ��Z
		                    }
		                   
		                    editor.commit();// ������ѡ��������xml
		                    dialog.dismiss(); 
		                    setlanguage();
		                 }  
		              }  
		            ).show();   
		       }else{
		    	   setlanguage();  
		       }
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	       // setlanguage();
	        //ȥ������  
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
	        setContentView(R.layout.login);  
	       
	        //���ʵ������  
	        sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);  
	        username = (EditText) findViewById(R.id.ET_username);  
	        password = (EditText) findViewById(R.id.ET_password);  
	        cb_rem = (CheckBox) findViewById(R.id.CB_rememberme);  
	        cb_auto = (CheckBox) findViewById(R.id.CB_autologin);  
	        login_btn = (TextView) findViewById(R.id.login_btn);  
	        register_btn = (TextView)findViewById(R.id.register_btn);  
	        btnQuit = (ImageView) findViewById(R.id.btnQuit);  
	          
	        //�жϼ�ס�����ѡ���״̬  
	      if(sp.getBoolean("ISCHECK", false))  
	        {  
	          //����Ĭ���Ǽ�¼����״̬  
	    	  cb_rem.setChecked(true);  
	    	  username.setText(sp.getString("USER_NAME", ""));  
	    	  userNameValue=sp.getString("USER_NAME", "");
	          password.setText(sp.getString("PASSWORD", ""));  
	          passwordValue=sp.getString("PASSWORD", "");
	          //�ж��Զ���½��ѡ��״̬  
	          if(sp.getBoolean("AUTO_ISCHECK", false))  
	          {  
	                 //����Ĭ�����Զ���¼״̬  
	        	  cb_auto.setChecked(true);  
	              /*  //��ת����  
	                Intent intent = new Intent(LoginActivity.this,MainActivity.class);  
	                startActivity(intent); */ 
	        	  checkAccount(userNameValue, passwordValue);
	                  
	          }  
	        }  
	          
	        // ��¼�����¼�   
	      login_btn.setOnClickListener(new OnClickListener() {  
	  
	            public void onClick(View v) {  
	                userNameValue = username.getText().toString();  
	                passwordValue = password.getText().toString();  
	                checkAccount(userNameValue, passwordValue);
/*                if(checkAccount(userNameValue, passwordValue))  
	                {  
	                    Toast.makeText(LoginActivity.this,"��¼�ɹ�", Toast.LENGTH_SHORT).show();  
	                    //��¼�ɹ��ͼ�ס�����Ϊѡ��״̬�ű����û���Ϣ  
	                    if(cb_rem.isChecked())  
	                    {  
	                     //��ס�û��������롢  
	                      Editor editor = sp.edit();  
	                      editor.putString("USER_NAME", userNameValue);  
	                      editor.putString("PASSWORD",passwordValue);  
	                      editor.commit();  
	                    }  
	                    //��ת����  
	                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);  
	                    LoginActivity.this.startActivity(intent);  
	                    finish();  
	                      
	                }else{  
	                      
	                    Toast.makeText(LoginActivity.this,"�û�����������������µ�¼", Toast.LENGTH_LONG).show();  
	                } */ 
	                  
	            }  
	        });  
	  
	        //������ס�����ѡ��ť�¼�  
	      cb_rem.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
	            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
	                if (cb_rem.isChecked()) {  
	                      
	                    System.out.println("��ס������ѡ��");  
	                    sp.edit().putBoolean("ISCHECK", true).commit();  
	                      
	                }else {  
	                      
	                    System.out.println("��ס����û��ѡ��");  
	                    sp.edit().putBoolean("ISCHECK", false).commit();  
	                      
	                }  
	  
	            }  
	        });  
	          
	        //�����Զ���¼��ѡ���¼�  
	      cb_auto.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
	            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
	                if (cb_auto.isChecked()) {  
	                    System.out.println("�Զ���¼��ѡ��");  
	                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();  
	  
	                } else {  
	                    System.out.println("�Զ���¼û��ѡ��");  
	                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();  
	                }  
	            }  
	        });  
	        register_btn.setOnClickListener(new OnClickListener() {  
	              
	            @Override  
	            public void onClick(View v) {  
	            	   //��ת����  
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);  
                    LoginActivity.this.startActivity(intent); 
	            }  
	        });  
	        btnQuit.setOnClickListener(new OnClickListener() {  
	              
	            @Override  
	            public void onClick(View v) {  
	                finish();  
	            }  
	        });  
	  
	    }  
		public void checkAccount(String username,String password){
			
			RequestParams params= new RequestParams();
			params.add("username",username); 
			params.add("password",password);  
			String returncode="";
			
			HttpEngine.getHttpEngine().get(Constant.login, params, new AsyncHttpResponseHandler(){
				@Override
			    public void onSuccess(String result) {

					 if (result != null) {					
						 //UserInfo user= JSONUtils.jsonToEntity(result,UserInfo.class);	//ȡResultForm���е�data��Ӧ�Ķ���					 
						 String info = JSON.parseObject(result).getString("info");
						 String infoText = JSON.parseObject(result).getString("infoText");
						 String tokenId = JSON.parseObject(result).getString("tokenId");
						 String data = JSON.parseObject(result).getString("data");
						  if(info.equals("-1")){
                        	 Toast.makeText(LoginActivity.this, infoText,Toast.LENGTH_SHORT).show();                   	 
                         }
                         else {                   
     	                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.loginsuccess), Toast.LENGTH_SHORT).show();  
    	                    //��¼�ɹ��ͼ�ס�����Ϊѡ��״̬�ű����û���Ϣ  
    	                    if(cb_rem.isChecked())  
    	                    {  
    	                     //��ס�û��������롢  
    	                      Editor editor = sp.edit();  
    	                      editor.putString("USER_NAME", userNameValue);  
    	                      editor.putString("PASSWORD",passwordValue);  
    	                      editor.commit();  
    	                    }  
    	                    
    	                    Constant.setToken(tokenId);
    	                    Constant.setUserId(data);
    	                    
    	                    
    	                if(info.equals("1")){//�ѿ�ͨ����ֱ�ӵ�¼
    	                    //��ת����  
    	                    Intent intent = new Intent(LoginActivity.this,MainTabActivity.class);  
    	                    finish();
    	                    LoginActivity.this.startActivity(intent);  
    	                    //finish();  
    	                	
    	                }
    	                if(info.equals("2")){//δ��ͨ������Ҫ������Ϣ
    	                    //��ת����  
    	                    Intent intent = new Intent(LoginActivity.this,ModifyInfoActivity.class);  
    	                    finish();
    	                    LoginActivity.this.startActivity(intent);  
    	                    
    	                   // finish();  
    	                	
    	                }

    	                    
                         }
						// Toast.makeText(MainActivity.this, "�ϴ��ɹ�",Toast.LENGTH_SHORT).show();
					 } else {

							Toast.makeText(LoginActivity.this, getResources().getString(R.string.dataerror),Toast.LENGTH_SHORT).show();
						}
			    }						
			     @Override
	             public void onFailure(Throwable error) {
			    	 if(Constant.Debug){
			    		 Constant.UserId="007";
			    		 //��ת����  
 	                    Intent intent = new Intent(LoginActivity.this,MainTabActivity.class);  
 	                    finish();
 	                    LoginActivity.this.startActivity(intent);  
			    	 }
	                     error.printStackTrace();
	                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.timeout),Toast.LENGTH_SHORT).show();
	             }
			});	
			
			
			
/*			
			if(username.equals(password))
				return true;
			else return false;*/
			
		}

		
		
		public void setlanguage(){
			
			  String language = share.getString("language", "");//��ȡ�ϴα������������

				Resources resources = getResources();//���res��Դ����

		        Configuration config = resources.getConfiguration();//������ö���

		        DisplayMetrics dm = resources .getDisplayMetrics();//�����Ļ��������Ҫ�Ƿֱ��ʣ����صȡ�

		        
		      if(language.equals("en"))  
		    	  {
		    	
		    	  config.locale = Locale.ENGLISH; //Ӣ��
		    	 
		    	  }
		      else if(language.equals("ti"))
		      {

		    	  config.locale = new Locale("bo"); //����
		    	  resources.updateConfiguration(config, dm);
		    	  }
		      else {

		    	  config.locale = Locale.SIMPLIFIED_CHINESE; //��������
		    	
		    	  }
		      resources.updateConfiguration(config, dm);
              
		     /*   if("ti".equals(language))config.locale = new Locale("bo"); //����
		        else if("en".equals(language))config.locale = Locale.ENGLISH; //Ӣ��
		        else{
		        	config.locale = Locale.SIMPLIFIED_CHINESE; //��������
		        	
		        }*/
		 
		      
			 
			 
		}
		
		
	}



