package com.sonali.portertestapp.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.sonali.portertestapp.R;


public class CustomProgressDialog extends Dialog {

	public CustomProgressDialog(Context context) {
		super(context, R.style.TransparentProgressDialog);
		
		setTitle(null);
		setCancelable(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.custom_progressbar);
		setCanceledOnTouchOutside(false);
	}

}
