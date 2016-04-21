package com.sufutian.myzhbj.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.sufutian.myzhbj.R;
import com.sufutian.myzhbj.activity.MainActivity;
import com.sufutian.myzhbj.base.imlbasepager.NewsPager;
import com.sufutian.myzhbj.domain.NewsMenu;

import java.util.ArrayList;

/**
 * 左侧菜单栏碎片
 */
public class LeftMenuFragment extends  BaseFragment {

    private static final String TAG = "LeftMenuFragment";
    private ListView lv_left_menu;

    private LeftMenuAdapter adapter;
    private ArrayList<NewsMenu.NewsMenuData> datalist;

    private int currentPosition;

    @Override
    public View innitView() {
        View view=View.inflate(mainActivty, R.layout.fragment_leftmenu,null);
        lv_left_menu = (ListView) view.findViewById(R.id.lv_left_menu);
        return view;
    }

    @Override
    public void inniData() {
    }
    /**
     *  在加载newspager时给 leftmenu设置数据
     */
    public void setMenuData(ArrayList<NewsMenu.NewsMenuData> data) {
        currentPosition=0;
        datalist = data;
        //适配器
        adapter=new LeftMenuAdapter();
        lv_left_menu.setAdapter(adapter);
        lv_left_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //记录当前位置 显示状态选择器
                currentPosition=position;
                adapter.notifyDataSetChanged();
                //改变侧拉菜单栏的显示和关闭  控件的方法
                toggle();
                // 侧边栏点击之后, 要修改新闻中心的FrameLayout中的内容
                setCurrentDetailPager(position);
            }
        });

    }

    /**
     * 侧边栏点击之后, 要修改新闻中心的FrameLayout中的内容 设置新闻详情页
     * @param position
     */
    private void setCurrentDetailPager(int position) {

        MainActivity MainUi= (MainActivity) mainActivty;
        ContentFragment contentFragment = MainUi.getContentFragment();

        NewsPager newsPager = contentFragment.getNewsPager();//得到新闻中心

        //调用新闻中心的方法设置菜单详情页
        newsPager.setCurrentDetailPager(position);


    }

    /**
     * //改变侧拉菜单栏的显示和关闭  控件的方法
     */
    private void toggle() {

       MainActivity MainUi= (MainActivity) mainActivty;
        SlidingMenu slidingMenu = MainUi.getSlidingMenu();
        slidingMenu.toggle();//打开或关闭

    }


    class LeftMenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return datalist.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

           View view=View.inflate(mainActivty,R.layout.item_left_menu,null);
            TextView tv= (TextView) view.findViewById(R.id.tv_menu);
            tv.setText(datalist.get(position).title);

            if(position==currentPosition){
                tv.setEnabled(true);
            }else{
                tv.setEnabled(false);
            }

            return view;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

    }
}
