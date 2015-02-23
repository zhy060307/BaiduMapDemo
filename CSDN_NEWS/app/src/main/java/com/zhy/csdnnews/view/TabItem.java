package com.zhy.csdnnews.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.csdnnews.R;
import com.zhy.csdnnews.utils.CBundleKey;

public class TabItem extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_item_fragment_main, container, false);
        TextView tv = (TextView) view.findViewById(R.id.id_tip);
        Bundle args = getArguments();
        if (null != args) {
            int pos = args.getInt(CBundleKey.NEWS_TYPE, 0);
            tv.setText(TabPagerAdapter.TITLES[pos]);
        }
        return view;
    }
}
