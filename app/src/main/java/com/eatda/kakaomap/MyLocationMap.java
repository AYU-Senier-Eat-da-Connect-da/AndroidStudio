package com.eatda.kakaomap;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.eatda.R;
import com.eatda.data.api.goodInfluenceStore.GoodInfluenceStoreApiService;
import com.eatda.data.api.goodInfluenceStore.GoodInfluenceStoreRetrofitClient;
import com.eatda.kakaomap.goodInfluenceStore.ApiResponse;
import com.eatda.kakaomap.goodInfluenceStore.StoreInfo;
import com.kakao.vectormap.KakaoMap;
import com.kakao.vectormap.KakaoMapReadyCallback;
import com.kakao.vectormap.KakaoMapSdk;
import com.kakao.vectormap.LatLng;
import com.kakao.vectormap.MapLifeCycleCallback;
import com.kakao.vectormap.MapView;
import com.kakao.vectormap.label.Label;
import com.kakao.vectormap.label.LabelLayer;
import com.kakao.vectormap.label.LabelOptions;
import com.kakao.vectormap.label.LabelStyle;
import com.kakao.vectormap.label.LabelStyles;
import com.kakao.vectormap.label.LabelTextBuilder;
import com.kakao.vectormap.label.LabelTextStyle;
import com.kakao.vectormap.label.TransformMethod;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyLocationMap extends AppCompatActivity {

    private MapView mapView;
    private KakaoMap kakaoMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location_map);

        // 네이티브 앱키
        KakaoMapSdk.init(this, "450f27ad0485316934257dcaa021f621");

        mapView = findViewById(R.id.map_view);
        mapView.start(new MapLifeCycleCallback() {
            @Override
            public void onMapDestroy() {
                Log.d("KakaoMap", "onMapDestroy: ");
            }

            @Override
            public void onMapError(Exception error) {
                Log.e("KakaoMap", "onMapError: ", error);
            }
        }, new KakaoMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull KakaoMap map) {
                kakaoMap = map;

                fetchGoodInfluenceData();
            }
        });
    }

    // 가게 데이터 API 호출 메서드
    private void fetchGoodInfluenceData() {
        GoodInfluenceStoreApiService apiService = GoodInfluenceStoreRetrofitClient.getRetrofitInstance()
                .create(GoodInfluenceStoreApiService.class);

        String key = GoodInfluenceStoreApiService.Key;
        String type = "json";
        int pIndex = 1;
        int pSize = 1000;

        Call<ApiResponse> call = apiService.getGoodInfluenceStores(key, type, pIndex, pSize);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<StoreInfo> stores = response.body().getGGGOODINFLSTOREST().get(1).getRow();
                    for (StoreInfo store : stores) {
                        Log.d("StoreInfo", "Name: " + store.getCMPNM_NM()
                                + ", Hours: " + store.getBSN_TM_NM()
                                + ", Road Address: " + store.getREFINE_ROADNM_ADDR()
                                + ", Detailed Address: " + store.getDETAIL_ADDR()
                                + ", Zip Code: " + store.getREFINE_ZIPNO()
                                + ", Latitude: " + store.getREFINE_WGS84_LAT()
                                + ", Longitude: " + store.getREFINE_WGS84_LOGT());
                    }
                    addStoreLabel(stores);
                    setMarkerClickListener(stores); // 마커 클릭 리스너 설정
                } else {
                    Log.e("Error", "Response not successful or body is null");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("Error", "Network error: " + t.getMessage());
            }
        });

    }

    private void addStoreLabel(List<StoreInfo> stores) {
        for (StoreInfo store : stores) {
            Double latitude = store.getREFINE_WGS84_LAT();  // 위도
            Double longitude = store.getREFINE_WGS84_LOGT();  // 경도

            // 위도와 경도가 null이 아니고 0.0이 아닐 때만 마커 추가
            if (latitude != null && longitude != null && latitude != 0.0 && longitude != 0.0) {
                LatLng position = LatLng.from(latitude, longitude);
                Log.d("MyLocationMap", "Adding marker for store: " + store.getCMPNM_NM() + " at position: " + position);

                LabelStyles styles = kakaoMap.getLabelManager().addLabelStyles(
                        LabelStyles.from(
                                LabelStyle.from(R.drawable.marker_icon)
                                        .setZoomLevel(0),

                                LabelStyle.from(R.drawable.marker_icon)
                                        .setTextStyles(20, Color.BLACK)
                                        .setZoomLevel(11),

                                LabelStyle.from(R.drawable.marker_icon)
                                        .setTextStyles(22, Color.BLACK)
                                        .setZoomLevel(12),

                                LabelStyle.from(R.drawable.marker_icon)
                                        .setTextStyles(23, Color.BLACK)
                                        .setZoomLevel(13),

                                LabelStyle.from(R.drawable.marker_icon)
                                        .setTextStyles(24, Color.BLACK)
                                        .setZoomLevel(14),

                                LabelStyle.from(R.drawable.marker_icon)
                                        .setTextStyles(25, Color.BLACK)
                                        .setZoomLevel(15),

                                LabelStyle.from(R.drawable.marker_icon)
                                        .setTextStyles(27, Color.BLACK)
                                        .setZoomLevel(16),

                                LabelStyle.from(R.drawable.marker_icon)
                                        .setTextStyles(30, Color.BLACK)
                                        .setZoomLevel(17)
                        )
                );

                LabelOptions options = LabelOptions.from(position)
                        .setStyles(styles)
                        .setTransform(TransformMethod.Default);
                LabelLayer layer = kakaoMap.getLabelManager().getLayer();

                Label label = layer.addLabel(options);
                LabelTextBuilder labelTextBuilder = new LabelTextBuilder().setTexts(store.getCMPNM_NM());

                label.changeText(labelTextBuilder);
                label.setTag(store);

                Log.e("MyLocationMap", "Marker added for: " + store.getCMPNM_NM());
            } else {
                Log.e("MyLocationMap", "Invalid location for store: " + store.getCMPNM_NM() + " - Latitude: " + latitude + ", Longitude: " + longitude);
            }
        }
    }

    private void setMarkerClickListener(List<StoreInfo> stores) {
        kakaoMap.setOnLabelClickListener(new KakaoMap.OnLabelClickListener() {
            @Override
            public boolean onLabelClicked(KakaoMap kakaoMap, LabelLayer labelLayer, Label label) {
                LatLng labelPosition = label.getPosition();

                for (StoreInfo store : stores) {
                    double REFINE_WGS84_LAT = store.getREFINE_WGS84_LAT();
                    double REFINE_WGS84_LOGT = store.getREFINE_WGS84_LOGT();
                    LatLng position = LatLng.from(REFINE_WGS84_LAT, REFINE_WGS84_LOGT);

]                    if (labelPosition.equals(position)) {
                        showStoreDetails(store);]
                        break;
                    }
                }
                return true;
            }
        });
    }

    private void showStoreDetails(StoreInfo store) {
        String details = "가게 이름: " + store.getCMPNM_NM() +
                "\n\n영업 시간: " + store.getBSN_TM_NM() +
                "\n\n도로명 주소: " + store.getREFINE_ROADNM_ADDR() +
                "\n\n상세 주소: " + store.getDETAIL_ADDR() +
                "\n\n우편번호: " + store.getREFINE_ZIPNO();
        new AlertDialog.Builder(this)
                .setTitle("가게 상세 정보")
                .setMessage(details)
                .setPositiveButton("닫기", null)
                .show();
    }
}