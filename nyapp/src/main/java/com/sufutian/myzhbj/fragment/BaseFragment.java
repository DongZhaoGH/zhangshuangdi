package com.sufutian.myzhbj.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 碎片的基类
 */
public abstract class BaseFragment extends Fragment{

    /**
     * 获取当前fragment所在的activity
     */
    public  Activity mainActivty;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivty = getActivity();//获取当前fragment所在的activity
    }

    // 初始化fragment的布局
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       
       View view=innitView();
        return view;

    }



    // fragment所依赖的activity的onCreate方法执行结束
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化数据
        inniData();
        System.out.print("初始化了布局...");
    }


    public abstract  View innitView();
    public abstract void inniData();

}
