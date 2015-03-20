package com.nuobuluo.huangye.bean.notebook;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.nuobuluo.huangye.bean.EntityBase;

import java.util.Date;

/**
 * Created by zxl on 2015/2/10.
 * 记事本实例
 */
@Table(name = "note_book")
public class NoteBook extends EntityBase {
    @Column(column = "title")
    private String title;
    @Column(column = "content")
    private String content;
    @Column(column = "create_time")
    private Date create_time;
    @Column(column = "update_time")
    private Date update_time;
    @Column(column = "is_sync")
    private Boolean is_sync;
    @Column(column = "notebook_label")
    private String label ;
    @Column(column = "notebook_name")
    private String labelName;
    public String[] urls;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public Boolean getIs_sync() {
        return is_sync;
    }

    public void setIs_sync(Boolean is_sync) {
        this.is_sync = is_sync;
    }

    public String getLabel() {
        return label==null?"":label;
    }

    public String getLabelName() {
        return labelName==null?"":labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

    @Override
    public NoteBook clone() throws CloneNotSupportedException {
        NoteBook noteBook = new NoteBook();
        noteBook.setId(this.getId());
        noteBook.setContent(this.getContent());
        noteBook.setTitle(this.getTitle());
        noteBook.setCreate_time(this.getCreate_time());
        noteBook.setUpdate_time(this.getUpdate_time());
        noteBook.setLabel(this.getLabel());
        return noteBook;
    }
}
