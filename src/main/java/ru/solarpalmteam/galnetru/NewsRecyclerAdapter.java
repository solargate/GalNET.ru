package ru.solarpalmteam.galnetru;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.solarpalmteam.galnetru.rss.RSSItem;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder>{

    private List<RSSItem> listContent;

    //DatabaseEngine de;

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;

        public NewsViewHolder(View v) {
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.tv_news_title);
        }
    }

    public NewsRecyclerAdapter(List<RSSItem> listContent) {
        //this.listContent = de.readContentAll();
        this.listContent = listContent;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler_item, parent, false);
        NewsViewHolder nvh = new NewsViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.tvTitle.setText(listContent.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return listContent.size();
    }
}
