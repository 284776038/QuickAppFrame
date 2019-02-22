package cn.bfy.frame.util.bitmap;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import cn.bfy.frame.R;

/**
 * <pre>
 * @copyright  : Copyright ©2004-2018 版权所有　XXXXXXXXXXXXXXX
 * @company    : XXXXXXXXXXXXXXX
 * @author     : OuyangJinfu
 * @e-mail     : jinfu123.-@163.com
 * @createDate : 2017/5/11 0011
 * @modifyDate : 2017/5/11 0011
 * @version    : 1.0
 * @desc       : 本地图片选择器对话框, 可选从图库中选择图片, 也可以拍照
 * </pre>
 */
public class PicSelectorDialog extends Dialog implements DialogInterface.OnShowListener
		, DialogInterface.OnDismissListener{
	
	private static final String TAG = PicSelectorDialog.class.getSimpleName();
	
	public PicSelectorDialog(Context context,
			final View.OnClickListener takePhotoListener,
			final View.OnClickListener selectPicListener){
			super(context, R.style.CustomDialog);

		Window window = getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.setBackgroundDrawable(new ColorDrawable());
//		window.setWindowAnimations(R.anim.abc_popup_enter);

		setContentView(R.layout.photo_selector_pop_head);
		TextView takePhotoTv = (TextView)findViewById(R.id.bt_camera);
		TextView selectPicTv = (TextView)findViewById(R.id.bt_photo);
		TextView cancelTv = (TextView)findViewById(R.id.bt_cancel);
		
		
			takePhotoTv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(takePhotoListener != null){
						takePhotoListener.onClick(v);
					}
					dismiss();
				}
			});
			selectPicTv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(selectPicListener != null){
						selectPicListener.onClick(v);
					}
					dismiss();
				}
			});
		
		cancelTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		setCancelable(true);

		setOnShowListener(this);

		setOnDismissListener(this);

	}

	@Override
	public void onShow(DialogInterface dialog) {
//		ScreenUtils.backgroundAlpha(getOwnerActivity(), 0.4f);
		Window window = getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.BOTTOM;
		window.getDecorView().setPadding(0, 0 ,0 ,0);
		window.setAttributes(lp);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
//		ScreenUtils.backgroundAlpha(getOwnerActivity(), 1f);
	}
}
