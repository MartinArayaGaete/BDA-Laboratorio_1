package com.example.demo.dtos;

import java.util.List;

public class TorneosPaginadosResponse {
    private List<TorneoListadoDTO> content;
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;

    public TorneosPaginadosResponse() {}

    public TorneosPaginadosResponse(List<TorneoListadoDTO> content, Integer page, Integer size,
                                    Long totalElements, Integer totalPages) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<TorneoListadoDTO> getContent() {
        return content;
    }

    public void setContent(List<TorneoListadoDTO> content) {
        this.content = content;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}
