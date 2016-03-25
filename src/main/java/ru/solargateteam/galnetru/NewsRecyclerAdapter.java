package ru.solargateteam.galnetru;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

        public NewsViewHolder(View v) {
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.tv_news_title);
            ivImage = (ImageView) v.findViewById(R.id.iv_news_image);
            tvDescription = (TextView) v.findViewById(R.id.tv_news_description);
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
        }

        holder.tvDescription.setText(listContent.get(position).getLink());
    }

    @Override
    public int getItemCount() {
        return listContent.size();
    }
}
