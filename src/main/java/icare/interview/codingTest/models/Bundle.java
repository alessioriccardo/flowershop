package icare.interview.codingTest.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Bundle {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional=false, fetch = FetchType.EAGER)
    @JoinColumn(name="code", nullable=false, updatable=false)
    private Product productCode;

    private BigDecimal cost;

    private Long productQuantity;
}
