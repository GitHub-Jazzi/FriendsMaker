package com.example.needinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class NeedPhoneSMS {

	private Uri SMS_INBOX = Uri.parse("content://sms/");
	private Context scontext;
	private List<SMSEntity> listsms = new ArrayList<SMSEntity>();

	public NeedPhoneSMS(Context context) {
		scontext = context;
	}

	public List<SMSEntity> getSmsFromPhone() {
		ContentResolver cr = scontext.getContentResolver();
		String[] projection = new String[] {"address", "person","body", "date", "type" };// "_id", "address", ������
														// "person", ������
		// "date", ʱ��
														// "type  ���ͻ��ǽ���
		String where = " address = '10086' AND date >  " + (System.currentTimeMillis() - 10 * 60 * 1000);
		Cursor cur = cr.query(SMS_INBOX, projection, null, null, "date desc");
		
		if(cur!=null)
		{	
			while(cur.moveToNext()) {
				String number = cur.getString(cur.getColumnIndex("address"));// �����˻��߽������ֻ���
				String name = cur.getString(cur.getColumnIndex("person"));// ��ϵ�������б�
				String body = cur.getString(cur.getColumnIndex("body"));
				// ��������Ҫ��ȡ�Լ����ŷ�������е���֤��~~
				Pattern pattern = Pattern.compile(" [a-zA-Z0-9]{10}");
				Matcher matcher = pattern.matcher(body);
				if (matcher.find()) {
					String res = matcher.group().substring(1, 11);
				}
	
				SMSEntity smse = new SMSEntity();
	
				smse.setNumber(number);
				smse.setName(name);
				smse.setBody(body);
				listsms.add(smse);
			}
			
		}
		
		return listsms;
	}

}
