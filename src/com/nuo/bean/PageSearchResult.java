package com.nuo.bean;

import com.google.gson.Gson;
import com.nuo.utils.JsonUtil;

import java.util.List;

/**
 * Created by zxl on 2015/1/27.
 */
public class PageSearchResult {
    private Integer pageSize;
    private Integer pageNumber;
    private Integer totalRow;
    private Integer totalPage;
    private List<MsgInfo> list;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(Integer totalRow) {
        this.totalRow = totalRow;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<MsgInfo> getList() {
        return list;
    }

    public void setList(List<MsgInfo> list) {
        this.list = list;
    }

    public static PageSearchResult parseMap(String result) {
        Gson gson = JsonUtil.getDateGson();
        return gson.fromJson(result,PageSearchResult.class);
    }
}
