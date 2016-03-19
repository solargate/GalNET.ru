package ru.solargateteam.galnetru;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.solargateteam.galnetru.rss.RSSItem;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder>{

    private List<RSSItem> listContent;

    public static class NewsViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public TextView tvDescription;

        public NewsViewHolder(View v) {
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.tv_news_title);
            tvDescription = (TextView) v.findViewById(R.id.tv_news_description);
        }
    }

    public NewsRecyclerAdapter(List<RSSItem> listContent) {
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
        holder.tvDescription.setText(listContent.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return listContent.size();
    }
}
