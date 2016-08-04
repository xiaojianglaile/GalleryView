package com.jym.galleryview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jym.galleryview.views.GalleryView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GalleryView gvImages1, gvImages2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gvImages1 = (GalleryView) findViewById(R.id.gvImages1);
        gvImages2 = (GalleryView) findViewById(R.id.gvImages2);

        findViewById(R.id.ivPre1).setOnClickListener(this);
        findViewById(R.id.ivNext1).setOnClickListener(this);
        findViewById(R.id.ivPre2).setOnClickListener(this);
        findViewById(R.id.ivNext2).setOnClickListener(this);

        gvImages1.postDelayed(new Runnable() {
            @Override
            public void run() {
                gvImages1.resetAllChildParams();
                gvImages2.resetAllChildParams();
            }
        }, 300);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivPre1:
                gvImages1.showPrevious();
                break;
            case R.id.ivNext1:
                gvImages1.showNext();
                break;
            case R.id.ivPre2:
                gvImages2.showPrevious();
                break;
            case R.id.ivNext2:
                gvImages2.showNext();
                break;
        }
    }
}
