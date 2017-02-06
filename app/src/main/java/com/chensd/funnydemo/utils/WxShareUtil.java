package com.chensd.funnydemo.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.chensd.funnydemo.MyApplication;
import com.chensd.funnydemo.R;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXEmojiObject;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by chen on 2017/1/19.
 */
public class WxShareUtil {

    public static final String WEIXIN_ID = "wx2b1fec53aff208d3";

    public interface OnShareBtnClick {
        void shareBtnClick(View v, Drawable img);
    }

    public enum ShareType {
        FRIEND, SESSION
    }

    public static void showShareDialog(Context context, String title, String url, final OnShareBtnClick onShareBtnClick) {
        View view = LayoutInflater.from(context).inflate(R.layout.send_dialog, null);
        final Button btn = (Button) view.findViewById(R.id.share_wx_btn);
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(view)
                .show();
        final ImageView image = (ImageView) view.findViewById(R.id.share_img);
//        Glide.with(context).load(R.drawable.loading).asGif().into(image);
        Glide.with(context).load(url).placeholder(R.drawable.loading).thumbnail(0.1f).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(final GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onShareBtnClick.shareBtnClick(v, resource);
                            }
                        });
                        return false;
                    }
                }).into(image);

    }

    public static void shareBitmap(final IWXAPI api, final Bitmap bmp) {

        try {
            WXImageObject imgObj = new WXImageObject(bmp);
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = imgObj;

            int scall = 200;
//            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, scall, scall, true);
            Bitmap thumbBmp = BitmapUtil.creatDecBitmap(bmp, scall, false);
            while(thumbBmp.getByteCount()/1024 > 55){
                scall -= 10;
//                thumbBmp = Bitmap.createScaledBitmap(thumbBmp, scall, scall, true);
                thumbBmp = BitmapUtil.creatDecBitmap(thumbBmp, scall, true);
//                thumbBmp = BitmapUtil.creatSmallBitmap(thumbBmp, 1.1f, false);
            }
//            Bitmap thumbBmp = BitmapUtil.compressImage(bmp, 32);
            Log.e("weixin_thumb", "缩略图大小："+thumbBmp.getByteCount()/1024+"kb");
//        bmp.recycle();
            msg.thumbData = bmpToByteArray(thumbBmp, true);

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("img");
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneSession;

            api.sendReq(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shareGif(final IWXAPI api, String path){
        WXEmojiObject emoji = new WXEmojiObject();
        emoji.emojiPath = path;

        WXMediaMessage msg = new WXMediaMessage(emoji);
        msg.title = "Emoji Title";
        msg.description = "Emoji Description";

        Bitmap thuBitmap= BitmapFactory.decodeResource(MyApplication.getGlobalApp().getResources(), R.drawable.fun_logo512);
        Bitmap thmBitmap=Bitmap.createScaledBitmap(thuBitmap, 80, 80, true);
        msg.thumbData=bmpToByteArray(thmBitmap, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("emoji");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;;
        api.sendReq(req);
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


}
