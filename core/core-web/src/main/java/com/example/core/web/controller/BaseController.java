package com.example.core.web.controller;

import com.example.core.web.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    protected <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    protected <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        return ResponseEntity.ok(ApiResponse.success(message, data));
    }

    protected <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resource created successfully", data));
    }

    protected ResponseEntity<ApiResponse<Void>> noContent() {
        return ResponseEntity.noContent().build();
    }

    protected <T> ResponseEntity<ApiResponse<PagedResponse<T>>> pagedSuccess(Page<T> page) {
        PagedResponse<T> pagedResponse = new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast());
        return ResponseEntity.ok(ApiResponse.success(pagedResponse));
    }

    public static class PagedResponse<T> {
        private final java.util.List<T> content;
        private final int page;
        private final int size;
        private final long totalElements;
        private final int totalPages;
        private final boolean first;
        private final boolean last;

        public PagedResponse(
                java.util.List<T> content,
                int page,
                int size,
                long totalElements,
                int totalPages,
                boolean first,
                boolean last) {
            this.content = content;
            this.page = page;
            this.size = size;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.first = first;
            this.last = last;
        }

        // Getters
        public java.util.List<T> getContent() {
            return content;
        }

        public int getPage() {
            return page;
        }

        public int getSize() {
            return size;
        }

        public long getTotalElements() {
            return totalElements;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public boolean isFirst() {
            return first;
        }

        public boolean isLast() {
            return last;
        }
    }
}
