package com.eatda.kakaomap;

import static androidx.core.location.LocationManagerCompat.getCurrentLocation;

import static com.kakao.vectormap.camera.CameraUpdateFactory.newCenterPosition;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.widget.Toast;

import com.eatda.R;
import com.eatda.data.api.goodInfluenceStore.GoodInfluenceStoreApiService;
import com.eatda.data.api.goodInfluenceStore.GoodInfluenceStoreRetrofitClient;
import com.eatda.kakaomap.goodInfluenceStore.ApiResponse;
import com.eatda.kakaomap.goodInfluenceStore.StoreInfo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.kakao.vectormap.KakaoMap;
import com.kakao.vectormap.KakaoMapReadyCallback;
import com.kakao.vectormap.KakaoMapSdk;
import com.kakao.vectormap.LatLng;
import com.kakao.vectormap.MapLifeCycleCallback;
import com.kakao.vectormap.MapView;
import com.kakao.vectormap.camera.CameraUpdate;
import com.kakao.vectormap.camera.CameraUpdateFactory;
import com.kakao.vectormap.label.Label;
import com.kakao.vectormap.label.LabelLayer;
import com.kakao.vectormap.label.LabelOptions;
import com.kakao.vectormap.label.LabelStyle;
import com.kakao.vectormap.label.LabelStyles;
import com.kakao.vectormap.label.LabelTextBuilder;
import com.kakao.vectormap.label.TransformMethod;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyLocationMap extends AppCompatActivity {

    private MapView mapView;
    private KakaoMap kakaoMap;
    private FusedLocationProviderClient fusedLocationClient;    //현재위치 사용
    private final String[] locationPermissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private LatLng currentLocation;  // 현재 위치를 저장할 변수
    private LocationManager locationManager;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location_map);

        // 네이티브 앱키
        KakaoMapSdk.init(this, "450f27ad0485316934257dcaa021f621"); // 자신의 앱키로 변경

        // 위치 권한 요청
        requestLocationPermission();
        //위치 서비스 클라이언트 만들기
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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

                // '내 위치 주변 보기' 버튼
                Button btn_my_location = findViewById(R.id.btn_my_location);
                btn_my_location.setOnClickListener(v -> moveToCurrentLocation());
            }
        });
        // 현재 위치 업데이트를 위한 LocationManager 초기화
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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

    // 가게 마커 붙이기
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

                    if (labelPosition.equals(position)) {
                        showStoreDetails(store);
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

    // 위치 권한 요청
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 허용되지 않은 경우 권한 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // 권한이 이미 허용된 경우 현재 위치 가져오기
            getCurrentLocation();
        }
    }


    // 권한 요청에 대한 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없으므로 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        // 현재 위치 좌표 저장
                        LatLng currentLocation =LatLng.from(location.getLatitude(), location.getLongitude());
                        updateMapLocation(currentLocation);
                        addCurrentLocationLabel(currentLocation); // 현재 위치 마커 추가
                    } else {
                        // 위치가 없는 경우 예외 처리
                        Toast.makeText(this, "현재 위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(this, e -> {
                    // 위치 가져오기 실패 시 처리
                    Toast.makeText(this, "위치 정보에 접근할 수 없습니다: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateMapLocation(LatLng currentLocation) {
        if (kakaoMap != null) {
            kakaoMap.moveCamera(newCenterPosition(currentLocation, 15));
        }
    }

    private void addCurrentLocationLabel(LatLng currentLocation) {

        LabelStyles styles = kakaoMap.getLabelManager()
                .addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.my_location_icon)));

        LabelOptions options = LabelOptions.from(currentLocation)
                .setStyles(styles)
                .setTransform(TransformMethod.Default);
        LabelLayer layer = kakaoMap.getLabelManager().getLayer();

        Label label = layer.addLabel(options);
        label.setTag(currentLocation); // 라벨에 가게 정보 저장
    }

    private void moveToCurrentLocation() {
        // 위치 권한이 허용되었는지 확인
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // 현재 위치 가져오기
            getCurrentLocation();
        } else {
            Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
        }
    }
}