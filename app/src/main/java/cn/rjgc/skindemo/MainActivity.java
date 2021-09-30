package cn.rjgc.skindemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;

import android.os.Bundle;

import com.qc.skin.SkinInflaterFactory;
import com.qc.skin.SkinManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //换肤步骤
        //Step1.Application中初始化换肤sdk
        //Step2.Activity中拦截控件的创建
        SkinInflaterFactory skinFactory = new SkinInflaterFactory(this);
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), skinFactory);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Step3.下载皮肤包  todo

        //Step4.加载皮肤包
        SkinManager.getInstance().loadSkin("皮肤包路径");
        //Step5.配置换肤控件
    }
}