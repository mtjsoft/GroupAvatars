package cn.mtjsoft.groupavatarslib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

import static cn.mtjsoft.groupavatarslib.utils.DisplayUtils.dp2px;

/**
 * @author mtj
 * @date 2021/10/21
 * @desc
 * @email mtjsoft3@gmail.com
 */
public class FileUtils {
    public static Bitmap getRoundBitmap(Context context, int width, int roundPx, String surname, int color, int textSize) {
        Bitmap output = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        // 去锯齿
        paint.setAntiAlias(true);
        Rect rect = new Rect(0, 0, width, width);
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color == 0 ? Color.GRAY : color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(output, rect, rect, paint);
        // 设置圆内字体白色
        paint.setColor(Color.WHITE);
        // sans serif字体类型
        paint.setTypeface(Typeface.SANS_SERIF);
        // 设置字体的大小
        paint.setTextSize(textSize);
        // x位于字符串中心
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
        float offHeight = fontTotalHeight / 2 - fontMetrics.bottom;
        // 计算出字符串中心的y坐标
        float newHeight = width / 2 + offHeight;
        // 画姓
        canvas.drawText(surname, width / 2, newHeight, paint);
        return output;
    }
}
