package com.boun.volkanyilmaz.relica;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by volkanyilmaz on 13/03/18.
 */
public class MyAdapter extends RecyclerView.Adapter {

    private List<MemoryModel> modelList;
    private Context context;

    public MyAdapter(List<MemoryModel> modelList,Context c) {
        //yapıcı metoda (Constructor) gelen context ve tweet bilgilerini sınıf değişkenlerine değer olarak atıyoruz
        this.modelList = modelList;
        this.context=c;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
