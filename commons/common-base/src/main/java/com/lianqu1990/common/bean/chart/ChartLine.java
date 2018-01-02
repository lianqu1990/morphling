package com.lianqu1990.common.bean.chart;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * @author hanchao
 * @date 2017/4/29 13:16
 */
public class ChartLine implements Serializable{
    private static final long serialVersionUID = -3028960957952222817L;

    public ChartLine(String name){
        this.name = name;
    }

    /**
     * 名字,serias
     */
    private String name;
    /**
     * 每一项的
     */
    public Collection<String> labels;
    public Collection<Integer> datas;

    /**
     *
     */
    private LinkedHashMap<String,Integer> preview = new LinkedHashMap<String,Integer>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<String> getLabels() {
        return preview.keySet();
    }

    public Collection<Integer> getDatas() {
        return preview.values();
    }

    public LinkedHashMap<String, Integer> getPreview() {
        return preview;
    }

    public void setPreview(LinkedHashMap<String, Integer> preview) {
        this.preview = preview;
    }
}
