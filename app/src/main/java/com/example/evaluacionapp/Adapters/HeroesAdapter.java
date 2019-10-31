package com.example.evaluacionapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluacionapp.Models.ListHeroes;
import com.example.evaluacionapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HeroesAdapter extends RecyclerView.Adapter<HeroesAdapter.ViewHolder> {

    private List<ListHeroes> list;
    private int layout;
    private OnItemClickListener listener;
    private ConstraintLayout constraintLayout;

    public HeroesAdapter(List<ListHeroes> list, int layout, OnItemClickListener listener) {
        this.list = list;
        this.layout = layout;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        constraintLayout = view.findViewById(R.id.cardHero);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.data(list.get(position).getImage(), list.get(position).getName(), list.get(position).getDetail(), listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewHero;
        private TextView textViewNameHero;
        private TextView textViewDetailHero;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewHero = itemView.findViewById(R.id.imageViewHero);
            textViewNameHero = itemView.findViewById(R.id.textViewHeroName);
            textViewDetailHero = itemView.findViewById(R.id.textViewHeroDetail);
        }

        private void data(String image, String name, String detail, final OnItemClickListener listener){
            Picasso.get().load(image + ".jpg").fit().centerCrop().into(imageViewHero);
            textViewNameHero.setText(name);
            if(detail.isEmpty())
                textViewDetailHero.setText("Sin descripcion");
            else
                textViewDetailHero.setText(detail);

            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
