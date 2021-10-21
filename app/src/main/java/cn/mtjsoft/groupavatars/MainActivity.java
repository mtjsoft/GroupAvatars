package cn.mtjsoft.groupavatars;

import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import cn.mtjsoft.groupavatarslib.GroupAvatarsLib;
import cn.mtjsoft.groupavatarslib.layout.DingLayoutManager;
import cn.mtjsoft.groupavatarslib.layout.WechatLayoutManager;

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

        showImageWx(findViewById(R.id.image2), 2);
        showImageWx(findViewById(R.id.image3), 3);
        showImageWx(findViewById(R.id.image4), 4);
        showImageWx(findViewById(R.id.image5), 5);
        showImageWx(findViewById(R.id.image6), 6);
        showImageWx(findViewById(R.id.image7), 7);
        showImageWx(findViewById(R.id.image8), 8);
        showImageWx(findViewById(R.id.image9), 9);


        showImageDing(findViewById(R.id.imageD2), 2);
        showImageDing(findViewById(R.id.imageD3), 3);
        showImageDing(findViewById(R.id.imageD4), 4);
    }

    /**
     * 微信群头像类型
     *
     * @param imageView 显示最终图片的ImageView
     * @param count     组合头像的个数
     */
    private void showImageWx(ImageView imageView, int count) {
        if (count > mList.size()) {
            count = mList.size();
        }
        String groupId = "wx_" + count;
        List<String> datas = mList.subList(0, count);

        GroupAvatarsLib.init(getBaseContext())
                // 必选，设置最终生成的图片尺寸，单位dp（一般就是当前imageView的大小）
                .setSize(80)
                // 设置钉钉或者微信群头像类型 DingLayoutManager、 WechatLayoutManager
                // 目前钉钉最多组合4个，微信最多9个。超出会自动截取前4或9个
                .setLayoutManager(new WechatLayoutManager())
                // 设置使用昵称生成头像时的背景颜色
                .setNickAvatarColor(R.color.color_1677FF)
                // 设置昵称生成头像时的文字大小 ,单位dp （设置为0时 = 单个小头像的1/4大小）
                .setNickTextSize(0)
                // 设置群组ID，用于生成缓存key
                .setGroupId(groupId)
                // 设置加载最终图片的圆角大小，单位dp，默认0
                .setRound(10)
                // 设置内部单个图片的圆角，单位dp，默认0
                .setChildAvatarRound(3)
                // 单个图片之间的距离，单位dp，默认0dp
                .setGap(1)
                // 设置生成的图片背景色
                .setGapColor(R.color.color_E4E4E4)
                // 单个网络图片加载失败时，会展示默认图片
                .setPlaceholder(R.mipmap.group_default)
                // 设置数据（可设置网络图片地址或者昵称）
                .setDatas(datas)
                // 设置要显示最终图片的ImageView
                .setImageView(imageView)
                .build();
    }


    /**
     * 微信群头像类型
     *
     * @param imageView 显示最终图片的ImageView
     * @param count     组合头像的个数
     */
    private void showImageDing(ImageView imageView, int count) {
        if (count > mList.size()) {
            count = mList.size();
        }
        String groupId = "ding_" + count;
        List<String> datas = mList.subList(0, count);

        GroupAvatarsLib.init(getBaseContext())
                // 必选，设置最终生成的图片尺寸，单位dp（一般就是当前imageView的大小）
                .setSize(80)
                // 设置钉钉或者微信群头像类型 DingLayoutManager、 WechatLayoutManager
                // 目前钉钉最多组合4个，微信最多9个。超出会自动截取前4或9个
                .setLayoutManager(new DingLayoutManager())
                // 设置使用昵称生成头像时的背景颜色(昵称或只截取后两位)
                .setNickAvatarColor(R.color.color_1677FF)
                // 设置昵称生成头像时的文字大小 ,单位dp ,默认12dp
                .setNickTextSize(12)
                // 设置群组ID，用于生成缓存key
                .setGroupId(groupId)
                // 设置加载最终图片的圆角大小，单位dp，默认0
                .setRound(10)
                // 设置内部单个图片的圆角，单位dp，默认0
                .setChildAvatarRound(3)
                // 单个图片之间的距离，单位dp，默认0dp
                .setGap(1)
                // 设置生成的图片背景色
                .setGapColor(R.color.color_E4E4E4)
                // 单个网络图片加载失败时，会展示默认图片
                .setPlaceholder(R.mipmap.group_default)
                // 设置数据（可设置网络图片地址或者昵称）
                .setDatas(datas)
                // 设置要显示最终图片的ImageView
                .setImageView(imageView)
                .build();
    }
}