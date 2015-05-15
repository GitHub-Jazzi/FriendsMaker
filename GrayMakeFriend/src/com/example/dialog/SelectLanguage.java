package com.example.dialog;

import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;



import com.example.main.MainTabActivity;
import com.example.mfriends.ConfigActivity;
import com.example.mfriends.LoginActivity;
import com.example.mfriends.MainActivity;
import com.example.mfriends.ModifyInfoActivity;
import com.example.mfriends.R;
import com.example.widget.ArrayWheelAdapter;
import com.example.widget.NumericWheelAdapter;
import com.example.widget.OnWheelChangedListener;
import com.example.widget.WheelView;

public class SelectLanguage extends PopupWindow implements OnClickListener {

	private Activity mContext;
	private View mMenuView;
	private ViewFlipper viewfipper;
	private Button btn_submit, btn_cancel;
	private TextView titleview;
	private TextView  text;
	private int mCurHeight = 80;
	
	private String arrayValue;
	private MyArrayAdapter arrayAdapter;
	private WheelView arrayWheel;

	 public static final int  EXIT_APPLICATION = 0x0001;  
	public SelectLanguage(Activity context,final View Fieldview,String title,int arrayType) {
		super(context);
		mContext = context;
		//this.textValue = ((TextView)Fieldview).getText().toString();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.language_dialog, null);
		viewfipper = new ViewFlipper(context);
		viewfipper.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		arrayWheel = (WheelView) mMenuView.findViewById(R.id.arrayWheel);
		
		 titleview = (TextView) mMenuView.findViewById(R.id.dialog_text_title);	
		btn_submit = (Button) mMenuView.findViewById(R.id.submit);
		btn_cancel = (Button) mMenuView.findViewById(R.id.cancel);
		
		titleview.setText(title);
		btn_submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				((TextView)Fieldview).setText(arrayAdapter.getItemText(arrayWheel.getCurrentItem()));
				changelanguage(arrayWheel.getCurrentItem());
				SelectLanguage.this.dismiss();
				
				
		        Intent mIntent = new Intent();  
		        mIntent.setClass(mContext, MainTabActivity.class);  
		        //��������flag���ǱȽ� ��Ҫ��  
		        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
		        //�����˳�����ָʾ  
		        mIntent.putExtra("flag", EXIT_APPLICATION);  
		        mContext.finish();
		        mContext.startActivity(mIntent); 
		        Intent mIntent1 = new Intent();  
		        mIntent.setClass(mContext, MainTabActivity.class);  
		        //��������flag���ǱȽ� ��Ҫ��  
		        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
		        //�����˳�����ָʾ  
		        mContext.startActivity(mIntent); 
		      
				
		       
/*				Intent intent = new Intent();
				intent.setClass(mContext,LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				mContext.finish();
				mContext.startActivity(intent);
				 android.os.Process.killProcess(android.os.Process.myPid());*/
				 
			}
		});
		btn_cancel.setOnClickListener(this);


		
		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				//updateValue(arrayWheel);
				 //((TextView)view).setText("111");
			}
		};
		String[] items=context.getResources().getStringArray(arrayType);
		arrayAdapter = new MyArrayAdapter(context,items);
		/*arrayAdapter.setTextType(dateType);*/
		arrayWheel.setViewAdapter(arrayAdapter);
		arrayWheel.addChangingListener(listener);
		



		viewfipper.addView(mMenuView);
		viewfipper.setFlipInterval(6000000);
		this.setContentView(viewfipper);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(dw);
		this.update();

	}

	private void updateValue(WheelView height) {

		//arrayValue=arrayWheel.getContentDescription();
	}
	
	
	
	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		viewfipper.startFlipping();
	}


	/**
	 * Adapter for  wheels. Highlights the current value.
	 */
	private class MyArrayAdapter extends ArrayWheelAdapter {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		String values[];
		/**
		 * Constructor
		 */
		public MyArrayAdapter(Context context,  String items[]) {
			super(context, items);
			//this.values=values;
			
			//this.currentValue = current;
			//setTextSize(24);
		}

		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			view.setTypeface(Typeface.SANS_SERIF);
		}

		public CharSequence getItemText(int index) {
			currentItem = index;
			return super.getItemText(index);
		}

	}


	public void onClick(View v) {
		this.dismiss();
	}

	
	public void changelanguage(int item){
		 SharedPreferences  share = mContext.getSharedPreferences("perference", mContext.MODE_PRIVATE);
		  Editor editor = share.edit();// ȡ�ñ༭��	 
		  String language = share.getString("language", "");//��ȡ�ϴα������������

			Resources resources = mContext.getResources();//���res��Դ����

	        Configuration config = resources.getConfiguration();//������ö���

	        DisplayMetrics dm = resources .getDisplayMetrics();//�����Ļ��������Ҫ�Ƿֱ��ʣ����صȡ�

	        
	      if(item==1&&!language.equals("en"))  
	    	  {
	    	  editor.putString("language", "en");// �洢���� ����ѡ��Ӣ��
	    	  config.locale = Locale.ENGLISH; //Ӣ��
	    	  editor.commit();
	    	  resources.updateConfiguration(config, dm);
	    	  }
	      else if(item==2&&!language.equals("ti"))
	      {
	    	  editor.putString("language", "ti");// �洢���� ����ѡ�����
	    	  config.locale = new Locale("bo"); //����
	    	  editor.commit();
	    	  resources.updateConfiguration(config, dm);
	    	  }
	      else  if(!language.equals("cn")) {
	    	  editor.putString("language", "cn");// �洢���� ����ѡ������
	    	  config.locale = Locale.SIMPLIFIED_CHINESE; //��������
	    	  editor.commit();
	    	  resources.updateConfiguration(config, dm);
	    	  }
	        
	     /*   if("ti".equals(language))config.locale = new Locale("bo"); //����
	        else if("en".equals(language))config.locale = Locale.ENGLISH; //Ӣ��
	        else{
	        	config.locale = Locale.SIMPLIFIED_CHINESE; //��������
	        	
	        }*/
	 
	      
		 
		 
	}

}
