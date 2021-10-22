package cn.mtjsoft.groupavatarslib.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.mtjsoft.groupavatarslib.cache.disklrucache.DiskLruCache;
import cn.mtjsoft.groupavatarslib.utils.thread.ThreadPoolUtils;

/**
 * @author mtj
 *
 * 处理群头像的bitmap缓存，防止刷新列表时重复绘制
 */
public class DiskLruCacheHelper {
    private static DiskLruCacheHelper mDiskLruCacheHelper;

    /**
     * 缓存的路径目录
     */
    private static final String UNIQUE_NAME = "groupAvatars";

    /**
     * 缓存的大小，10 * 1024 * 1024 = 10M
     */
    private static final int CACHE_SIZE = 10 * 1024 * 1024;

    private DiskLruCache diskLruCache;

    public static DiskLruCacheHelper init() {
        if (mDiskLruCacheHelper == null) {
            synchronized (DiskLruCacheHelper.class) {
                if (mDiskLruCacheHelper == null) {
                    mDiskLruCacheHelper = new DiskLruCacheHelper();
                }
            }
        }
        return mDiskLruCacheHelper;
    }

    public void setDiskLruCachePath(Context context) {
        //四个参数分别为，1.缓存的路径目录 2.版本号 3.每个节点对应的数据个数，4.缓存的大小，10 * 1024 * 1024 = 10M
        try {
            if (diskLruCache == null) {
                synchronized (DiskLruCacheHelper.class) {
                    if (diskLruCache == null) {
                        diskLruCache = DiskLruCache.open(getCachFile(context, UNIQUE_NAME), 1, 1, CACHE_SIZE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取缓存目录
     *
     * @param uniqueName 指定目录下的文件名
     */
    private File getCachFile(Context context, String uniqueName) {
        String catchPath;
        //有内存卡，并且内存卡没有正在移除，就把文件缓存到内存卡中
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
            !Environment.isExternalStorageRemovable()) {
            catchPath = context.getExternalCacheDir().getPath();
        } else {
            catchPath = context.getCacheDir().getPath();
        }
        return new File(catchPath + File.separator + uniqueName);
    }

    /**
     * 将bitmap写入缓存
     */
    public void addBitmapToDiskCache(String key, Bitmap bitmap) {
        if (diskLruCache != null) {
            ThreadPoolUtils.execute(() -> {
                DiskLruCache.Editor editor = null;
                OutputStream outputStream = null;
                try {
                    //创建 Editor 对象
                    editor = diskLruCache.edit(key);
                    if (editor != null) {
                        //创建输出流
                        outputStream = editor.newOutputStream(0);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        editor.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        if (editor != null) {
                            editor.abort();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } finally {
                    try {
                        if (outputStream != null) {
                            outputStream.flush();
                            outputStream.close();
                        }
                        diskLruCache.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 从磁盘缓存获取bitmap
     */
    public Bitmap getBitmapFromDiskCache(String key) {
        try (DiskLruCache.Snapshot snapshot = diskLruCache.get(key)) {
            if (snapshot != null) {
                InputStream inputStream = snapshot.getInputStream(0);
                return BitmapFactory.decodeStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 移除key对应的缓存
     *
     * @param key KEY
     */
    public boolean remove(String key) {
        try {
            return diskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 情况磁盘缓存
     */
    public void delete() {
        ThreadPoolUtils.execute(() -> {
            try {
                diskLruCache.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
