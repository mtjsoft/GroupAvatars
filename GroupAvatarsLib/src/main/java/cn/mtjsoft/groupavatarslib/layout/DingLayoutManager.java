package cn.mtjsoft.groupavatarslib.layout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * @author mtj
 * Date: 2021-10-21 15:38:45
 *
 * 钉钉群头像布局，最多显示9个
 */
public class DingLayoutManager implements ILayoutManager {
    @Override
    public Bitmap combineBitmap(int size, int subSize, int gap, int gapColor, Object[] mObjects, int childAvatarRoundPx,
        int nickNameBgColor, int nickNameTextSize) {
        Bitmap result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        if (gapColor == 0) {
            gapColor = Color.WHITE;
        }
        Paint paint = new Paint();
        // 去锯齿
        paint.setAntiAlias(true);
        canvas.drawColor(gapColor);
        int count = mObjects.length;
        Bitmap subBitmap = null;
        int[][] dxy = { { 0, 0 }, { 1, 0 }, { 1, 1 }, { 0, 1 } };
        for (int i = 0; i < count; i++) {
            if (mObjects[i] == null) {
                continue;
            }
            paint.setColor(gapColor);
            boolean isBitmap = mObjects[i] instanceof Bitmap;
            int dx = dxy[i][0];
            int dy = dxy[i][1];
            float startX = dx * (size + gap) / 2.0f;
            float startY = dy * (size + gap) / 2.0f;
            float width = size;
            float height = size;
            if (isBitmap) {
                subBitmap = (Bitmap) mObjects[i];
                if (count == 2 || (count == 3 && i == 0)) {
                    subBitmap = Bitmap.createBitmap(subBitmap, (size + gap) / 4, 0, (size - gap) / 2, size);
                } else if ((count == 3 && (i == 1 || i == 2)) || count == 4) {
                    subBitmap =
                        Bitmap.createBitmap(subBitmap, (size + gap) / 4, (size + gap) / 4, (size - gap) / 2, (size - gap) / 2);
                }
                canvas.drawBitmap(subBitmap, startX, startY, paint);
            } else {
                if (count == 2 || (count == 3 && i == 0)) {
                    width = (size - gap) / 2f;
                    height = size;
                } else if ((count == 3 && (i == 1 || i == 2)) || count == 4) {
                    width = (size - gap) / 2f;
                    height = width;
                }
                // 根据昵称绘制上默认头像
                String nickName = (String) mObjects[i];
                drawNickHead(canvas, paint, subSize, startX, startY, width, height, childAvatarRoundPx, nickName, nickNameBgColor,
                    nickNameTextSize);
            }
        }
        return result;
    }

    private void drawNickHead(Canvas canvas, Paint paint, int subSize, float x, float y, float width, float height,
        int childAvatarRoundPx, String nickName, int nickNameBgColor, int nickNameTextSize) {
        paint.setColor(nickNameBgColor == 0 ? Color.GRAY : nickNameBgColor);
        canvas.drawRect(x, y, x + width, y + height, paint);
        // 设置圆内字体白色
        paint.setColor(Color.WHITE);
        // sans serif字体类型
        paint.setTypeface(Typeface.SANS_SERIF);
        // 设置字体的大小
        paint.setTextSize(nickNameTextSize);
        // x位于字符串中心
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
        float offHeight = fontTotalHeight / 2 - fontMetrics.bottom;
        // 计算出字符串中心的y坐标
        float newHeight = y + height / 2f + offHeight;
        // 画姓
        canvas.drawText(nickName, x + width / 2f, newHeight, paint);
    }
}
