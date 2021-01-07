package cn.imppp.guider.ui.activity.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapRouteActivity;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

import androidx.annotation.Nullable;
import cn.imppp.guider.R;
import cn.imppp.guider.app.App;
import cn.imppp.guider.base.BaseActivity;
import cn.imppp.guider.constant.utils.AMapUtil;
import cn.imppp.guider.databinding.ActivityAmapRouteBinding;
import cn.imppp.guider.overlay.DrivingRouteOverlay;

/**
 * map页面
 */

public class MapRouteActivity extends BaseActivity<MapRouteViewModel, ActivityAmapRouteBinding>
        implements AMap.OnMapClickListener,
        AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener,
        AMap.InfoWindowAdapter, RouteSearch.OnRouteSearchListener {

    private AMap aMap;
    private LatLonPoint mStartPoint = new LatLonPoint(39.942295, 118.335891);//起点，39.942295,116.335891
    private LatLonPoint mEndPoint = new LatLonPoint(39.995576, 116.481288);//终点，39.995576,116.481288
    private RouteSearch mRouteSearch;

    @Override
    public int layoutRes() {
        return R.layout.activity_amap_route;
    }

    @Override
    public Class<MapRouteViewModel> viewModelClass() {
        return MapRouteViewModel.class;
    }

    @Override
    public void initClick() {
    }

    @Override
    public void loadData() {
    }

    @Override
    public void observer() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding.mvMapRouter.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mBinding.mvMapRouter.getMap();
        }
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setInfoWindowAdapter(this);
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
        searchRouteResult();

        // 加载导航模块
//        LatLng p2 = new LatLng(39.917337, 116.397056);//故宫博物院
//        LatLng p3 = new LatLng(39.904556, 116.427231);//北京站
//        AmapNaviParams params = new AmapNaviParams(new Poi("北京站", p3, ""),
//                null, new Poi("故宫博物院", p2, ""), AmapNaviType.DRIVER);
//        AMapNaviViewOptions aMapNaviViewOptions = new AMapNaviViewOptions();
//        params.setUseInnerVoice(true);
//        AmapNaviPage.getInstance().showRouteActivity(App.getContext(),
//                params, null);
    }

    private void searchRouteResult() {
        if (mStartPoint == null) {
            return;
        }
        if (mEndPoint == null) {
        }
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        // 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo,
                RouteSearch.DRIVING_SINGLE_DEFAULT, null,
                null, "");
        mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    DriveRouteResult mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    if(drivePath == null) {
                        return;
                    }
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            App.getContext(), aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
                    int taxiCost = (int) mDriveRouteResult.getTaxiCost();

                } else if (result != null && result.getPaths() == null) {
                }

            } else {
            }
        } else {
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
