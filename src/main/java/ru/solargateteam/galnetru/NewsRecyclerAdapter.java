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
import java.io.FileNotFoundException;
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

        // TEST
        Log.d(Global.TAG, "HOLDER IMAGE 1");
        if (listContent.get(position).getImagePath() != null) {
            Log.d(Global.TAG, "HOLDER IMAGE 2");
            try {
                Log.d(Global.TAG, "HOLDER IMAGE 3");
                File imageFile = new File(listContent.get(position).getImagePath(), listContent.get(position).getId() + ".png");
                Log.d(Global.TAG, "HOLDER IMAGE 4");
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(imageFile));
                Log.d(Global.TAG, "HOLDER IMAGE 5");
                holder.ivImage.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }

            //holder.ivImage.setImageBitmap(BitmapFactory.decodeByteArray(listContent.get(position).getImage(), 0, listContent.get(position).getImage().length));

        }
        Log.d(Global.TAG, "HOLDER IMAGE 6");

        /*
        Log.d(Global.TAG, "HOLDER IMAGE 1");
        if (listContent.get(position).getImage() != null) {
            Log.d(Global.TAG, "HOLDER IMAGE 2");
            holder.ivImage.setImageBitmap(BitmapFactory.decodeByteArray(listContent.get(position).getImage(), 0, listContent.get(position).getImage().length));
            Log.d(Global.TAG, "HOLDER IMAGE 3");
        }
        Log.d(Global.TAG, "HOLDER IMAGE 4");
        */

        holder.tvDescription.setText(listContent.get(position).getLink());
    }

    @Override
    public int getItemCount() {
        return listContent.size();
    }
}
