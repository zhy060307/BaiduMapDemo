package com.zhy.csdnnews.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhy.bean.NetworkException;
import com.zhy.bean.NewsItem;
import com.zhy.csdn.Constaint;
import com.zhy.csdn.NewsParser;
import com.zhy.csdnnews.R;
import com.zhy.csdnnews.utils.CConfigKey;

import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;

import static me.maxwin.view.XListView.IXListViewListener;

public class TabItem extends Fragment implements IXListViewListener {

    private int newsType = 1;
    private int curPage = Constaint.NEWS_TYPE_YEJIE;

    private NewsParser parser;

    private XListView xListView;
    private List<NewsItem> newsList = new ArrayList<>();

    private NewsItemAdapter newsItemAdapter;

    public TabItem() {
        parser = new NewsParser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_item_fragment_main, container, false);
        Bundle args = getArguments();
        if (null != args) {
            newsType = args.getInt(CConfigKey.NEWS_TYPE, Constaint.NEWS_TYPE_YEJIE);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        xListView = (XListView) getView().findViewById(R.id.id_xlistView);
        newsItemAdapter = new NewsItemAdapter(getActivity(), newsList);

        xListView.setAdapter(newsItemAdapter);
        xListView.setPullRefreshEnable(true);
        xListView.setPullLoadEnable(true);
        xListView.setXListViewListener(this);
        xListView.startRefresh();

    }


    //下拉刷新
    @Override
    public void onRefresh() {
        new LoadDataTask().execute();
    }

    //上拉加载更多
    @Override
    public void onLoadMore() {
    }

    private class LoadDataTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            try {
                newsList = parser.parserByPage(newsType, curPage);
            } catch (NetworkException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            newsItemAdapter.addAll(newsList);
            newsItemAdapter.notifyDataSetChanged();
            xListView.stopRefresh();
        }
    }
}
