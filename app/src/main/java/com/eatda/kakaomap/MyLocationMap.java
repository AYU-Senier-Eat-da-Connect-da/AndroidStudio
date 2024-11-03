package com.eatda.kakaomap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.eatda.R;
import com.eatda.data.api.goodInfluenceStore.GoodInfluenceStoreApiService;
import com.eatda.data.api.goodInfluenceStore.GoodInfluenceStoreRetrofitClient;
import com.eatda.kakaomap.goodInfluenceStore.GoodInfluenceResponse;
import com.eatda.kakaomap.goodInfluenceStore.GoodInfluenceStore;
import com.kakao.vectormap.KakaoMap;
import com.kakao.vectormap.KakaoMapReadyCallback;
import com.kakao.vectormap.KakaoMapSdk;
import com.kakao.vectormap.LatLng;
import com.kakao.vectormap.MapLifeCycleCallback;
import com.kakao.vectormap.MapView;
import com.kakao.vectormap.RoadViewRequest;
import com.kakao.vectormap.label.Label;
import com.kakao.vectormap.label.LabelLayer;

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
                // 가게 데이터 가져오기
                fetchGoodInfluenceData();
            }
        });
    }

    // 가게 데이터 API 호출 메서드
    private void fetchGoodInfluenceData() {
        GoodInfluenceStoreApiService apiService = GoodInfluenceStoreRetrofitClient.getRetrofitInstance().create(GoodInfluenceStoreApiService.class);

        String key = GoodInfluenceStoreApiService.Key; // API 키
        String type = "json"; // 데이터 타입
        int pIndex = 1; // 페이지 인덱스
        int pSize = 10; // 페이지 사이즈

        Call<GoodInfluenceResponse> call = apiService.getGoodInfluenceStores(key, type, pIndex, pSize);

        call.enqueue(new Callback<GoodInfluenceResponse>() {
            @Override
            public void onResponse(Call<GoodInfluenceResponse> call, Response<GoodInfluenceResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GoodInfluenceResponse goodInfluenceResponse = response.body();
                    List<GoodInfluenceStore> stores = goodInfluenceResponse.getGoodInfluenceStores(); // 가게 목록 가져오기

                    if (stores != null && !stores.isEmpty()) {
                        Log.d("MyLocationMap", "Fetched stores: " + stores);
                        addStoreMarkers(stores); // 가게 마커 추가
                    } else {
                        Log.e("MyLocationMap", "가게 목록이 비어있습니다.");
                        showErrorDialog("가게 목록이 비어있습니다.");
                    }
                } else {
                    Log.e("MyLocationMap", "가게 정보를 불러오는 데 실패했습니다.");
                    showErrorDialog("가게 정보를 불러오는 데 실패했습니다.");
                }
            }

            @Override
            public void onFailure(Call<GoodInfluenceResponse> call, Throwable t) {
                Log.e("MyLocationMap", "API 호출 실패: ", t);
                showErrorDialog("가게 정보를 불러오는 데 실패했습니다.");
            }
        });
    }

    // 가게 마커 추가 메서드
    private void addStoreMarkers(List<GoodInfluenceStore> stores) {
        for (GoodInfluenceStore store : stores) {
            double REFINE_WGS84_LAT = store.getREFINE_WGS84_LAT();
            double REFINE_WGS84_LOGT = store.getREFINE_WGS84_LOGT();
            LatLng position = LatLng.from(REFINE_WGS84_LAT, REFINE_WGS84_LOGT);

            Log.d("MyLocationMap", "Adding marker for store: " + store.getCMPNM_NM() + " at position: " + position);

            // 마커 비트맵 생성 및 추가
            Bitmap markerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker_icon2);
            RoadViewRequest roadViewRequest = new RoadViewRequest(position);
            roadViewRequest.addMarker(markerBitmap, position);

            Log.d("MyLocationMap", "Marker added for: " + store.getCMPNM_NM());
        }

        // 클릭 이벤트 설정
        setMarkerClickListener(stores);
    }

    // 마커 클릭 리스너 설정 메서드
    private void setMarkerClickListener(List<GoodInfluenceStore> stores) {
        kakaoMap.setOnLabelClickListener(new KakaoMap.OnLabelClickListener() {
            @Override
            public boolean onLabelClicked(KakaoMap kakaoMap, LabelLayer labelLayer, Label label) {
                for (GoodInfluenceStore store : stores) {
                    double REFINE_WGS84_LAT = store.getREFINE_WGS84_LAT();
                    double REFINE_WGS84_LOGT = store.getREFINE_WGS84_LOGT();
                    LatLng position = LatLng.from(REFINE_WGS84_LAT, REFINE_WGS84_LOGT);

                    if (label.getPosition().equals(position)) {
                        showStoreDetails(store);
                        break;
                    }
                }
                return true;
            }
        });
    }

    // 가게 상세정보 보여주는 메서드
    private void showStoreDetails(GoodInfluenceStore store) {
        String details = "가게 이름: " + store.getCMPNM_NM() +
                "\n영업 시간: " + store.getBSN_TM_NM() +
                "\n도로명 주소: " + store.getREFINE_ROADNM_ADDR() +
                "\n상세 주소: " + store.getDETAIL_ADDR() +
                "\n우편번호: " + store.getREFINE_ZIPNO();

        new AlertDialog.Builder(MyLocationMap.this)
                .setTitle("가게 상세 정보")
                .setMessage(details)
                .setPositiveButton("확인", null)
                .show();
    }

    // 오류 메시지 다이얼로그 표시 메서드
    private void showErrorDialog(String message) {
        new AlertDialog.Builder(MyLocationMap.this)
                .setTitle("오류")
                .setMessage(message)
                .setPositiveButton("확인", null)
                .show();
    }
}