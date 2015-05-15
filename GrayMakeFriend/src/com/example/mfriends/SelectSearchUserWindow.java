package com.example.mfriends;


import com.example.main.AActivity;
import com.example.main.MainTabActivity;
import com.example.mfriends.R;
import com.tencent.weibo.oauthv2.OAuthV2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public class SelectSearchUserWindow extends PopupWindow {

	private View mMenuView;
	private Button search_btn;
	private TextView search_name;

	public SelectSearchUserWindow(final Activity context,OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.search, null);
		search_btn=(Button)mMenuView.findViewById(R.id.search_btn);
		search_name=(TextView)mMenuView.findViewById(R.id.search_name);
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		//���ð�ť����
		//����SelectPicPopupWindow��View
		this.setContentView(mMenuView);
		//����SelectPicPopupWindow��������Ŀ�
		this.setWidth(LayoutParams.FILL_PARENT);
		//����SelectPicPopupWindow��������ĸ�
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//����SelectPicPopupWindow��������ɵ��
		this.setFocusable(true);
		//����SelectPicPopupWindow�������嶯��Ч��
		this.setAnimationStyle(R.style.mystyle);
		//ʵ����һ��ColorDrawable��ɫΪ��͸��
		ColorDrawable dw = new ColorDrawable(0000000000);
		//����SelectPicPopupWindow��������ı���
		this.setBackgroundDrawable(dw);
		//mMenuView���OnTouchListener�����жϻ�ȡ����λ�������ѡ������������ٵ�����
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.pop_layout_search).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}				
				return true;
			}
		});
		
		search_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
/*				Log.i("SelectPicPopupWindow", "��������");
			 	Intent intent=new Intent();
            	intent.setClass(context,SearchActivity.class);
            	
            	//��BundleЯ������
                Bundle bundle=new Bundle();
                //����name����Ϊtinyphp
                bundle.putString("username", search_name.getText().toString());
                intent.putExtras(bundle);
            	
            	context.startActivity(intent);*/
				
				
				// �㲥֪ͨ  
		         Intent intent = new Intent();  
		         intent.putExtra("username", search_name.getText().toString());
		         intent.setAction("action.search");  
		         context.sendBroadcast(intent); 
				
				
				dismiss();
			}
		});
	}
}
