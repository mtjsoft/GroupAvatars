package cn.mtjsoft.groupavatarslib.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * @author mtj
 *
 * 处理群头像的bitmap缓存，防止刷新列表时重复绘制
 */
public class LruCacheHelper {
    private static LruCache<String, Bitmap> mMemoryCache;

    private static LruCacheHelper mLruCacheHelper;

    public static LruCacheHelper init() {
        if (mLruCacheHelper == null) {
            synchronized (LruCacheHelper.class) {
                if (mLruCacheHelper == null) {
                    mLruCacheHelper = new LruCacheHelper();
                }
            }
        }
        return mLruCacheHelper;
    }

    private LruCacheHelper() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        mMemoryCache.put(key, bitmap);
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
}
