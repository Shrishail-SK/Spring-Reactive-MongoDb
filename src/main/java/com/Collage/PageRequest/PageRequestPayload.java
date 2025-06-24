package com.Collage.PageRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class PageRequestPayload {
    private int page = 0;
    private int size = 10;
    private String sortBy = "personId";
    private boolean asscending = true;

    public Pageable getPageable(){
        Sort sort = asscending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(page, size,sort);
    }

}
