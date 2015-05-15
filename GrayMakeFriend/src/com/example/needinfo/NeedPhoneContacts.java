package com.example.needinfo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class NeedPhoneContacts {

	Context mContext;
	
	/** ��ȡ��Phon���ֶ� **/
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER };
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;	/** ��ϵ����ʾ���� **/
	private static final int PHONES_NUMBER_INDEX = 1;	/** �绰���� **/




	private List<ContractsEntity> listcon = new ArrayList<ContractsEntity>();
	
	public NeedPhoneContacts(Context context) {
		mContext = context;
	}

	
	//��õ绰����sim���ϵ�������ϵ��ʽ
	public List<ContractsEntity> getPhoneAndSIMContracts()
	{
		//��ȡ�绰�ϵ���ϵ��ʽ
		this.getPhoneContacts();
		//��ȡsim���ϵ���ϵ��ʽ
		this.getSIMContacts();
		
		
		return listcon; 
	}
	
	
	
	/** �õ��ֻ�ͨѶ¼��ϵ����Ϣ **/
	private  void getPhoneContacts() {
		ContentResolver resolver = mContext.getContentResolver();

		// ��ȡ�ֻ���ϵ��
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				// �õ��ֻ�����
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				// ���ֻ�����Ϊ�յĻ���Ϊ���ֶ� ������ǰѭ��
				if (TextUtils.isEmpty(phoneNumber))
					continue;

				// �õ���ϵ������
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

				

				ContractsEntity ce = new ContractsEntity();
				ce.setName(contactName);
				ce.setPhonenum(phoneNumber);
				listcon.add(ce);
				
			}

			phoneCursor.close();
		}
	}

	/** �õ��ֻ�SIM����ϵ������Ϣ **/
	private void getSIMContacts() {
		ContentResolver resolver = mContext.getContentResolver();
		// ��ȡSims����ϵ��
		Uri uri = Uri.parse("content://icc/adn");
		Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				// �õ��ֻ�����
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				// ���ֻ�����Ϊ�յĻ���Ϊ���ֶ� ������ǰѭ��
				if (TextUtils.isEmpty(phoneNumber))
					continue;
				// �õ���ϵ������
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

				// Sim����û����ϵ��ͷ��
				ContractsEntity ce = new ContractsEntity();
				ce.setName(contactName);
				ce.setPhonenum(phoneNumber);
				listcon.add(ce);
			}

			phoneCursor.close();
		}
	}

	

}
