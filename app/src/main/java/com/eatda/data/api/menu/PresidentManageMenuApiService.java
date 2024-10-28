package com.eatda.data.api.menu;

import com.eatda.data.form.menu.MenuRequest;
import com.eatda.data.form.menu.MenuResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PresidentManageMenuApiService {
    @GET("/api/president/findMyMenu/{presidentId}")
    Call<List<MenuResponse>> getMenuByPresidentId(@Path("presidentId") Long presidentId);

    @POST("/api/menu/create/{presidentId}")
    Call<MenuRequest> addMenu(@Body MenuRequest menuRequest,@Path("presidentId") Long presidentId);

    @PUT("/api/menu/{menuId}/update")
    Call<MenuRequest> updateMenu(@Path("menuId") Long menuId, @Body MenuRequest menuRequest);

    @DELETE("/api/menu/{menuId}")
    Call<String> deleteMenu(@Path("menuId") Long menuId);
}
