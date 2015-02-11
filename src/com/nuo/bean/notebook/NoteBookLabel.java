package com.nuo.bean.notebook;

import com.lidroid.xutils.db.annotation.Column;
import com.nuo.bean.EntityBase;

import java.util.Date;

/**
 * Created by zxl on 2015/2/10.
 * 标签
 */
public class NoteBookLabel extends EntityBase {
    @Column(column = "name")
    private String name;
    @Column(column = "create_time")
    private Date create_time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}
