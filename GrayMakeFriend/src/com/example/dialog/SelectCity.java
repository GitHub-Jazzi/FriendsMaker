package com.example.dialog;

import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;


import com.example.mfriends.ModifyInfoActivity;
import com.example.mfriends.R;
import com.example.model.CityModel;
import com.example.model.DistrictModel;
import com.example.model.ProvinceModel;
import com.example.utils.XmlParserHandler;
import com.example.widget.ArrayWheelAdapter;
import com.example.widget.NumericWheelAdapter;
import com.example.widget.OnWheelChangedListener;
import com.example.widget.WheelView;


public class SelectCity extends PopupWindow implements OnClickListener,OnWheelChangedListener {

	private Activity mContext;
	private View mMenuView;
	private ViewFlipper viewfipper;
	private Button btn_submit, btn_cancel;
	private TextView titleview;
	private TextView  text;
	private int mCurHeight = 80;
	
	private String arrayValue;
	private MyArrayAdapter arrayAdapter;
	private WheelView province,city,district;

	/**
	 * ����ʡ
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - ʡ value - ��
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - �� values - ��
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
	
	/**
	 * key - �� values - �ʱ�
	 */
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>(); 

	/**
	 * ��ǰʡ������
	 */
	protected String mCurrentProviceName;
	/**
	 * ��ǰ�е�����
	 */
	protected String mCurrentCityName;
	/**
	 * ��ǰ��������
	 */
	protected String mCurrentDistrictName ="";
	
	/**
	 * ��ǰ������������
	 */
	protected String mCurrentZipCode ="";
	
	/**
	 * ����ʡ������XML����
	 */
	
    protected void initProvinceDatas()
	{
		List<ProvinceModel> provinceList = null;
    	AssetManager asset = mContext.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // ����һ������xml�Ĺ�������
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// ����xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// ��ȡ��������������
			provinceList = handler.getDataList();
			//*/ ��ʼ��Ĭ��ѡ�е�ʡ���С���
			if (provinceList!= null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList!= null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0).getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();
					mCurrentZipCode = districtList.get(0).getZipcode();
				}
			}
			//*/
			mProvinceDatas = new String[provinceList.size()];
        	for (int i=0; i< provinceList.size(); i++) {
        		// ��������ʡ������
        		mProvinceDatas[i] = provinceList.get(i).getName();
        		List<CityModel> cityList = provinceList.get(i).getCityList();
        		String[] cityNames = new String[cityList.size()];
        		for (int j=0; j< cityList.size(); j++) {
        			// ����ʡ����������е�����
        			cityNames[j] = cityList.get(j).getName();
        			List<DistrictModel> districtList = cityList.get(j).getDistrictList();
        			String[] distrinctNameArray = new String[districtList.size()];
        			DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
        			for (int k=0; k<districtList.size(); k++) {
        				// ����������������/�ص�����
        				DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
        				// ��/�ض��ڵ��ʱ࣬���浽mZipcodeDatasMap
        				mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
        				distrinctArray[k] = districtModel;
        				distrinctNameArray[k] = districtModel.getName();
        			}
        			// ��-��/�ص����ݣ����浽mDistrictDatasMap
        			mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
        		}
        		// ʡ-�е����ݣ����浽mCitisDatasMap
        		mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
        	}
        } catch (Throwable e) {  
            e.printStackTrace();  
        } finally {
        	
        } 
	}


	public SelectCity(Activity context,final View Fieldview,String title) {
		super(context);
		mContext = context;
		//this.textValue = ((TextView)Fieldview).getText().toString();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.city_dialog, null);
		viewfipper = new ViewFlipper(context);
		viewfipper.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		province = (WheelView) mMenuView.findViewById(R.id.province);
		city = (WheelView) mMenuView.findViewById(R.id.city);
		district = (WheelView) mMenuView.findViewById(R.id.district);
		
		 titleview = (TextView) mMenuView.findViewById(R.id.dialog_text_title);	
		btn_submit = (Button) mMenuView.findViewById(R.id.submit);
		btn_cancel = (Button) mMenuView.findViewById(R.id.cancel);
		
		titleview.setText(title);
		btn_submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//((TextView)Fieldview).setText(arrayAdapter.getItemText(arrayWheel.getCurrentItem()));
				((TextView)Fieldview).setText(mCurrentProviceName+mCurrentCityName+mCurrentDistrictName);
				SelectCity.this.dismiss();
			}
		});
		btn_cancel.setOnClickListener(this);

		setUpListener();
		setUpData();
/*		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				//updateValue(arrayWheel);
				 //((TextView)view).setText("111");
			}
		};*/
		//String[] items=context.getResources().getStringArray(arrayType);
		//arrayAdapter = new MyArrayAdapter(context,items);
		/*arrayAdapter.setTextType(dateType);*/
		//province.setViewAdapter(arrayAdapter);
		//arrayWheel.addChangingListener(listener);
		



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

	
	private void setUpListener() {
    	// ���change�¼�
    	province.addChangingListener(this);
    	// ���change�¼�
    	city.addChangingListener(this);
    	// ���change�¼�
    	district.addChangingListener(this);
    }
	
	private void setUpData() {
		initProvinceDatas();
		province.setViewAdapter(new ArrayWheelAdapter<String>(mContext, mProvinceDatas));
		// ���ÿɼ���Ŀ����
		province.setVisibleItems(7);
		city.setVisibleItems(7);
		district.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == province) {
			updateCities();
		} else if (wheel == city) {
			updateAreas();
		} else if (wheel == district) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		}
	}

	/**
	 * ���ݵ�ǰ���У�������WheelView����Ϣ
	 */
	private void updateAreas() {
		int pCurrent = city.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		district.setViewAdapter(new ArrayWheelAdapter<String>(mContext, areas));
		district.setCurrentItem(0);
	}

	/**
	 * ���ݵ�ǰ��ʡ��������WheelView����Ϣ
	 */
	private void updateCities() {
		int pCurrent = province.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		city.setViewAdapter(new ArrayWheelAdapter<String>(mContext, cities));
		city.setCurrentItem(0);
		updateAreas();
	}

}
