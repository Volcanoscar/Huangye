package com.nuo.actionbar;

import android.content.Context;
import android.content.Intent;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;
import com.nuo.activity.R;

public class PlusActionProvider extends ActionProvider {

	private Context context;

	public PlusActionProvider(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public View onCreateActionView() {
		return null;
	}

	@Override
        public void onPrepareSubMenu(SubMenu subMenu) {
		subMenu.clear();
		subMenu.add(context.getString(R.string.plus_add_friend))
				.setIcon(R.drawable.ofm_add_icon)
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
                      /*  Intent intent = new Intent(context, AddFriendActivity.class);
                        context.startActivity(intent);
                        return true;*/
                        return false;
					}
				});
		subMenu.add(context.getString(R.string.plus_video_chat))
				.setIcon(R.drawable.ofm_video_icon)
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						return false;
					}
				});
		subMenu.add(context.getString(R.string.plus_scan))
				.setIcon(R.drawable.ofm_qrcode_icon)
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
                      /*  Intent intent = new Intent(context, ErcodeScanActivity.class);
                        context.startActivity(intent);
                        return true;*/
                         return false;
					}
				});
		subMenu.add(context.getString(R.string.plus_take_photo))
				.setIcon(R.drawable.ofm_camera_icon)
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
                      /*  Intent intent = new Intent(context, WaterCameraActivity.class);
                        context.startActivity(intent);
                        return true;*/
                        return false;
					}
				});
        subMenu.add(context.getString(R.string.action_feed))
                .setIcon(R.drawable.ofm_feedback_icon)
                .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                      /*  Intent intent = new Intent(context, FeedbackActivity.class);
                        context.startActivity(intent);
                       return true;*/
                        return false;
                    }
                });
        subMenu.add(context.getString(R.string.action_share))
                .setIcon(R.drawable.ofm_collect_icon)
                .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                       /* Intent intent=new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                        intent.putExtra(Intent.EXTRA_TEXT, "http://baidu.com");
                        context.startActivity(Intent.createChooser(intent,"诺部落"));
                        return true;*/
                        return false;
                    }
                });
	}

	@Override
	public boolean hasSubMenu() {
		return true;
	}

}