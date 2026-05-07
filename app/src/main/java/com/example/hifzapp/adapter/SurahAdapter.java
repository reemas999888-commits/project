package com.example.hifzapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hifzapp.R;
import com.example.hifzapp.model.Surah;

import java.util.List;

public class SurahAdapter extends RecyclerView.Adapter<SurahAdapter.SurahViewHolder> {

    private List<Surah> surahList;
    private OnSurahClickListener listener;

    public interface OnSurahClickListener {
        void onSurahClick(Surah surah);
    }

    public SurahAdapter(List<Surah> surahList, OnSurahClickListener listener) {
        this.surahList = surahList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SurahViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_surah, parent, false);
        return new SurahViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SurahViewHolder holder, int position) {
        Surah surah = surahList.get(position);
        holder.tvSurahName.setText(surah.getName());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSurahClick(surah);
            }
        });
    }

    @Override
    public int getItemCount() {
        return surahList.size();
    }

    public void updateList(List<Surah> newList) {
        this.surahList = newList;
        notifyDataSetChanged();
    }

    public static class SurahViewHolder extends RecyclerView.ViewHolder {
        TextView tvSurahName;

        public SurahViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSurahName = itemView.findViewById(R.id.tvSurahName);
        }
    }
}