package com.zhy.baidumap;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;

public class MainActivity extends Activity {
    public static final String TAG = "TAG";
    private MapView mapView;
    private BaiduMap bdMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //初始化SDK
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.main_activity);
        mapView = (MapView) findViewById(R.id.bmapView);

        //创建地图状态对象
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        bdMap = mapView.getMap();
        bdMap.setMapStatus(msu);
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

            default:
                break;
        }

        return true;
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
}
