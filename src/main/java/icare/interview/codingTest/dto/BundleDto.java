package icare.interview.codingTest.dto;

import icare.interview.codingTest.models.Product;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BundleDto {

    private String productCode;

    private BigDecimal cost;

    private Long productQuantity;
}
