package icare.interview.codingTest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
public class OrderDto {

    private static final long serialVersionUID = 1L;

    private Long items;

    private String productCode;
}
