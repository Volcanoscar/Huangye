package com.nuo.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fujie.common.SystemMethod;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.nuo.activity.R;
import com.nuo.bean.notebook.NoteBookRecord;
import com.nuo.utils.FileUtiles;
import com.nuo.utils.T;
import com.nuo.utils.XutilHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2014/11/5.
 */
public class RecordAdapter extends BaseAdapter{

    private final Context context;
    private LayoutInflater mInflater;
    private List<NoteBookRecord> recordList;
    private DbUtils dbUtils;




    public RecordAdapter(Context context, List<NoteBookRecord> stadiumList) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.recordList = stadiumList;
        dbUtils = XutilHelper.getDB(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return recordList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return recordList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_record, null);
           /* holder.score = (TextView) convertView.findViewById(R.id.score);*/
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.item_right = (RelativeLayout) convertView.findViewById(R.id.item_right);
            holder.item_left = (RelativeLayout) convertView.findViewById(R.id.item_left);
            holder.record_play_controller = (ImageView) convertView.findViewById(R.id.record_play_controller);
            holder.voice_display_voice_progressbar = (ProgressBar) convertView.findViewById(R.id.voice_display_voice_progressbar);
            holder.time_layout = (LinearLayout) convertView.findViewById(R.id.time_layout);
            holder.voice_display_voice_layout = (LinearLayout) convertView.findViewById(R.id.voice_display_voice_layout);
            holder.voice_display_voice_time = (TextView) convertView.findViewById(R.id.voice_display_voice_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final NoteBookRecord noteBookRecord = recordList.get(position);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(SystemMethod.dip2px(context,100), LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_right.setLayoutParams(lp2);
        holder.name.setText(noteBookRecord.getName());
        holder.time.setText(String.valueOf((int)noteBookRecord.getLength()));
        holder.voice_display_voice_time.setText(String.valueOf((int)noteBookRecord.getLength()));
        final ViewHolder finalHolder = holder;
        holder.record_play_controller.setOnClickListener(new View.OnClickListener() {
            boolean mPlayState=false; // 播放状态
            private int mPlayCurrentPosition=0;// 当前播放的时间
            private MediaPlayer mMediaPlayer;
            public void onClick(View v) {
                // 播放录音
                if (!mPlayState) {
                    //显示播放条，隐藏大小
                    if (mPlayCurrentPosition == 0) {
                        finalHolder.voice_display_voice_layout.setVisibility(View.VISIBLE);
                        finalHolder.time_layout.setVisibility(View.GONE);
                        if (mMediaPlayer == null) {
                        mMediaPlayer = new MediaPlayer();
                        // 添加录音的路径
                        try {
                            mMediaPlayer.setDataSource(noteBookRecord.getPath());
                            // 准备
                            mMediaPlayer.prepare();
                        } catch (IOException e) {
                            T.showShort(context, "不能播放的录音");
                            return;
                        }
                        }
                    }

                    try {
                        // 播放
                        mMediaPlayer.start();
                        // 根据时间修改界面
                        new Thread(new Runnable() {

                            public void run() {
                                finalHolder.voice_display_voice_progressbar
                                        .setMax((int) noteBookRecord.getLength());
                                while (mMediaPlayer.isPlaying()) {
                                    mPlayCurrentPosition = mMediaPlayer
                                            .getCurrentPosition() / 1000;
                                    finalHolder.voice_display_voice_progressbar
                                            .setProgress(mPlayCurrentPosition);
                                }
                            }
                        }).start();
                        // 修改播放状态
                        mPlayState = true;
                        // 修改播放图标
                        finalHolder.record_play_controller
                                .setImageResource(R.drawable.audio_pause);

                        mMediaPlayer
                                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    // 播放结束后调用
                                    public void onCompletion(MediaPlayer mp) {
                                        // 停止播放
                                        mMediaPlayer.stop();
                                        // 修改播放状态
                                        mPlayState = false;
                                        // 修改播放图标
                                        finalHolder.record_play_controller
                                                .setImageResource(R.drawable.audio_play);
                                        // 初始化播放数据
                                        mPlayCurrentPosition = 0;
                                        finalHolder.voice_display_voice_progressbar
                                                .setProgress(mPlayCurrentPosition);
                                        //隐藏播放条，显示大小
                                        finalHolder.voice_display_voice_layout.setVisibility(View.GONE);
                                        finalHolder.time_layout.setVisibility(View.VISIBLE);
                                    }
                                });

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (mMediaPlayer != null) {
                        // 根据播放状态修改显示内容
                        if (mMediaPlayer.isPlaying()) {
                            mPlayState = false;
                            mMediaPlayer.pause();
                            finalHolder.record_play_controller
                                    .setImageResource(R.drawable.audio_play);
                            finalHolder.voice_display_voice_progressbar
                                    .setProgress(mPlayCurrentPosition);
                        } else {
                            mPlayState = false;
                            finalHolder.record_play_controller
                                    .setImageResource(R.drawable.audio_play);
                            finalHolder.voice_display_voice_progressbar
                                    .setProgress(mPlayCurrentPosition);
                        }
                    }
                }
            }
        });
        holder.item_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File file = new File(noteBookRecord.getPath());
                if (file.exists()) {
                    file.delete();
                }
                if (noteBookRecord.getId() != null) {
                    try {
                        dbUtils.deleteById(NoteBookRecord.class, noteBookRecord.getId());
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                recordList.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public final class ViewHolder {
        public TextView score;
        public TextView name;
        public TextView time;
        public RelativeLayout item_right;
        public RelativeLayout item_left;
        public ImageView record_play_controller;
        public ProgressBar voice_display_voice_progressbar;
        public LinearLayout time_layout;
        public LinearLayout voice_display_voice_layout;
        public TextView voice_display_voice_time;
    }
}
