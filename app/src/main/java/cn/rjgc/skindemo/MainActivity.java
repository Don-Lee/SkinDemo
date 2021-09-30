package cn.rjgc.skindemo;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.LayoutInflaterCompat;

import com.qc.skin.SkinInflaterFactory;
import com.qc.skin.SkinManager;

import java.util.ArrayList;
import java.util.List;

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

        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        ActivityCompat.requestPermissions(this, permissions.toArray(new String[1]),
                0);
        //Step3.下载皮肤包


        //Step5.配置换肤控件

    }

    public void skin(View view) {
        // 下载目录;  根目录下的erpskin.apk是此demo的皮肤库,将apk放置到/storage/emulated/0/Android/data/cn.rjgc.skindemo/files/don下
        String downloadPath =
                getExternalFilesDir("don").getAbsolutePath() + "/erpskin.apk";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/erpskin.apk";
        }
        Log.e("TAG", "skin: " + downloadPath);
        //Step4.加载皮肤包
        SkinManager.getInstance().loadSkin(downloadPath);
    }
}