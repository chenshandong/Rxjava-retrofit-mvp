package com.chensd.funnydemo.ui;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.chensd.funnydemo.MyApplication;
import com.chensd.funnydemo.R;
import com.chensd.funnydemo.adapter.RecyclerAdapter;
import com.chensd.funnydemo.model.ImageInfo;
import com.chensd.funnydemo.presenter.RecentFragmentPresenter;
import com.chensd.funnydemo.utils.BitmapUtil;
import com.chensd.funnydemo.utils.WxShareUtil;
import com.chensd.funnydemo.view.BaseView;
import com.chensd.funnydemo.view.MvpFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecentFragment extends MvpFragment<RecentFragmentPresenter> implements BaseView {

    @Bind(R.id.recentSwipRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.recentFrgRv)
    RecyclerView recentFrgRv;
    private RecyclerAdapter mAdapter;

    public RecentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        ButterKnife.bind(this, view);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setColorSchemeColors(Color.GREEN, Color.GREEN);

        mAdapter = new RecyclerAdapter();
        recentFrgRv.setLayoutManager(new GridLayoutManager(mActivity, 2));
        recentFrgRv.setAdapter(mAdapter);

        // TODO: 2017/1/18
        mAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, final int position) {

                onGridItemClick(v, position, mAdapter);

//                WxShareUtil.showShareDialog(mActivity, "分享", ((ImageInfo) mAdapter.getItem(position)).getImage_url(), new WxShareUtil.OnShareBtnClick() {
//                    @Override
//                    public void shareBtnClick(View v, Drawable drawable) {
//
//                        Bitmap bmp = null;
//                        if (drawable instanceof GlideBitmapDrawable) {
//                            bmp = ((GlideBitmapDrawable) drawable).getBitmap();
//                        } else if (drawable instanceof GifDrawable) {
//                            bmp = ((GifDrawable) drawable).getFirstFrame();
//                        } else {
//                            String name = drawable.getClass().getName();
//                            Log.e("weixin", "name=" + name);
//                        }
//                        Log.e("weixin", "width=" + bmp.getWidth() + ",height=" + bmp.getHeight() + "大小：" + bmp.getByteCount() / 1024 + "kb");
//
//                        if (bmp != null) {
//
//                            final Bitmap finalBmp = bmp;
//                            Observable.create(new Observable.OnSubscribe<Bitmap>() {
//
//                                @Override
//                                public void call(Subscriber<? super Bitmap> subscriber) {
//                                    if ((finalBmp.getByteCount()/1024/1024) > 3){//大于3m压缩下
//                                        Bitmap bitmap = BitmapUtil.creatSmallBitmap(finalBmp, 5, false);
//                                        subscriber.onNext(bitmap);
//                                    }else{
//                                        subscriber.onNext(finalBmp);
//                                    }
//                                }
//                            })
//                                    .subscribeOn(Schedulers.io())
//                                    .observeOn(Schedulers.io())
//                                    .subscribe(new Action1<Bitmap>() {
//                                        @Override
//                                        public void call(Bitmap bitmap) {
//                                            //分享到微信
//                                            WxShareUtil.shareBitmap(mActivity.wxapi, bitmap);
//
//                                            //更新到数据库
////                                            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
////                                                @Override
////                                                public void execute(Realm realm) {
////                                                    ImageInfo item = (ImageInfo) mAdapter.getItem(position);
////                                                    item.setTime(System.currentTimeMillis());
////                                                    realm.copyToRealmOrUpdate(item);
////                                                }
////                                            });
//                                        }
//                                    });
//                        }
//
////                        更新到数据库
//                        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
//                            @Override
//                            public void execute(Realm realm) {
//                                ImageInfo item = (ImageInfo) mAdapter.getItem(position);
//                                item.setTime(System.currentTimeMillis());
//                                realm.copyToRealmOrUpdate(item);
//                            }
//                        });
//                    }
//                });
            }
        });
        return view;
    }

    private void onGridItemClick(View v, int position, RecyclerAdapter mAdapter) {
        mvpPresenter.onItemViewClick(mActivity, v, position, mAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter.findDataFromDB();
    }

    @Override
    protected RecentFragmentPresenter createPresenter() {
        return new RecentFragmentPresenter(this);
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
        //通知适配器更新
        mAdapter.setImages(info);
    }

    @Override
    public void getDataFailure(String msg) {
        showtToast(msg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
