package kpl.fiml.global.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

@Getter
public class PageResponse<T, R> {

        private final List<R> content;
        private final int totalPages;
        private final long totalElements;

        public PageResponse(Page<T> pageResponse, Function<T, R> converter) {
            this.content = pageResponse.getContent().stream().map(converter).toList();
            this.totalPages = pageResponse.getTotalPages();
            this.totalElements = pageResponse.getTotalElements();
        }
}
