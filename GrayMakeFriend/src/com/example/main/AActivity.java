package com.example.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.mfriends.ListUserDetailsActivity;
import com.example.mfriends.MainActivity;
import com.example.mfriends.ModifyInfoActivity;
import com.example.mfriends.R;
import com.example.mfriends.UsersHuihuaAdapter;
import com.example.model.UsersHuiHua;
import com.example.utils.Constant;
import com.example.utils.DownImage;
import com.example.utils.HandlerConstants;
import com.example.utils.HttpEngine;
import com.example.utils.JSONUtils;
import com.example.utils.DownImage.ImageCallBack;
import com.example.utils.ProgressDialogUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class AActivity extends Activity {

	private ListView listview1;
	private UsersHuihuaAdapter haAdapter;
	private int pageNumUsers = 1;
	private int pageSizeUsers = 10; // Ĭ��
	private boolean isLastPageUsers = true;// �ж��Ƿ�Ϊ���һҳ
	private String searchname = "";

	/*
	 * private Handler mhandler=new Handler(){//δʹ��
	 * 
	 * @Override public void handleMessage(Message msg) { switch (msg.what) {
	 * case 301: hcAdapter.addList(cfDataList);
	 * listViewMyCare.setAdapter(hcAdapter); break; default: break; }
	 * super.handleMessage(msg); }
	 * 
	 * };
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * TextView tv = new TextView(this); tv.setText("This is A Activity!");
		 * tv.setGravity(Gravity.CENTER);
		 */
		setContentView(R.layout.menu_list);

		// ��ҳ���������
		Bundle bundle = this.getIntent().getExtras();
		// ����nameֵ
		if (bundle != null && bundle.getString("username") != null)
			searchname = bundle.getString("username");

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("action.refresh");
		intentFilter.addAction("action.refreshUsers");
		intentFilter.addAction("action.search");
		registerReceiver(mRefreshBroadcastReceiver, intentFilter);

		listview1 = (ListView) findViewById(R.id.listView1);
		ArrayList<UsersHuiHua> st = new ArrayList<UsersHuiHua>();
		haAdapter = new UsersHuihuaAdapter(this, st);// �����ȡ�û�������Ϣ�б�����ݣ���Ƭ���û����ȣ�
		listview1.setAdapter(haAdapter);// �������
		listview1.setCacheColorHint(0);
		// ��ʼ���ȵ�һ�μ���һ������
		// ReloadrunThreadUsers();
		if (isSearch()) {
			searchUserList();
		} else {
			startUserListPart();
		}

		// listViewUsers.set
		// ��ӹ�����������
		listview1.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}

			@Override
			public void onScroll(AbsListView arg0, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int lastItemid = listview1.getLastVisiblePosition();
				if ((lastItemid + 1) == totalItemCount && !isLastPageUsers) {

					// ReloadrunThreadUsers();
					if (isSearch()) {
						searchUserList();
					} else {
						startUserListPart();
					}

				}

			}
		});
		// listview ÿ��item��������¼�
		listview1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) { // (AdapterView<?> parent, View
												// view,int position, long id
				// ��һ�������������ڶ�����view ����������position ���ĸ���id
				// TODO Auto-generated method stub

				UsersHuiHua uhh = (UsersHuiHua) listview1
						.getItemAtPosition(position);

				Intent intent = new Intent(AActivity.this,
						ListUserDetailsActivity.class);

				intent.putExtra("id", String.valueOf(Constant.getUserId()));
				intent.putExtra("friendid", String.valueOf(uhh.getId()));
				intent.putExtra("username", String.valueOf(uhh.getUsername()));
				startActivity(intent);
			}
		});
	}

	public void searchUserList() {

		RequestParams para = new RequestParams();
		para.add("pageSize", pageSizeUsers + "");
		para.add("pageNow", (pageNumUsers++) + "");// ÿ�μ��أ�pageNum+1=pageNow
		para.add("username", searchname);
		// ���ʷ�����
		ProgressDialogUtils.showProgressDialog(this, "���ݼ�����...");
		HttpEngine.getHttpEngine().post(Constant.selectUsersList, para,
				new AsyncHttpResponseHandler() {

					String[] localar = { "id", "username", "gender",
							"education", "salary", "age", "image",
							"lastlogintime", "selfintro" };
					String[] serar = { "id", "username", "gender", "education",
							"salary", "age", "image", "lastlogintime",
							"selfintro" };

					@SuppressWarnings("deprecation")
					@Override
					public void onSuccess(String result) {
						// �رս�����
						ProgressDialogUtils.dismissProgressDialog();
						if (result != null) {
							// �ɹ�֮����ʼ�����û���������
							List<Map<String, Object>> relist = JSONUtils
									.getDataList(localar, serar, result);

							if (relist != null) {
								if (relist.size() < pageSizeUsers)
									isLastPageUsers = true;
								else
									isLastPageUsers = false;

								if (relist.size() > 0) {

									/*
									 * userDataList=getHuahui(relist); Message
									 * msg = new Message(); msg.what = 302;
									 * handler.sendMessage(msg);
									 */

									haAdapter.addDataList(getHuahui(relist));
									if (haAdapter != null) {
										haAdapter.notifyDataSetChanged();
									}

								}

							}
							// �ɹ�֮�󣬻ص���Ϣ
							// ��������������̣߳�֮��֪ͨ�߳��Ѿ�����
							/*
							 * Message msg =
							 * handler.obtainMessage(HandlerConstants
							 * .GET_USER_LIST_OK,"");//����������what and obj
							 * 
							 * handler.sendMessage(msg);
							 */

						} else {
							// Toast.makeText(MainActivity.this,
							// "��ȡ���ݴ���",Toast.LENGTH_SHORT).show();
							/*
							 * Message msg =
							 * handler.obtainMessage(HandlerConstants
							 * .GET_USER_LIST_ERROR,"��������ʧ��");//����������what and
							 * obj
							 * 
							 * handler.sendMessage(msg);
							 */
						}

						// ÿ��ִ��֮��ǿ�йر��߳�
						// Thread.currentThread().stop();
					}

					@Override
					public void onFailure(Throwable error) {
						if (Constant.Debug) {

							haAdapter.addDataList(getHuahuiLocal());
							if (haAdapter != null) {
								haAdapter.notifyDataSetChanged();
							}

						}

						// �رս�����
						ProgressDialogUtils.dismissProgressDialog();
						error.printStackTrace();
					}

				});
	}

	public boolean isSearch() {
		if (searchname.equals(""))
			return false;
		else
			return true;

	}

	public void startUserListPart() {
		RequestParams para = new RequestParams();

		para.add("pageSize", pageSizeUsers + "");
		para.add("pageNow", (pageNumUsers++) + "");// ÿ�μ��أ�pageNum+1=pageNow
		// ���ʷ�����
		ProgressDialogUtils.showProgressDialog(this, "���ݼ�����...");
		HttpEngine.getHttpEngine().post(Constant.getUsersList, para,
				new AsyncHttpResponseHandler() {

					String[] localar = { "id", "username", "gender",
							"education", "salary", "age", "image",
							"lastlogintime", "selfintro" };
					String[] serar = { "id", "username", "gender", "education",
							"salary", "age", "image", "lastlogintime",
							"selfintro" };

					@SuppressWarnings("deprecation")
					@Override
					public void onSuccess(String result) {
						// �رս�����
						ProgressDialogUtils.dismissProgressDialog();
						if (result != null) {
							// �ɹ�֮����ʼ�����û���������
							List<Map<String, Object>> relist = JSONUtils
									.getDataList(localar, serar, result);

							if (relist != null) {
								if (relist.size() < pageSizeUsers)
									isLastPageUsers = true;
								else
									isLastPageUsers = false;

								if (relist.size() > 0) {

									/*
									 * userDataList=getHuahui(relist); Message
									 * msg = new Message(); msg.what = 302;
									 * handler.sendMessage(msg);
									 */

									haAdapter.addDataList(getHuahui(relist));
									if (haAdapter != null) {
										haAdapter.notifyDataSetChanged();
									}

								}

							}
							// �ɹ�֮�󣬻ص���Ϣ
							// ��������������̣߳�֮��֪ͨ�߳��Ѿ�����
							/*
							 * Message msg =
							 * handler.obtainMessage(HandlerConstants
							 * .GET_USER_LIST_OK,"");//����������what and obj
							 * 
							 * handler.sendMessage(msg);
							 */

						} else {
							// Toast.makeText(MainActivity.this,
							// "��ȡ���ݴ���",Toast.LENGTH_SHORT).show();
							/*
							 * Message msg =
							 * handler.obtainMessage(HandlerConstants
							 * .GET_USER_LIST_ERROR,"��������ʧ��");//����������what and
							 * obj
							 * 
							 * handler.sendMessage(msg);
							 */
						}

						// ÿ��ִ��֮��ǿ�йر��߳�
						// Thread.currentThread().stop();
					}

					@Override
					public void onFailure(Throwable error) {
						if (Constant.Debug) {
							haAdapter.addDataList(getHuahuiLocal());
							if (haAdapter != null) {
								haAdapter.notifyDataSetChanged();
							}

						}

						// �رս�����
						ProgressDialogUtils.dismissProgressDialog();
						error.printStackTrace();
					}

				});
	}

	public ArrayList<UsersHuiHua> userDataList = new ArrayList();

	private ArrayList<UsersHuiHua> getHuahui(List<Map<String, Object>> huilist) {
		// ��ʾ��������
		ArrayList<UsersHuiHua> hhList = new ArrayList<UsersHuiHua>();

		UsersHuiHua huihua;
		for (int i = 0; i < huilist.size(); i++) {
			huihua = new UsersHuiHua();
			huihua.setId(String.valueOf(huilist.get(i).get("id")));
			// huihua.setTxPath(String.valueOf(huilist.get(i).get("image")));
			huihua.setImage(String.valueOf(huilist.get(i).get("image")));

			huihua.setUsername(String.valueOf(huilist.get(i).get("username")));
			huihua.setselfintro(String.valueOf(huilist.get(i).get("selfintro")));
			huihua.setLastlogintime(String.valueOf(huilist.get(i).get(
					"lastlogintime")));
			huihua.setGender(String.valueOf(huilist.get(i).get("gender")));
			huihua.setAge(String.valueOf(huilist.get(i).get("age")));
			huihua.setSalary(String.valueOf(huilist.get(i).get("salary")));
			huihua.setEducation(String.valueOf(huilist.get(i).get("education")));
			hhList.add(huihua);
		}

		return hhList;
	}

	private ArrayList<UsersHuiHua> getHuahuiLocal() {
		// ��ʾ��������
		ArrayList<UsersHuiHua> hhList = new ArrayList<UsersHuiHua>();

		UsersHuiHua huihua;
		for (int i = 0; i < 10; i++) {
			huihua = new UsersHuiHua();
			huihua.setId(String.valueOf(007));
			// huihua.setTxPath(String.valueOf(huilist.get(i).get("image")));
			huihua.setImage(String.valueOf(""));

			huihua.setUsername(String.valueOf("Mrxu"));
			huihua.setselfintro(String.valueOf("���˼��"));
			huihua.setLastlogintime("");
			huihua.setGender(String.valueOf(true));
			huihua.setAge(String.valueOf(26));
			huihua.setSalary(String.valueOf(666666));
			huihua.setEducation(String.valueOf("��ʿ"));
			hhList.add(huihua);
		}

		return hhList;
	}

	// broadcast receiver
	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("action.refreshUsers")
					|| action.equals("action.refresh")) {
				searchname = "";
				reFreshList();
			}

			if (action.equals("action.search")) {
				searchname = intent.getStringExtra("username");
				reFreshList();

			}

		}
	};

	public void reFreshList() {
		pageNumUsers = 1;
		pageSizeUsers = 10; // Ĭ��
		isLastPageUsers = true;// �ж��Ƿ�Ϊ���һҳ
		haAdapter.clearList();
		if (isSearch()) {
			searchUserList();
		} else {
			startUserListPart();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mRefreshBroadcastReceiver);
	}
}
