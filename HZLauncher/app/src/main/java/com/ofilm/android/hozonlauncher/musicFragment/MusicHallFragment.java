package com.ofilm.android.hozonlauncher.musicFragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ofilm.android.hozonlauncher.R;
import com.ofilm.android.hozonlauncher.toast.OneToast;

import java.util.Objects;

/**
 * Created by MR.XIE on 2018/11/13.
 */
public class MusicHallFragment extends Fragment implements View.OnClickListener {

    private TextView musicButton;
    private TextView fmButton;
    private TextView kaolaButton;
    private TextView usbButton;
    private TextView bluetoothButton;
    private Fragment localMusicFragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.music_hall_fragment_item,container,false);
       initView(view);
       initEvents();
       setTab(0);//默认选中第一个Tab
       return view;
    }

    private void setTab(int i) {
        switch (i){
            case 0:
                musicButton.callOnClick();
                break;
            case 1:
                fmButton.callOnClick();
                break;
            case 2:
                kaolaButton.callOnClick();
                break;
            case 3:
                usbButton.callOnClick();
                break;
            case 4:
                bluetoothButton.callOnClick();
                break;
                default:
                    break;
        }
    }

    private void initView(View view) {
        musicButton = view.findViewById(R.id.musicButton);
        fmButton = view.findViewById(R.id.fmButton);
        kaolaButton = view.findViewById(R.id.kaolaButton);
        usbButton = view.findViewById(R.id.usbButton);
        bluetoothButton = view.findViewById(R.id.bluetoothButton);
    }

    private void initEvents() {
        musicButton.setOnClickListener(this);
        fmButton.setOnClickListener(this);
        kaolaButton.setOnClickListener(this);
        usbButton.setOnClickListener(this);
        bluetoothButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        final FragmentManager fragmentManager= Objects.requireNonNull(getActivity()).getFragmentManager();
        final FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction);
       switch (view.getId()){
           case R.id.musicButton:
               if(localMusicFragment==null){
                   localMusicFragment=new LocalMusicFragment();
                   fragmentTransaction.add(R.id.localMusicFrameLayout,localMusicFragment);
               }else{
                   fragmentTransaction.show(localMusicFragment);
               }
               break;
           case R.id.fmButton:
               break;
           case R.id.kaolaButton:
               break;
           case R.id.usbButton:
               break;
           case R.id.bluetoothButton:
               break;
               default:
                   break;

       }
       fragmentTransaction.commit();
    }
    private void hideAllFragment(FragmentTransaction fragmentTransaction ){
        if(localMusicFragment!=null){
            fragmentTransaction.hide(localMusicFragment);
        }
    }


}
