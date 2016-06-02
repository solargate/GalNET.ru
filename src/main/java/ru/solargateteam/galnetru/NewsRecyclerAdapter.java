package ru.solargateteam.galnetru;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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
import ru.solargateteam.galnetru.pref.PrefEngine;
import ru.solargateteam.galnetru.util.Util;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder> {

    private List<DBItem> listContent;

    Context mContext;

    public void switchPost(int id, Fragment fragment) {
        if (mContext == null) {
            return;
        }
        if (mContext instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) mContext;
            Fragment frag = fragment;
            mainActivity.switchPost(id, frag);
        }
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public TextView tvPubDate;
        public ImageView ivImage;
        //public TextView tvDescription;
        public CardView cvNews;

        public View view;
        public DBItem currentItem;

        public NewsViewHolder(View v) {
            super(v);

            view = v;

            tvTitle = (TextView) v.findViewById(R.id.tv_news_title);
            tvPubDate = (TextView) v.findViewById(R.id.tv_news_pubdate);
            ivImage = (ImageView) v.findViewById(R.id.iv_news_image);
            //tvDescription = (TextView) v.findViewById(R.id.tv_news_description);
            cvNews = (CardView) v.findViewById(R.id.news_card_view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentPostJump(currentItem);
                }
            });

            PrefEngine pe = new PrefEngine(v.getContext());
            if (pe.useGalNETFont()) {
                Typeface face = Typeface.createFromAsset(v.getContext().getAssets(), Global.FONT_JURA_BOLD);
                tvTitle.setTypeface(face);
                tvPubDate.setTypeface(face);
                //tvDescription.setTypeface(face);
            }
        }

        private void fragmentPostJump(DBItem itemSelected) {
            PostFragment fragmentPost = new PostFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(PostFragment.PARAM_ITEM, itemSelected);
            fragmentPost.setArguments(bundle);
            switchPost(R.id.fMain, fragmentPost);
        }

    }

    public NewsRecyclerAdapter(Context context, List<DBItem> listContent) {
        this.mContext = context;
        this.listContent = listContent;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler_item, parent, false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {

        holder.tvTitle.setText(Util.strProcessHTML(listContent.get(position).getTitle()));

        holder.tvPubDate.setText(Util.makeDateStringFromUnixTime(listContent.get(position).getPubDate()));

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

        //holder.tvDescription.setText(Util.strProcessHTML(listContent.get(position).getDescription()));

        // TODO: Выделение цветом новых постов
        //holder.cvNews.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorFront));

        holder.currentItem = listContent.get(position);
    }

    @Override
    public int getItemCount() {
        return listContent.size();
    }
}
