package cn.mtjsoft.groupavatars;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import cn.mtjsoft.groupavatarslib.GroupAvatarsLib;
import cn.mtjsoft.groupavatarslib.layout.WechatLayoutManager;
import cn.mtjsoft.groupavatarslib.utils.DisplayUtils;

import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String[] IMG_URL = {
        "http://img.hb.aicdn.com/eca438704a81dd1fa83347cb8ec1a49ec16d2802c846-laesx2_fw658",
        "http://img.hb.aicdn.com/729970b85e6f56b0d029dcc30be04b484e6cf82d18df2-XwtPUZ_fw658", "123小明",
        "http://img.hb.aicdn.com/2814e43d98ed41e8b3393b0ff8f08f98398d1f6e28a9b-xfGDIC_fw658",
        "http://img.hb.aicdn.com/a1f189d4a420ef1927317ebfacc2ae055ff9f212148fb-iEyFWS_fw658",
        "http://img.hb.aicdn.com/69b52afdca0ae780ee44c6f14a371eee68ece4ec8a8ce-4vaO0k_fw658",
        "http://img.hb.aicdn.com/9925b5f679964d769c91ad407e46a4ae9d47be8155e9a-seH7yY_fw658", "123大华",
        "http://img.hb.aicdn.com/73f2fbeb01cd3fcb2b4dccbbb7973aa1a82c420b21079-5yj6fx_fw658",
        };

    private List<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Collections.addAll(mList, IMG_URL);

        showImage(findViewById(R.id.image2), 2);
        showImage(findViewById(R.id.image3), 3);
        showImage(findViewById(R.id.image4), 4);
        showImage(findViewById(R.id.image5), 5);
        showImage(findViewById(R.id.image6), 6);
        showImage(findViewById(R.id.image7), 7);
        showImage(findViewById(R.id.image8), 8);
        showImage(findViewById(R.id.image9), 9);
    }

    private void showImage(ImageView imageView, int count) {
        if (count > mList.size()) {
            count = mList.size();
        }
        GroupAvatarsLib.init(getBaseContext())
            .setLayoutManager(new WechatLayoutManager())
            .setNickAvatarColor(R.color.color_1677FF)
            .setGroupId("1")
            .setRoundPx(DisplayUtils.dip2px(getBaseContext(), 10))
            // 必选，组合后Bitmap的尺寸，单位dp
            .setSize(80)
            // 单个图片之间的距离，单位dp，默认0dp
            .setGap(1)
            // 图片加载失败的默认显示图片
            .setPlaceholder(R.mipmap.group_default)
            .setGapColor(R.color.color_E4E4E4)
            .setDatas(mList.subList(0, count))
            // 直接设置要显示图片的ImageView
            .setImageView(imageView)
            .build();
    }
}