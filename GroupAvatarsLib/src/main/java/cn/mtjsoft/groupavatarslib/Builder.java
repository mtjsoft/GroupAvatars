package cn.mtjsoft.groupavatarslib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import cn.mtjsoft.groupavatarslib.cache.DiskLruCacheHelper;
import cn.mtjsoft.groupavatarslib.cache.LruCacheHelper;
import cn.mtjsoft.groupavatarslib.layout.DingLayoutManager;
import cn.mtjsoft.groupavatarslib.layout.ILayoutManager;
import cn.mtjsoft.groupavatarslib.layout.WechatLayoutManager;
import cn.mtjsoft.groupavatarslib.utils.MD5Util;
import cn.mtjsoft.groupavatarslib.utils.thread.ThreadPoolUtils;

import static cn.mtjsoft.groupavatarslib.utils.DisplayUtils.dp2px;

/**
 * @author mtj
 * Date: 2021-10-21 15:38:45
 * <p>
 * 群头像参数设置
 */
public class Builder {
    private static final int LOAD_END = 0x09;

    private final WeakReference<Context> context;

    private WeakReference<ImageView> imageView;

    /**
     * 最终生成bitmap的尺寸
     * dp
     */
    private int size;

    /**
     * 根据 groupId 和 datas 内容，获取的md5，用于缓存
     */
    private String md5 = "";

    /**
     * 群ID
     */
    private String groupId = "";

    /**
     * 每个小bitmap之间的距离
     */
    private int gap;

    /**
     * 间距的颜色 即 生成图片的背景色
     */
    private int gapColor;

    /**
     * 获取图片失败时的默认图片
     */
    private int placeholder;

    /**
     * 要加载的资源数量
     */
    private int count;

    /**
     * 单个bitmap的尺寸
     */
    private int subSize;

    /**
     * bitmap的组合样式
     */
    private ILayoutManager layoutManager;

    /**
     * 图像数据
     */
    private List<String> datas;

    /**
     * 生成图像的圆角
     * PX
     */
    private int roundPx = 0;

    /**
     * 昵称生成头像是的背景
     */
    private int nickAvatarColor;

    /**
     * 内部小头像的圆角
     */
    private int childAvatarRoundPx = 0;

    /**
     * 昵称生成头像时的文字大小 dp
     */
    private int nickTextSize = 0;

    /**
     * 使用glide加载出所需bitmap
     */
    private int overCount = 0;

    private Object[] mObjects;

    public Builder(Context context) {
        this.context = new WeakReference<>(context);
        DiskLruCacheHelper.init().setDiskLruCachePath(context.getApplicationContext());
    }

    public Builder setImageView(ImageView imageView) {
        this.imageView = new WeakReference<>(imageView);
        return this;
    }

    /**
     * 设置groupId，用于缓存
     */
    public Builder setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public Builder setNickAvatarColor(@ColorRes int nickAvatarColor) {
        this.nickAvatarColor = ContextCompat.getColor(context.get(), nickAvatarColor);
        return this;
    }

    public Builder setSize(int size) {
        this.size = dp2px(context.get(), size);
        return this;
    }

    public Builder setGap(int gap) {
        this.gap = dp2px(context.get(), gap);
        return this;
    }

    public Builder setGapColor(@ColorRes int gapColor) {
        this.gapColor = ContextCompat.getColor(context.get(), gapColor);
        return this;
    }

    public Builder setPlaceholder(int placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public Builder setLayoutManager(ILayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        return this;
    }

    public Builder setDatas(List<String> datas) {
        this.datas = datas;
        if (datas != null) {
            this.count = datas.size();
        }
        return this;
    }

    public Builder setRound(int round) {
        this.roundPx = dp2px(context.get(), round);
        return this;
    }

    public Builder setChildAvatarRound(int childAvatarRound) {
        this.childAvatarRoundPx = dp2px(context.get(), childAvatarRound);
        return this;
    }

    public Builder setNickTextSize(int nickTextSize) {
        this.nickTextSize = dp2px(context.get(), nickTextSize);
        return this;
    }

    public void build() {
        if (this.datas == null) {
            throw new RuntimeException("datas cant not is null");
        }
        // 生成md5用于缓存的key
        createKey();
        // 先从内存缓存中查找
        Bitmap bitmap = LruCacheHelper.init().getBitmapFromMemCache(md5);
        if (bitmap != null) {
            // 缓存中存在，直接显示，不再生成bitmap了
            showImage(bitmap, true);
            return;
        }
        // 再从磁盘缓存中取
        Bitmap bitmapDisk = DiskLruCacheHelper.init().getBitmapFromDiskCache(md5);
        if (bitmapDisk != null) {
            // 磁盘缓存中存在，直接显示，不再生成bitmap了
            showImage(bitmapDisk, true);
            // 将磁盘缓存写入到内存缓存
            LruCacheHelper.init().addBitmapToMemoryCache(md5, bitmapDisk);
            return;
        }
        if (layoutManager == null) {
            layoutManager = new WechatLayoutManager();
        }
        if (layoutManager instanceof DingLayoutManager && count > 4) {
            this.datas = this.datas.subList(0, 4);
            this.count = 4;
        } else if (layoutManager instanceof WechatLayoutManager && count > 9) {
            this.datas = this.datas.subList(0, 9);
            this.count = 9;
        }
        subSize = getSubSize(size, gap, layoutManager, count);
        // 根据所有参数，生成Bitmap[]数组
        overCount = 0;
        recycleBitmap();
        mObjects = new Object[count];
        boolean isDing = layoutManager instanceof DingLayoutManager;
        for (int i = 0; i < count; i++) {
            // 网络图片，使用glide获取bitmap
            if (datas.get(i).startsWith("http://") || datas.get(i).startsWith("https://")) {
                loadBitmapGlide(isDing, i);
            } else {
                Message message = mHandler.obtainMessage(LOAD_END, i, 0, getShortNickName(datas.get(i)));
                mHandler.sendMessage(message);
            }
        }
    }

    /**
     * 使用Glide 从网络加载图片
     *
     * @param isDing   是否是钉钉群头像类型
     * @param position 数组下标
     */
    @SuppressLint("CheckResult")
    private void loadBitmapGlide(boolean isDing, final int position) {
        RequestBuilder<Bitmap> requestBuilder =
            Glide.with(context.get())
                .asBitmap()
                .load(datas.get(position))
                .override(subSize, subSize)
                .centerCrop()
                // 禁止掉glide内存和磁盘缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(placeholder)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target,
                        boolean isFirstResource) {
                        Message message = mHandler.obtainMessage(LOAD_END, position, 0,
                            BitmapFactory.decodeResource(context.get().getResources(), placeholder));
                        mHandler.sendMessage(message);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource,
                        boolean isFirstResource) {
                        Message message = mHandler.obtainMessage(LOAD_END, position, 0, resource);
                        mHandler.sendMessage(message);
                        return false;
                    }
                });
        if (!isDing && childAvatarRoundPx > 0) {
            requestBuilder.apply(RequestOptions.bitmapTransform(new RoundedCorners(childAvatarRoundPx)));
        }
        requestBuilder.submit(subSize, subSize);
    }

    /**
     * 根据设置的群ID以及设置的头像数据，计算一个md5值，作为缓存的key
     * 当群ID和数据变化时，就会重新绘制，否则优先从内存缓存中获取
     */
    private void createKey() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(groupId).append("_");
        for (String s : datas) {
            stringBuilder.append(s).append("_");
        }
        md5 = MD5Util.stringMD5(stringBuilder.toString());
    }

    /**
     * 根据最终生成bitmap的尺寸，计算单个bitmap尺寸
     */
    private int getSubSize(int size, int gap, ILayoutManager layoutManager, int count) {
        int subSize;
        if (layoutManager instanceof DingLayoutManager) {
            subSize = size;
        } else if (layoutManager instanceof WechatLayoutManager) {
            if (count < 2) {
                subSize = size;
            } else if (count < 5) {
                subSize = (size - 3 * gap) / 2;
            } else {
                subSize = (size - 4 * gap) / 3;
            }
        } else {
            throw new IllegalArgumentException("Must use DingLayoutManager or WechatRegionManager!");
        }
        return subSize;
    }

    /**
     * @return 获取昵称的简称
     */
    private static String getShortNickName(String name) {
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        if (name.length() <= 2) {
            return name;
        }
        return name.substring(name.length() - 2);
    }

    private synchronized void recycleBitmap() {
        if (mObjects != null && mObjects.length > 0) {
            for (Object obj : mObjects) {
                if (obj instanceof Bitmap) {
                    ((Bitmap) obj).recycle();
                }
            }
        }
    }

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOAD_END && context.get() != null && imageView.get() != null) {
                mObjects[msg.arg1] = msg.obj;
                overCount++;
                if (overCount == count) {
                    ThreadPoolUtils.execute(() -> {
                        showImage(layoutManager.combineBitmap(size, subSize, gap, gapColor, mObjects, childAvatarRoundPx,
                            nickAvatarColor, nickTextSize > 0 ? nickTextSize : subSize / 4), false);
                    });
                }
            }
        }
    };

    @SuppressLint("CheckResult")
    private synchronized void showImage(Bitmap bitmap, final boolean fromCache) {
        mHandler.post(() -> {
            if (bitmap != null && context.get() != null && imageView.get() != null) {
                if (!fromCache) {
                    // 不是来自缓存，就存入内存缓存中
                    LruCacheHelper.init().addBitmapToMemoryCache(md5, bitmap);
                    // 存入磁盘
                    DiskLruCacheHelper.init().addBitmapToDiskCache(md5, bitmap);
                }
                RequestBuilder<Drawable> requestBuilder = Glide.with(context.get()).load(bitmap)
                    // 禁止掉glide内存和磁盘缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true);
                if (layoutManager instanceof DingLayoutManager) {
                    requestBuilder.apply(RequestOptions.bitmapTransform(new CircleCrop()));
                } else if (layoutManager instanceof WechatLayoutManager) {
                    requestBuilder.apply(RequestOptions.bitmapTransform(new RoundedCorners(roundPx <= 0 ? 1 : roundPx)));
                }
                requestBuilder.into(imageView.get());
            }
        });
    }
}
