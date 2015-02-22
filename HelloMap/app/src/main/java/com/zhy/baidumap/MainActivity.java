package com.zhy.baidumap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class MainActivity extends Activity implements OnOrientationEvent {
    public static final String TAG = "TAG";
    private MapView mapView;
    private BaiduMap bdMap;


    private LocationClient locClient;
    private BDLocationListener locListener;

    private MySensorEventListener sensorEventListener;

    private LatLng curLatLng;
    private float direction;
    private boolean isFirstIn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //初始化SDK
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.main_activity);
        //初始化控件
        init();
        //初始化定位信息
        initLocClt();

    }

    private void initLocClt() {
        locClient = new LocationClient(this);
        locListener = new MyLocationListener();
        LocationClientOption locCltOpt = new LocationClientOption();
        locCltOpt.setCoorType("bd09ll");//设置百度坐标系
        locCltOpt.setOpenGps(true);
        locCltOpt.setScanSpan(1000);//设置定位时间
        locClient.setLocOption(locCltOpt);
        locClient.registerLocationListener(locListener);
    }

    private void init() {
        mapView = (MapView) findViewById(R.id.bmapView);

        //创建地图状态对象
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        bdMap = mapView.getMap();
        bdMap.setMapStatus(msu);

        sensorEventListener = new MySensorEventListener(this);
        sensorEventListener.setOrientationEvent(this);


    }

    @Override
    protected void onStart() {
        bdMap.setMyLocationEnabled(true);
        if (!locClient.isStarted()) {
            locClient.start();
        }
        sensorEventListener.start();
        super.onStart();
    }

    @Override
    protected void onStop() {

        bdMap.setMyLocationEnabled(false);
        locClient.stop();
        sensorEventListener.stop();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //初始化Menu
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_common:
                Log.i(TAG, "普通地图");
                bdMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            case R.id.menu_item_site:
                Log.i(TAG, "卫星地图");
                bdMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.menu_item_traffic:
                Log.i(TAG, "实时交通");
                setTrafficEnabled(item);
                break;
            case R.id.menu_item_heat:
                Log.i(TAG, "热力地图");
                setHeatEnabled(item);

            default:
                break;
        }

        return true;
    }

    private void setHeatEnabled(MenuItem item) {
        if (bdMap.isBaiduHeatMapEnabled()) {
            bdMap.setBaiduHeatMapEnabled(false);
            item.setTitle("热力(off)");
        } else {
            bdMap.setBaiduHeatMapEnabled(true);
            item.setTitle("热力(on)");
        }
    }

    private void setTrafficEnabled(MenuItem item) {
        if (bdMap.isTrafficEnabled()) {
            bdMap.setTrafficEnabled(false);
            item.setTitle("实时交通(off)");
        } else {
            bdMap.setTrafficEnabled(true);
            item.setTitle("实时交通(on)");
        }
    }

    @Override
    public void onOrientationChange(float value) {
        this.direction = value;
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            //构造定位数据
            MyLocationData myLocData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .direction(direction)
                    .build();

            curLatLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            BitmapDescriptor myDescriptor =
                    BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
            MyLocationConfiguration config = new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL, true, myDescriptor);

            bdMap.setMyLocationData(myLocData);
            bdMap.setMyLocationConfigeration(config);

            if (isFirstIn) {

                setMyLocation(curLatLng);
                isFirstIn = false;
            }


        }
    }

    private void setMyLocation(LatLng curLatLng) {
        if (null == curLatLng) {
            return;
        }
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(curLatLng);
        bdMap.setMapStatus(msu);
    }
}
