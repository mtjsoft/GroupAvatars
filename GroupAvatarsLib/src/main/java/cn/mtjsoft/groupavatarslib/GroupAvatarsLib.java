package cn.mtjsoft.groupavatarslib;

import android.content.Context;

/**
 * @author mtj
 * Date: 2021-10-21 15:38:45
 *
 * 群头像设置
 */
public class GroupAvatarsLib {
    public static Builder init(Context context) {
        return new Builder(context);
    }
}
