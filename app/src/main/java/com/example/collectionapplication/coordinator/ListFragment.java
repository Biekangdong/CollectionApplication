package com.example.collectionapplication.coordinator;import android.content.Context;import android.os.Bundle;import android.support.annotation.Nullable;import android.support.v4.app.Fragment;import android.support.v4.widget.SwipeRefreshLayout;import android.support.v7.widget.LinearLayoutManager;import android.support.v7.widget.RecyclerView;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.TextView;import com.example.collectionapplication.R;import java.util.ArrayList;/** * Created by Administrator on 2016/7/25 0025. */public class ListFragment extends Fragment {    private ArrayList<String> mDatas;    private View view;    private RecyclerView mListView;    private SwipeRefreshLayout mSwipeRefreshLayout;    Context mContext;    @Override    public void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        mContext=getActivity();    }    @Nullable    @Override    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {        view = inflater.inflate(R.layout.list_layout, container, false);        initView();        return view;    }    private void initView() {        mSwipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeLayout);        mListView = (RecyclerView) view.findViewById(R.id.list_view);        initData();        mListView.setLayoutManager(new LinearLayoutManager(mContext));        mListView.setAdapter(new ListAdapter());        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {            @Override            public void onRefresh() {                mSwipeRefreshLayout.setRefreshing(false);            }        });    }    protected void initData()    {        mDatas = new ArrayList<String>();        for (int i = 'A'; i < 'z'; i++)        {            mDatas.add("" + (char) i);        }    }    class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder>    {        @Override        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)        {            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(                    mContext).inflate(R.layout.item, parent,                    false));            return holder;        }        @Override        public void onBindViewHolder(MyViewHolder holder, int position)        {            holder.tv.setText(mDatas.get(position));        }        @Override        public int getItemCount()        {            return mDatas.size();        }        class MyViewHolder extends RecyclerView.ViewHolder        {            TextView tv;            public MyViewHolder(View view)            {                super(view);                tv = (TextView) view.findViewById(R.id.id_num);            }        }    }}