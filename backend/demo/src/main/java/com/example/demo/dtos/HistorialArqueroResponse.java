package com.example.demo.dtos;

import java.util.List;

public class HistorialArqueroResponse {
    private List<HistorialTorneoDTO> torneos;
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;

    public HistorialArqueroResponse() {}

    public HistorialArqueroResponse(List<HistorialTorneoDTO> torneos, Integer page, Integer size, 
                                   Long totalElements, Integer totalPages) {
        this.torneos = torneos;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<HistorialTorneoDTO> getTorneos() {
        return torneos;
    }

    public void setTorneos(List<HistorialTorneoDTO> torneos) {
        this.torneos = torneos;
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
