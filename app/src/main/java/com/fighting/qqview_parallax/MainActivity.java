package com.fighting.qqview_parallax;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

/**
 * 描述：
 * 作者 mjd
 * 日期：2016/1/28 13:11
 */
public class MainActivity extends Activity {

    private ParallaxListView plv;
    private ImageView ivHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        setContentView(R.layout.activity_main);
        plv = (ParallaxListView) findViewById(R.id.plv);
        View headerView = View.inflate(this, R.layout.layout_header, null);
        ivHeader = (ImageView) headerView.findViewById(R.id.iv_header);
        plv.addHeaderView(headerView);
    }

    private void initData() {
        plv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Cheeses.NAMES));
    }

    private void initListener() {
        //等 view 的树状结构渲染完毕时，再将 headerImage 设置到 plv 中
        ivHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //宽高已经测量完毕
                plv.setParallaxImage(ivHeader);
                //移除监听，避免下次渲染时还调用
                ivHeader.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
}
