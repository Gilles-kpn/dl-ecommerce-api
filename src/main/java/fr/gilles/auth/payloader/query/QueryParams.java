package fr.gilles.auth.payloader.query;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class QueryParams {
    private int pageNumber = 0;
    private int pageSize = 12;
    private boolean deleted = false;
    private Sort.Direction direction = Sort.Direction.DESC;
    private String[] fields = {"code"} ;

    public Pageable toPageRequest(){
        return  PageRequest.of(pageNumber, pageSize, Sort.by(direction, fields));
    }
}
