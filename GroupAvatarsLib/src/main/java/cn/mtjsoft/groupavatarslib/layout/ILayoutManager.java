package cn.mtjsoft.groupavatarslib.layout;

import android.graphics.Bitmap;

/**
 * @author mtj
 */
public interface ILayoutManager {
    Bitmap combineBitmap(int size, int subSize, int gap, int gapColor, Object[] mObjects, int childAvatarRoundPx,
        int nickNameBgColor, int nickNameTextSize);
}
