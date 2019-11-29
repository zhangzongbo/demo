package com.example.demo.entity;

import com.example.demo.entity.enums.AppEnum;

/**
 * @author zhangzongbo
 */
public class Page {

    public static final String ASC = "asc";
    public static final String DESC = "desc";
    public static final String DEFAULT_SORT = AppEnum.OrderMap.id.getColumn();
    private Integer start = 0;
    private Integer limit = 20;
    private String sort = DEFAULT_SORT;
    private String order = ASC;
    private static Page pageMax;
    private static Integer pageMaxAmount = 10000;

    public static Integer getPageMaxAmount() {
        return pageMaxAmount;
    }

    public Page() {
    }

    public Page(Integer start, Integer limit, String sort, String order) {
        this.start = start;
        this.limit = limit;
        this.sort = sort;
        this.order = order;
    }

    @Override
    public String toString() {
        return "Page{"
                + "start="
                + start
                + ", limit="
                + limit
                + ", sort='"
                + sort
                + '\''
                + ", order='"
                + order
                + '\''
                + '}';
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public static Page getPageMax() {
        if(pageMax == null) {
            pageMax = new Page();
            pageMax.setStart(0);
            pageMax.setLimit(10000);
        }
        return pageMax;
    }

    public static Page getPagMin() {
        Page p = new Page();
        p.setStart(0);
        p.setLimit(0);
        return p;
    }
}
