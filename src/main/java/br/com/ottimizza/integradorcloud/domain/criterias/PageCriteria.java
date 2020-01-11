package br.com.ottimizza.integradorcloud.domain.criterias;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    public Integer pageIndex = 0;

    public Integer pageSize = 10;

    public String sortBy;

    public String sortOrder;

    public static class Order {
        public static final String ASC = "asc";
        public static final String DESC = "desc";
    }

}