package ru.solargateteam.galnetru;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import ru.solargateteam.galnetru.db.DBItem;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder> {

    private List<DBItem> listContent;

    public static class NewsViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public ImageView ivImage;
        public TextView tvDescription;

        public View view;
        public DBItem currentItem;

        public NewsViewHolder(View v) {
            super(v);

            view = v;

            tvTitle = (TextView) v.findViewById(R.id.tv_news_title);
            ivImage = (ImageView) v.findViewById(R.id.iv_news_image);
            tvDescription = (TextView) v.findViewById(R.id.tv_news_description);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), PostActivity.class);
                    intent.putExtra(PostActivity.PARAM_ITEM, currentItem);
                    v.getContext().startActivity(intent);
                }
            });

            Typeface face = Typeface.createFromAsset(v.getContext().getAssets(), Global.FONT_JURA_BOLD);
            tvTitle.setTypeface(face);
            tvDescription.setTypeface(face);
        }
    }

    public NewsRecyclerAdapter(List<DBItem> listContent) {
        this.listContent = listContent;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler_item, parent, false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {

        holder.tvTitle.setText(listContent.get(position).getTitle());

        if (listContent.get(position).getImagePath() != null) {
            try {
                File imageFile = new File(listContent.get(position).getImagePath(), listContent.get(position).getId() + ".png");
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(imageFile));
                holder.ivImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder.ivImage.setImageResource(R.drawable.image_no_banner);
        }

        holder.tvDescription.setText(listContent.get(position).getLink());

        holder.currentItem = listContent.get(position);

        //Typeface face = Typeface.createFromAsset(holder.view.get)
    }

    @Override
    public int getItemCount() {
        return listContent.size();
    }
}
