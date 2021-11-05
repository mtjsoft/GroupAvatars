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
 * 微信群头像布局，最多显示9个
 */
public class WechatLayoutManager implements ILayoutManager {
    @Override
    public Bitmap combineBitmap(int size, int subSize, int gap, int gapColor, Object[] mObjects, int childAvatarRoundPx,
        int nickNameBgColor, int nickNameTextSize) {
        Bitmap result = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);
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
        for (int i = 0; i < count; i++) {
            if (mObjects[i] == null) {
                continue;
            }
            paint.setColor(gapColor);
            boolean isBitmap = mObjects[i] instanceof Bitmap;
            if (isBitmap) {
                subBitmap = (Bitmap) mObjects[i];
            }
            float x = 0;
            float y = 0;

            if (count == 2) {
                x = gap + i * (subSize + gap);
                y = (size - subSize) / 2.0f;
            } else if (count == 3) {
                if (i == 0) {
                    x = (size - subSize) / 2.0f;
                    y = gap;
                } else {
                    x = gap + (i - 1) * (subSize + gap);
                    y = subSize + 2 * gap;
                }
            } else if (count == 4) {
                x = gap + (i % 2) * (subSize + gap);
                if (i < 2) {
                    y = gap;
                } else {
                    y = subSize + 2 * gap;
                }
            } else if (count == 5) {
                if (i == 0) {
                    x = y = (size - 2 * subSize - gap) / 2.0f;
                } else if (i == 1) {
                    x = (size + gap) / 2.0f;
                    y = (size - 2 * subSize - gap) / 2.0f;
                } else if (i > 1) {
                    x = gap + (i - 2) * (subSize + gap);
                    y = (size + gap) / 2.0f;
                }
            } else if (count == 6) {
                x = gap + (i % 3) * (subSize + gap);
                if (i < 3) {
                    y = (size - 2 * subSize - gap) / 2.0f;
                } else {
                    y = (size + gap) / 2.0f;
                }
            } else if (count == 7) {
                if (i == 0) {
                    x = (size - subSize) / 2.0f;
                    y = gap;
                } else if (i < 4) {
                    x = gap + (i - 1) * (subSize + gap);
                    y = subSize + 2 * gap;
                } else {
                    x = gap + (i - 4) * (subSize + gap);
                    y = gap + 2 * (subSize + gap);
                }
            } else if (count == 8) {
                if (i == 0) {
                    x = (size - 2 * subSize - gap) / 2.0f;
                    y = gap;
                } else if (i == 1) {
                    x = (size + gap) / 2.0f;
                    y = gap;
                } else if (i < 5) {
                    x = gap + (i - 2) * (subSize + gap);
                    y = subSize + 2 * gap;
                } else {
                    x = gap + (i - 5) * (subSize + gap);
                    y = gap + 2 * (subSize + gap);
                }
            } else if (count == 9) {
                x = gap + (i % 3) * (subSize + gap);
                if (i < 3) {
                    y = gap;
                } else if (i < 6) {
                    y = subSize + 2 * gap;
                } else {
                    y = gap + 2 * (subSize + gap);
                }
            }
            if (isBitmap && subBitmap != null) {
                canvas.drawBitmap(subBitmap, x, y, paint);
            } else {
                // 根据昵称绘制上默认头像
                String nickName = (String) mObjects[i];
                drawNickHead(canvas, paint, subSize, x, y, childAvatarRoundPx, nickName, nickNameBgColor, nickNameTextSize);
            }
        }
        return result;
    }

    private void drawNickHead(Canvas canvas, Paint paint, int subSize, float x, float y, int childAvatarRoundPx, String nickName,
        int nickNameBgColor, int nickNameTextSize) {
        paint.setColor(nickNameBgColor == 0 ? Color.GRAY : nickNameBgColor);
        if (childAvatarRoundPx > 0) {
            canvas.drawRoundRect(x, y, x + subSize, y + subSize, childAvatarRoundPx, childAvatarRoundPx, paint);
        } else {
            canvas.drawRect(x, y, x + subSize, y + subSize, paint);
        }
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
        float newHeight = y + subSize / 2f + offHeight;
        // 画姓
        canvas.drawText(nickName, x + subSize / 2f, newHeight, paint);
    }
}
