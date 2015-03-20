package com.nuobuluo.huangye.bean.notebook;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.nuobuluo.huangye.bean.EntityBase;

import java.util.Date;

/**
 * Created by zxl on 2015/3/3.
 * 笔记录音bean
 */
@Table(name = "note_book_record")
public class NoteBookRecord extends EntityBase{
    /** 录音备注 **/
    @Column(column = "note")
    private String note;
    /** 录音名称 **/
    @Column(column = "name")
    private String name;

    /** 录音文件保存路径 **/
    @Column(column = "path")
    private String path;
    /** 录音时长 **/
    @Column(column = "length")
    private float length;
    /** 录音创建时间 **/
    @Column(column = "createTime")
    private Date createTime;
    /** 录音所属笔记ID **/
    @Column(column = "note_book_id")
    private int note_book_id;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getNote_book_id() {
        return note_book_id;
    }

    public void setNote_book_id(int note_book_id) {
        this.note_book_id = note_book_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
