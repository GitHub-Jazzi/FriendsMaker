package com.example.needinfo;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class NeedGPSInfo {

	private Context gContext;
    // ��ȡλ�ù������
	private LocationListener locationListener;
    private LocationManager locationManager;
	public NeedGPSInfo(Context context,LocationListener localListener)
	{
		gContext = context;
		locationListener= localListener;
	}
	
	//�ж��Ƿ�ͨgps����
	public  boolean openGPSSettings() {
        LocationManager alm = (LocationManager) gContext.getSystemService(Context.LOCATION_SERVICE);
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            return true;
        }
        
        //δ��ͨ
        //Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        //startActivityForResult(intent,0); //��Ϊ������ɺ󷵻ص���ȡ����
        return false;
    }

	
	//��ȡ��ǰλ��
	public  Location getGPSLocation()
    {
        
        // ���ҵ�������Ϣ
        Criteria criteria = new Criteria();//�ҵ�gps����
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // �߾���
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW); // �͹���

        //String serviceName = gContext.LOCATION_SERVICE;
        
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) gContext.getSystemService(serviceName);
        String provider = locationManager.getBestProvider(criteria, true); // ��ȡGPS��Ϣ
        
        //ʵ�ּ�����   
        locationListener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String arg0) {
				// TODO Auto-generated method stub
				
			}
			
			//���귢���ı�ʱ�򣬴ٷ��˺���
			@Override
			public void onLocationChanged(Location arg0) {
				// TODO Auto-generated method stub
				
			}
		};
        
        
      
        //z��������Զ���gps����
        //locationManager.setTestProviderEnabled("gps", true);//��gps
        
       
      
        //ע�������  ������Ҫע�������    ���ü��������Զ����µ���Сʱ��Ϊ���N��(1��Ϊ1*1000������д��ҪΪ�˷���)����Сλ�Ʊ仯����N��
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5 * 1000, 500, locationListener);
        
        

        
        //�رռ������ر�gps
        //locationManager.removeUpdates(locationListener);
        //locationManager.setTestProviderEnabled("gps", false);//��gps
        // Location location = locationManager.getLastKnownLocation(provider)
       
       // Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); // ͨ��GPS��ȡλ��
        
    
 		 
       return locationManager.getLastKnownLocation(provider);
    }
	
	
	//��ʾgps��λλ��
	private String updateToNewLocation(LocationManager manager,String provider) {

       // TextView tv1;
       // tv1 = (TextView) this.findViewById(R.id.tv1);
		String gpslocal =null;
		Location location = manager.getLastKnownLocation(provider);
        if (location != null) {
            double  latitude = location.getLatitude();
            double longitude= location.getLongitude();
            
             gpslocal = String.valueOf(latitude)+String.valueOf(longitude);
            
           System.out.println("ά�ȣ�" +  latitude+ "\n����" + longitude);
        } else {
        	System.out.println("�޷���ȡ������Ϣ11");
        }
        
        return gpslocal;

    }
	
	public String getLocation(){
		if(openGPSSettings()){
			Location location = this.getGPSLocation();
			if(location != null)
				return String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
			else 
				return "�޷���ȡ������Ϣ" ;
		}else{
			return "δ��ͨGPS";
		}
	}
	
}
