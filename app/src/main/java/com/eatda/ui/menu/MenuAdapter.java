package com.eatda.ui.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eatda.R;
import com.eatda.data.form.menu.MenuResponse;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private ArrayList<MenuResponse> menuList;

    public MenuAdapter(ArrayList<MenuResponse> menuList) {
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuResponse menu = menuList.get(position);
        holder.menuName.setText(menu.getMenuName());
        holder.menuBody.setText(menu.getMenuBody());
        holder.menuPrice.setText(String.format("%,d 원", menu.getPrice()));
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView menuName, menuBody, menuPrice;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            menuName = itemView.findViewById(R.id.menu_name);
            menuBody = itemView.findViewById(R.id.menu_body);
            menuPrice = itemView.findViewById(R.id.menu_price);
        }
    }
}

