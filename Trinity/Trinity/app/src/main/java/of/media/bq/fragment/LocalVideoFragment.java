package of.media.bq.fragment;
import of.media.bq.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


import of.media.bq.adapter.LocalVideoAdapter;
import of.media.bq.bean.Video;
import of.media.bq.localInformation.FileManger;
import of.media.bq.saveData.SaveData;
import of.media.bq.toast.OneToast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MR.XIE on 2018/10/23.
 */
public class LocalVideoFragment extends Fragment {
    
    private    GridView localVideoGridView;
    private LocalVideoAdapter localVideoAdapter;
    private  boolean isFirstLoad=true;//判断是否第一次加载
    private SwipeRefreshLayout swipeRefreshLayoutVideo;
    private final static int UPDATE_VIDEO=1;
    private final static int SET_VIDEO=2;
    @SuppressLint("HandlerLeak")
    private Handler mhander=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SET_VIDEO:
                    localVideoGridView.setAdapter(localVideoAdapter);

                    break;
                case UPDATE_VIDEO:
                    localVideoAdapter.notifyDataSetChanged();
                    swipeRefreshLayoutVideo.setRefreshing(false);
                    OneToast.showMessage(getContext(),"刷新成功");
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.local_video_fragment,container,false);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取测试的视频地址
                List<Video> videoList = new ArrayList<>(FileManger.getInstance(getContext()).getVideos());
                if(videoList.size()==0){
                    OneToast.showMessage(getContext(),"当前无本地视频");
                }
                SaveData.setLocalVideoList(videoList);
                localVideoAdapter = new LocalVideoAdapter(videoList);
                localVideoGridView.setNumColumns(4);
                localVideoGridView.setVerticalSpacing(20);
                localVideoGridView.setHorizontalSpacing(20);
                mhander.sendEmptyMessageDelayed(SET_VIDEO,300);
            }
        }).start();


    }



    private List<String> getVideoAddress(){
        //adb push  G:\Trinity_project\Video\. /storage/emulated/0/Movies
        List<String> videoAddress=new ArrayList<>();
        String sdAddress=null;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//sd卡是否存在
            sdAddress=Environment.getExternalStorageDirectory().getAbsolutePath();//sd卡存储路径
        }else {
            sdAddress=Environment.getExternalStorageDirectory().getAbsolutePath();//本地存储路径
        }
        sdAddress+="/Movies/";
        
        File file=new File(sdAddress);
        if(!file.exists()||file.isFile()){
            file.mkdir();
            return null;
        }else{
            File[] files=file.listFiles();
            for(File file1:files){
                if(file1.isFile()){
                    if (file1.getAbsolutePath().endsWith(".mp4")){
                        videoAddress.add(file1.getAbsolutePath());
                    }
                }
                
            }
        }
        return videoAddress;
    }
    private void initView(View view) {
        localVideoGridView = view.findViewById(R.id.localVideoGridView);
        swipeRefreshLayoutVideo = view.findViewById(R.id.swipe_refresh_layout_video);
    }
    
    
    private void initListener() {
//        localVideoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                OneToast.showMessage(getContext(),i+"");
//                if(localVideoAdapter.getPosition()==i){//播放视频
//                    PlayVideoActivity.setVideoList(localVideoAdapter.getVideoList());
//                    Intent intent=new Intent(getContext(),PlayVideoActivity.class);
//                    intent.putExtra("position",i);
//                    getContext().startActivity(intent);
//                }else{
//                    selectVideosByIndex(i);
//                }
//
//            }
//        });
        swipeRefreshLayoutVideo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //获取测试的视频地址
                        List<Video> videoList = new ArrayList<>(FileManger.getInstance(getContext()).getVideos());
                        if(videoList.size()==0){
                            OneToast.showMessage(getContext(),"当前无本地视频");
                        }
                        SaveData.setLocalVideoList(videoList);
                        localVideoAdapter.setVideoList(videoList);
                        mhander.sendEmptyMessageDelayed(UPDATE_VIDEO,500);

                    }
                }).start();
            }
        });
    
    }
    
    //此函数用于解决视频列表突然失踪的bug
    public void reshow(){
        if(localVideoAdapter!=null&&localVideoAdapter.getCount()==0){
            localVideoAdapter.setVideoList(SaveData.getLocalVideoList());
            localVideoAdapter.notifyDataSetChanged();
        }
    }
   
    
    @Override
    public void onResume() {
        super.onResume();
        
        if(!isFirstLoad){
            // localVideoGridView.setAdapter(localVideoAdapter);
            // localVideoAdapter.notifyDataSetChanged();
            Log.i("trinity18", "onResume: "+localVideoAdapter.getCount()+"//"+SaveData.getLocalVideoList().size());
            if(localVideoAdapter.getCount()==0){
                localVideoAdapter.setVideoList(SaveData.getLocalVideoList());
                localVideoAdapter.notifyDataSetChanged();
            }
        }
        isFirstLoad=false;
    }
    
    
    //选中下标为index的视频时
    public void selectVideosByIndex(int index){
        localVideoAdapter.setPosition(index);
        TransitionSet transitionSet=new TransitionSet();
        Fade fade=new Fade();
        Slide slide=new Slide();
        transitionSet.addTransition(slide).addTransition(fade);
        TransitionManager.beginDelayedTransition(localVideoGridView,transitionSet);
        localVideoAdapter.notifyDataSetChanged();
        
    }
}
