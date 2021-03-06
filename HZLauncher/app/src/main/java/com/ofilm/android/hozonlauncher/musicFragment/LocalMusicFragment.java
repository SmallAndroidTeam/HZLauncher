package com.ofilm.android.hozonlauncher.musicFragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import com.ofilm.android.hozonlauncher.R;
import com.ofilm.android.hozonlauncher.adapter.LryicAdapter;
import com.ofilm.android.hozonlauncher.localInformation.App;
import com.ofilm.android.hozonlauncher.localInformation.MusicUtils;
import com.ofilm.android.hozonlauncher.toast.OneToast;
import com.ofilm.android.hozonlauncher.util.Format;
import com.ofilm.android.hozonlauncher.util.MusicIconLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import of.media.hz.bean.Music;
import of.media.hz.service.IMusicService;
import of.media.hz.service.MusicListChangeListener;
import of.media.hz.service.MusicPlayProgressListener;


/**
 * Created by MR.XIE on 2018/11/6.
 */
public class LocalMusicFragment extends Fragment implements View.OnClickListener {

    private TextView musicTitle;
    private ImageView musicAlbum;
    private ListView musicLyric;
    private ImageView prevImageView;
    private ImageView playImageView;
    private ImageView nextImageView;
    private TextView musicCurrentPosition;
    private TextView musicDuration;
    private SeekBar musicSeekbar;

    private IMusicService musicController;
    private final static String TAG="hz11111";
    private TextView musicArtist;
    private final static int INIT_UI=0;//初始化播放Ui界面
    private final static int UPDATE_PROGRESS=1;//更新进度条
    private final static int UPDATE_LRYIC=2;//更新歌词进度
    private int currentPlayIndex=-1;//当前的播放下标
    private boolean isPersonTouch=false;//判断是否为人为滑动进度条
    private boolean isResetBind=false;//是否重新绑定
    private  final List<String> noLrcs=new ArrayList<>();//无歌词
    private  ObjectAnimator roateAnimation;
    private LryicAdapter lryicAdapter;

    @SuppressLint("HandlerLeak")
    private Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case INIT_UI:
                    Music music= (Music) msg.obj;
                    if(music!=null){
                        musicTitle.setText(music.getTitle());//设置标题
                        musicArtist.setText(music.getArtist());//设置歌手名
                        if(music.getImage()!=null){//图片地址存在
                            if(music.getImage().toLowerCase().startsWith("http")){//如果是网络图片
                                Glide.with(Objects.requireNonNull(getContext())).load(music.getImage()).into(musicAlbum);
                            }else if(new File(music.getImage()).exists()){//如果是本地图片，且对应地址的文件存在
                                Bitmap bitmap = MusicIconLoader.getInstance().load(music.getImage());
                                if(bitmap!=null){
                                    musicAlbum .setImageBitmap(bitmap);
                                }else{
                                    musicAlbum.setImageResource(R.drawable.mp1);
                                }

                            }else{//对应地址的文件不存在
                                   musicAlbum.setImageResource(R.drawable.mp1);
                            }
                        }else{
                            musicAlbum.setImageResource(R.drawable.mp1);
                        }
                    }
                    try {
                        if(musicController!=null){
                            musicCurrentPosition.setText(Format.changeToTime(musicController.of_getCurrentPosition()));//设置开始的进度
                            musicDuration.setText(Format.changeToTime(musicController.of_getDuration()));//设置总的播放时间
                            musicSeekbar.setMax(musicController.of_getDuration());//设置进度条的最大值
                            currentPlayIndex=musicController.of_getCurrentPlayIndex();//获取当前的播放下标
                            if(musicController.of_getCurrentPlayMusicAllLyric().size()!=0){
                                lryicAdapter.setmLrcs(musicController.of_getCurrentPlayMusicAllLyric());
                                lryicAdapter.setIndex(musicController.of_getCurrentPlayMusicOneLyricIndex());
                                lryicAdapter.notifyDataSetChanged();
                            }
//                            Log.i(TAG, "歌词行数： "+musicController.of_getCurrentPlayMusicAllLyric().size()+"  ，正在唱的歌词//  "+
//                                    musicController.of_getCurrentPlayMusicOneLyric()+"   //正在唱的歌词的下标："+musicController.of_getCurrentPlayMusicOneLyricIndex());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case UPDATE_PROGRESS://更新进度条
                    if(!isPersonTouch){//如果不是人为拖动进度条
                        musicSeekbar.setProgress((Integer) msg.obj);//设置进度条进度
                    }
                    musicCurrentPosition.setText(Format.changeToTime((Integer) msg.obj));//设置播放的时间
                        try {
                            if(musicController!=null) {
                                if (musicController.of_getCurrentPlayIndex() != currentPlayIndex){//如果播放下标改变，更新播放界面的信息
                                    Music music1=musicController.of_getPlayMusicInfo();//得到当前下标的音乐信息
                                    if(music1!=null){
                                        mhandler.obtainMessage(INIT_UI,music1).sendToTarget();
                                    }
                                }
                                if(musicSeekbar.getMax()==0){//如果进度条的最大值为0，则重新设置最大值
                                    musicSeekbar.setMax(musicController.of_getDuration());//设置进度条的最大值
                                    musicDuration.setText(Format.changeToTime(musicController.of_getDuration()));//设置总的播放时间
                                }

                                if(musicController.of_musicIsPlaying()){
//                                    if(roateAnimation==null){
//                                        roateAnimation=ObjectAnimator.ofFloat(musicAlbum,"rotation",0f,360f);
//                                        roateAnimation.setInterpolator(new LinearInterpolator());
//                                        roateAnimation.setRepeatMode(ValueAnimator.RESTART);
//                                        roateAnimation.setRepeatCount(-1);//不停旋转
//                                        roateAnimation.setDuration(36000);
//                                            roateAnimation.start();
//                                    }else {
//                                            roateAnimation.resume();//继续旋转
//                                    }

                                    playImageView.setImageResource(R.drawable.play_imageview);
                                }else{
//
//                                    if(roateAnimation!=null){
//                                        roateAnimation.pause();//暂停图片的旋转动画
//                                    }

                                    //fsdfsd
                                    playImageView.setImageResource(R.drawable.pause_imageview);
                                }

                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                    break;
                case UPDATE_LRYIC://更新歌词
                    lryicAdapter.notifyDataSetChanged();
                    if(msg.obj!=null){
                        int line= (int) msg.obj;
                        musicLyric.smoothScrollToPositionFromTop(line,120,500);//滑动到position  距离top的偏移量  滑动所用的时间
                    }
                    break;
                    default:
                        break;
            }
        }
    };



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.local_music_fragment_item, container,false);
        noLrcs.add("     ");
        noLrcs.add("     ");
        noLrcs.add("暂无歌词");
        noLrcs.add("     ");
        noLrcs.add("     ");
        initView(view);
        initEvents();
        bindService();
        initData();
        return view;
    }

    //adb push G:\Trinity_project\视频和音乐资源\music /stoage/emulated/0/music/
    //初始化数据
    private void initData() {


  new Timer().schedule(new TimerTask() {//通过一个定时器，当连接服务后更新播放界面信息
      @Override
      public void run() {
          if(musicController!=null){
              try {
              musicController.of_setCurrentMusicList(musicController.of_getLocalMusicList());//设置当前的播放列为本地列表
                  Log.i("fsafdasdf", "run: "+musicController.of_getLocalMusicList().size());
                  musicController.of_setCurrentPlayIndex(0);//设置当前的音乐播放下标
                  musicController.of_setPlayMode(0);//设置当前的播放模式为顺序播放
                  musicController.of_initMusicService();//初始化服务
                  Music music=musicController.of_getPlayMusicInfo();//得到当前下标的音乐信息
                  if(music!=null){
                     mhandler.obtainMessage(INIT_UI,music).sendToTarget();
                  }
              } catch (Exception e) {
                  e.printStackTrace();
              }
              this.cancel();
          }
      }
  },100,100);

  new Timer().schedule(new TimerTask() {//更新歌词进度
      @Override
      public void run() {
        try {
            if(musicController!=null) {
                if (musicController.of_getCurrentPlayMusicAllLyric().size() != 0) {
                    if(!lryicAdapter.getmLrcs().containsAll(musicController.of_getCurrentPlayMusicAllLyric()))
                    {
                        lryicAdapter.setmLrcs(musicController.of_getCurrentPlayMusicAllLyric());
                        mhandler.sendEmptyMessage(UPDATE_LRYIC);
                    }
                    if (lryicAdapter.getIndex() != musicController.of_getCurrentPlayMusicOneLyricIndex()) {
                        lryicAdapter.setIndex(musicController.of_getCurrentPlayMusicOneLyricIndex());
                        mhandler.obtainMessage(UPDATE_LRYIC,musicController.of_getCurrentPlayMusicOneLyricIndex()).sendToTarget();
                    }
                }else{//当前无歌词
                    lryicAdapter.setIndex(-1);
                    lryicAdapter.setmLrcs(noLrcs);
                    mhandler.sendEmptyMessage(UPDATE_LRYIC);
                }

//                Log.i(TAG, "歌词行数： " + musicController.of_getCurrentPlayMusicAllLyric().size() + "  ，正在唱的歌词//  " +
//                        musicController.of_getCurrentPlayMusicOneLyric() + "   //正在唱的歌词的下标：" + musicController.of_getCurrentPlayMusicOneLyricIndex());
            }else{
                lryicAdapter.setmLrcs(noLrcs);
                lryicAdapter.setIndex(-1);
                mhandler.sendEmptyMessage(UPDATE_LRYIC);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

      }
  },0,100);

    }




    //播放音乐
    private void staticMusic(){
        if(musicController!=null){
            try {
                musicController.of_startCurrentPlayIndexMusic();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

   //暂停音乐
    private void  pauseMusic(){
        if(musicController!=null){
            try {
                musicController.of_pauseCurrentPlayIndexMusic();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //设置播放模式 type={0 顺序模式 ,1 列表模式 ,2 单曲模式 ,3 随机模式}

    private void setPlayMode(int type){
        if(musicController!=null){
            try {
                musicController.of_setPlayMode(type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //前一首
    private void prevMusic(){
        if(musicController!=null){
            try {
                musicController.of_prevMusic();
                playImageView.setImageResource(R.drawable.play_imageview);
                Music music=musicController.of_getPlayMusicInfo();//得到当前下标的音乐信息
                if(music!=null){
                    mhandler.obtainMessage(INIT_UI,music).sendToTarget();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //下一首
    private void nextMusic(){
        if(musicController!=null){
            try {
                musicController.of_nextMusic();
                playImageView.setImageResource(R.drawable.play_imageview);
                Music music=musicController.of_getPlayMusicInfo();//得到当前下标的音乐信息
                if(music!=null){
                    mhandler.obtainMessage(INIT_UI,music).sendToTarget();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //绑定服务
    private void bindService() {
        Intent intent=new Intent();
        intent.setPackage("of.media.hz");
        intent.setAction("com.android.oflim.action");
        Objects.requireNonNull(getContext()).bindService(intent,serviceConnection, Service.BIND_AUTO_CREATE);
    }

    private MusicPlayProgressListener musicPlayProgressListener=new MusicPlayProgressListener.Stub() {//进度条监听
        @Override
        public void musicProgressListener(int progress) throws RemoteException {
          mhandler.obtainMessage(UPDATE_PROGRESS,progress).sendToTarget();//更新进度条
        }
    };

    private MusicListChangeListener musicListChangeListener=new MusicListChangeListener.Stub() {//音乐列表改变监听
        @Override
        public void musicListChangeListener(boolean result) throws RemoteException {

        }
    };
    private ServiceConnection serviceConnection= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i("ha11111", "服务连接成功 ");
            musicController= IMusicService.Stub.asInterface(iBinder);

            try {

                //设置死亡代理,目的是防止断开连接
                musicController.asBinder().linkToDeath(deathRecipient,0);
                musicController.of_setMusicPlayProgressListener(musicPlayProgressListener);//添加播放进度监听
                musicController.of_setMusicListChangeListener(musicListChangeListener);//添加音乐列表变化监听

                //如果重新绑定服务
                //(服务主动关闭的过程：打开music,再打开HZMultiMedia应用，点击播放按钮，然后关闭前台服务，再关闭music应用，那么服务就会关闭了)
                if(isResetBind){
                    Log.i("ha1111", "onServiceConnected: "+musicController.of_getCurrentMusicListSize());
                    if(musicController.of_getCurrentMusicListSize()==0){//当前的播放列表为空（是由于服务主动关闭导致的，此时设置当前的播放列表为本地列表，且播放下标为1)
                        initData();
                    }
                    isResetBind=false;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i("ha1111", "onServiceDisconnected: ");
            try {
                if(musicController!=null)
                {  musicController.of_cancelMusicPlayProgressListener(musicPlayProgressListener);//取消播放进度监听
                    musicController.of_cancelMusicListChangeListener(musicListChangeListener);//取消音乐列表变化的监听
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            musicController=null;
        }
    };

    /**
     * 监听Biner是否死亡，如果是则重新绑定
     */
    private  IBinder.DeathRecipient deathRecipient=new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.i("ha1111", "binderDied: ");
            if(musicController==null){
                return;
            }
            isResetBind=true;//重新绑定字段
            musicController.asBinder().unlinkToDeath(deathRecipient,0);
            musicController=null;
            //重新绑定
            Log.i("ha1111", "binderDied: 重新绑定");
            bindService();

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(musicController!=null&&musicPlayProgressListener.asBinder().isBinderAlive()) {
            try {
                musicController.of_cancelMusicListChangeListener(musicListChangeListener);//取消音乐列表变化的监听
                musicController.of_cancelMusicPlayProgressListener(musicPlayProgressListener);//取消播放进度监听
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        Objects.requireNonNull(getContext()).unbindService(serviceConnection);
    }

    private void initView(View view) {
        musicTitle = view.findViewById(R.id.musicTitle);
        musicArtist = view.findViewById(R.id.musicArtist);
        musicAlbum = view.findViewById(R.id.musicAlbum);
        musicLyric = view.findViewById(R.id.musicLyric);
        prevImageView = view.findViewById(R.id.prevImageView);
        playImageView = view.findViewById(R.id.playImageView);
        nextImageView = view.findViewById(R.id.nextImageView);
        musicCurrentPosition = view.findViewById(R.id.musicCurrentPosition);
        musicDuration = view.findViewById(R.id.musicDuration);
        musicSeekbar = view.findViewById(R.id.musicSeekbar);
        lryicAdapter = new LryicAdapter();
        lryicAdapter.setmLrcs(noLrcs);
        musicLyric.setAdapter(lryicAdapter);
    }

    private void initEvents() {
        musicAlbum.setOnClickListener(this);
        playImageView.setOnClickListener(this);
        prevImageView.setOnClickListener(this);
        nextImageView.setOnClickListener(this);
        musicSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {//设置进度条拖动监听
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
              if(musicController==null){
                  musicSeekbar.setProgress(0);
              }else {
                  try {
                      if(musicController.of_getCurrentMusicListSize()==0){
                          musicSeekbar.setProgress(0);
                      }
                  } catch (RemoteException e) {
                      e.printStackTrace();
                  }
              }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isPersonTouch=true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
              if(musicController!=null){
                  try {
                      musicController.of_setCurrent(musicSeekbar.getProgress());
                  } catch (RemoteException e) {
                      e.printStackTrace();
                  }
              }
                isPersonTouch=false;
            }
        });
        musicLyric.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                musicLyric.setVisibility(View.GONE);
                musicAlbum.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.musicAlbum:
                    musicLyric.setVisibility(View.VISIBLE);
                    musicAlbum.setVisibility(View.GONE);
                break;
            case R.id.playImageView:
                if(musicController!=null){
                    try {
                        if(musicController.of_musicIsPlaying()){
                            pauseMusic();
                            playImageView.setImageResource(R.drawable.pause_imageview);
                        }else{
                            if(musicController.of_getCurrentMusicListSize()==0){//判断当前音乐列表是否为空
                            OneToast.showMessage(getContext(),"当前无歌曲");
                            }else{
                                staticMusic();
                                playImageView.setImageResource(R.drawable.play_imageview);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.prevImageView:
                if(musicController!=null){
                    try {
                        if(musicController.of_getCurrentMusicListSize()==0){//判断当前音乐列表是否为空
                            Log.i(TAG, "onClick: 22");
                         OneToast.showMessage(getContext(),"当前无歌曲");
                        }else{
                            prevMusic();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.nextImageView:
                if(musicController!=null){
                    try {
                        if(musicController.of_getCurrentMusicListSize()==0){//判断当前音乐列表是否为空
                            Log.i(TAG, "onClick: 11");
                           OneToast.showMessage(getContext(),"当前无歌曲");
                        }else{
                            nextMusic();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
                default:
                    break;
        }
    }
}
