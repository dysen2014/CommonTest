package com.dysen.common_library.utils;

import com.dysen.common_library.ui.UIHelper;


/**
 * @package com.dysen.common_library.utils
 * @email dy.sen@qq.com
 * created by dysen on 2018/9/7 - 下午12:52
 * @info
 */
public class ShareUtils {

    /**
     * 分享图片
     * @param picPath
     * @param shareType
     */
//    private static void showShare(final String picPath, final String shareType) {
//
//        if (Wechat.NAME.equalsIgnoreCase(shareType)){
//            Wechat.ShareParams sp = new Wechat.ShareParams();
//            sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
//            sp.setTitle(null);
//            sp.setText(null);
//            sp.setImageUrl(null);
//            sp.setTitleUrl(null);  //网友点进链接后，可以看到分享的详情
//            sp.setImagePath(picPath);
//            Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
//            wechat.setPlatformActionListener(platformActionListener); // 设置分享事件回调
//            // 执行分享
//            wechat.share(sp);
//        }else if (WechatMoments.NAME.equalsIgnoreCase(shareType)) {
//            WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
//            sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
//            sp.setTitle(null);
//            sp.setText(null);
//            sp.setImageUrl(null);
//            sp.setTitleUrl(null);  //网友点进链接后，可以看到分享的详情
//            sp.setImagePath(picPath);
//            //3、非常重要：获取平台对象
//            Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
//            wechatMoments.setPlatformActionListener(platformActionListener); // 设置分享事件回调
//            // 执行分享
//            wechatMoments.share(sp);
//
//        }else if (SinaWeibo.NAME.equalsIgnoreCase(shareType)) {
//            SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
//            sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
//            sp.setTitle(null);
//            sp.setText(null);
//            sp.setImageUrl(null);
//            sp.setTitleUrl(null);  //网友点进链接后，可以看到分享的详情
//            sp.setImagePath(picPath);
//            Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
//            sinaWeibo.setPlatformActionListener(platformActionListener); // 设置分享事件回调
//            sinaWeibo.SSOSetting(false);//false是使用客户端授权
//            // 执行分享
//            sinaWeibo.share(sp);
//
//        }else if (QQ.NAME.equalsIgnoreCase(shareType)) {
//            QQ.ShareParams sp = new QQ.ShareParams();
//            sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
//            sp.setTitle(null);
//            sp.setText(null);
//            sp.setImageUrl(null);
//            sp.setTitleUrl(null);  //网友点进链接后，可以看到分享的详情
//            sp.setImagePath(picPath);
//            //3、非常重要：获取平台对象
//            Platform qq = ShareSDK.getPlatform(QQ.NAME);
//            qq.setPlatformActionListener(platformActionListener); // 设置分享事件回调
//            // 执行分享
//            qq.share(sp);
//        } else if (QZone.NAME.equalsIgnoreCase(shareType)) {
//            QZone.ShareParams sp = new QZone.ShareParams();
//            sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
//            sp.setTitle(null);
//            sp.setText(null);
//            sp.setImageUrl(null);
//            sp.setTitleUrl(null);  //网友点进链接后，可以看到分享的详情
//            sp.setImagePath(picPath);
//            Platform qzone = ShareSDK.getPlatform(QZone.NAME);
//            qzone.setPlatformActionListener(platformActionListener); // 设置分享事件回调
//            // 执行图文分享
//            qzone.share(sp);
//        } else if (WechatFavorite.NAME.equalsIgnoreCase(shareType))  {
//            WechatFavorite.ShareParams sp = new WechatFavorite.ShareParams();
//            sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
//            sp.setTitle(null);
//            sp.setText(null);
//            sp.setImageUrl(null);
//            sp.setTitleUrl(null);  //网友点进链接后，可以看到分享的详情
//            sp.setImagePath(picPath);
//            Platform wechatFavorite = ShareSDK.getPlatform(WechatFavorite.NAME);
//            wechatFavorite.setPlatformActionListener(platformActionListener);
//            wechatFavorite.share(sp);
//        } else {
//            UIHelper.ToastMessage(mActivity, "Other App Share!");
//        }
//    }
}
