package com.sufutian.myzhbj.domain;

import java.util.ArrayList;

public class NewsMenu {

    public int retcode;
    public ArrayList<Integer> extend;
    public ArrayList<NewsMenuData> data;


    public class NewsMenuData{

        public int id;
        public String title;
        public int type;

        public ArrayList<newsTabDetail> children;


    }

    public class newsTabDetail{
        public int id;
        public String title;
        public int type;
        public String url;

    }


}
