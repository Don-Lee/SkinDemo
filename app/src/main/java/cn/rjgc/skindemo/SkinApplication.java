package cn.rjgc.skindemo;

import android.app.Application;

import com.qc.skin.SkinManager;

/**
 * Date 2021/9/30
 *
 * @author Don
 */
public class SkinApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化换肤 启动换肤的功能时SplashActivity中
        SkinManager.getInstance().init(this);
//        ExtraAttrRegister.init();// 处理自定义的换肤属性
    }
}
