package com.lambdaschool.cookbook.models.utils;


import com.lambdaschool.cookbook.models.Recipe;

import java.util.List;


/**
 * DTO using for pagination
 */
public class PagingResponse {

    private Long count;
    private Long pageNumber;
    private Long pageSize;
    private Long pageOffset;
    private Long pageTotal;
    private List<Recipe> elements;

    public PagingResponse(
            Long count,
            Long pageNumber,
            Long pageSize,
            Long pageOffset,
            Long pageTotal,
            List<Recipe> elements
    ) {
        this.count = count;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.pageOffset = pageOffset;
        this.pageTotal = pageTotal;
        this.elements = elements;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Long pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Long pageOffset) {
        this.pageOffset = pageOffset;
    }

    public Long getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(Long pageTotal) {
        this.pageTotal = pageTotal;
    }

    public List<Recipe> getElements() {
        return elements;
    }

    public void setElements(List<Recipe> elements) {
        this.elements = elements;
    }

}
