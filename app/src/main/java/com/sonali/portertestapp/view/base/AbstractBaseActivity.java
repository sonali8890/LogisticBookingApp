package com.sonali.portertestapp.view.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.sonali.portertestapp.R;
import com.sonali.portertestapp.databinding.ActivityAbstractBaseBinding;
import com.sonali.portertestapp.view.CustomProgressDialog;

/**
 * Created by Sonali
 */

public class AbstractBaseActivity extends AppCompatActivity implements View.OnClickListener{

    protected CustomProgressDialog mDialog;
    private ActivityAbstractBaseBinding binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_abstract_base);
        mDialog = new CustomProgressDialog(this);

        setSupportActionBar(binder.toolbar);
    }

    protected void showTitle(@NonNull String title){
        binder.titleTextView.setVisibility(View.VISIBLE);
        binder.titleTextView.setText(title);
        binder.titleTextView.setOnClickListener(this);

    }

    protected void showCenterLogo(){
        binder.logo.setVisibility(View.VISIBLE);
    }

    protected ViewDataBinding addChildLayout(int layoutId){
        ViewDataBinding b = DataBindingUtil.bind(LayoutInflater.from(this).inflate(layoutId,binder.body, false));
        binder.body.addView(b.getRoot());
        return b;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.titleTextView){
            finish();
        }
    }
}
