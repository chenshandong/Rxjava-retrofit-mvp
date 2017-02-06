package com.chensd.funnydemo.ui;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.chensd.funnydemo.MyApplication;
import com.chensd.funnydemo.R;
import com.chensd.funnydemo.adapter.RecyclerAdapter;
import com.chensd.funnydemo.model.ImageInfo;
import com.chensd.funnydemo.presenter.MainFragmentPresenter;
import com.chensd.funnydemo.utils.BitmapUtil;
import com.chensd.funnydemo.utils.FileUtil;
import com.chensd.funnydemo.utils.OkHttpClientManager;
import com.chensd.funnydemo.utils.WxShareUtil;
import com.chensd.funnydemo.view.BaseView;
import com.chensd.funnydemo.view.MvpFragment;
import com.chensd.funnydemo.wxapi.WXEntryActivity;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.Call;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends MvpFragment<MainFragmentPresenter> implements BaseView {

    @Bind(R.id.mainSwipRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.mainFrgRv)
    RecyclerView mainFrgRv;

    @Bind(R.id.fragment_search_edt)
    EditText searchView;

    private RecyclerAdapter mAdapter;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setColorSchemeColors(Color.GREEN, Color.BLUE);
        searchView.clearFocus();
        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_UP) {
                    String key = searchView.getText().toString().trim();
                    if (!TextUtils.isEmpty(key)) {
                        mActivity.showSoftInput(false);
                        mvpPresenter.loadImage(key);
                    }
                }

                return true;
            }
        });

        mAdapter = new RecyclerAdapter();
        mainFrgRv.setLayoutManager(new GridLayoutManager(mActivity, 2));
        mainFrgRv.setAdapter(mAdapter);

        // TODO: 2017/1/18  
        mAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, final int position) {

                onGridItemClick(v, position, mAdapter);
            }
        });

        return view;
    }

    private void onGridItemClick(View v, final int position, final RecyclerAdapter mAdapter) {
        mvpPresenter.onItemViewClick(mActivity, v, position, mAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter.loadImage("装逼");
    }

    @Override
    protected MainFragmentPresenter createPresenter() {
        return new MainFragmentPresenter(this);
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void getDataSuccess(List<ImageInfo> info) {
        //通知适配器更新显示
        mAdapter.setImages(info);
    }

    @Override
    public void getDataFailure(String msg) {
        showtToast(msg);
    }

    @OnClick(R.id.fragment_main_search_tv)
    public void search() {
        String key = searchView.getText().toString().trim();
        if (!TextUtils.isEmpty(key)) {
            mActivity.showSoftInput(false);
            mvpPresenter.loadImage(key);
        }
    }
}
