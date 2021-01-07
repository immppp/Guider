package cn.imppp.guider.ui.activity.guide;

import androidx.appcompat.app.AppCompatActivity;
import cn.imppp.guider.R;
import cn.imppp.guider.app.App;
import cn.imppp.guider.constant.utils.AMapUtil;
import cn.imppp.guider.listener.MapNaviListener;
import cn.imppp.guider.listener.MapNaviViewListener;
import cn.imppp.guider.overlay.DrivingRouteOverlay;

import android.os.Bundle;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.api.navi.enums.ReCalculateRouteType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.model.NaviPoi;
import com.amap.api.services.route.RouteSearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuideActivity extends AppCompatActivity {

    private AMapNaviView anvGuide;
    protected NaviLatLng mEndLatlng = new NaviLatLng(40.084894,116.603039);
    protected NaviLatLng mStartLatlng = new NaviLatLng(39.825934,116.342972);
    protected final List<NaviLatLng> sList = new ArrayList<NaviLatLng>();
    protected final List<NaviLatLng> eList = new ArrayList<NaviLatLng>();
    protected List<NaviLatLng> mWayPointList = new ArrayList<NaviLatLng>();
    private AMapNavi aMapNavi;
    private AMap aMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        anvGuide = findViewById(R.id.anvGuide);
        anvGuide.setAMapNaviViewListener(mapNaviViewListener);
        anvGuide.onCreate(savedInstanceState);
        AMapNaviViewOptions options = anvGuide.getViewOptions();
        options.setLayoutVisible(false);

        aMap = anvGuide.getMap();

        aMapNavi = AMapNavi.getInstance(App.getContext());
        aMapNavi.setMultipleRouteNaviMode(true);
        aMapNavi.addAMapNaviListener(mapNaviListener);
        //开启多路线模式
    }

    private MapNaviViewListener mapNaviViewListener = new MapNaviViewListener() {
    };

    private MapNaviListener mapNaviListener = new MapNaviListener() {

        @Override
        public void onInitNaviSuccess() {
            super.onInitNaviSuccess();
            // 设置数据
//            sList.add(mStartLatlng);
//            eList.add(mEndLatlng);
//            int strategy = 0;
//            strategy = aMapNavi.strategyConvert(true, false, false, false, false);
//            aMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);
            //起点
            NaviPoi start = new NaviPoi("立水桥(北5环)", new LatLng(40.066957,116.320518), "");
            //终点
            NaviPoi end = new NaviPoi("新三余公园(南5环)", new LatLng(40.070882 ,126.319429), "");
            aMapNavi.calculateDriveRoute(start ,end, null,
                    PathPlanningStrategy.DRIVING_MULTIPLE_ROUTES_DEFAULT);
        }

        @Override
        public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {
            super.onCalculateRouteSuccess(aMapCalcRouteResult);
            HashMap<Integer, AMapNaviPath> mHashMap = aMapNavi.getNaviPaths();
            Log.i("路的条数", mHashMap.size() + "  success" + aMapCalcRouteResult.getCalcRouteType());
            // 设置成功之后,开始模拟导航
            aMapNavi.startNavi(NaviType.NONE);
            aMapNavi.setUseInnerVoice(true, true);
            aMapNavi.startSpeak();
        }

        // 位置更新
        @Override
        public void onNaviInfoUpdate(NaviInfo naviInfo) {
            super.onNaviInfoUpdate(naviInfo);
            Log.i("GuideActivity", "data ------>" + naviInfo.getCurrentRoadName() +
                    "\n" +
                    naviInfo.getCurStep());
        }
    };

}
