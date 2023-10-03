package icare.interview.codingTest.service;

import icare.interview.codingTest.dto.BundleDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class FlowerShopServiceTest {

    @Autowired
    FlowerShopService flowerShopService;

    @Test
    void orderElaboration() throws IOException {
        File initialFile = new File("src/test/resources/inputFile");
        InputStream targetStream = new FileInputStream(initialFile);
        List<BundleDto> response = flowerShopService.orderElaboration(targetStream);

        Assertions.assertThat(response.stream().filter(r -> r.getProductCode().equals("R12")).findFirst().get().getProductQuantity().equals(10L));

        Assertions.assertThat(response.stream().filter(r -> r.getProductCode().equals("L09")).filter(r -> r.getProductQuantity().equals(9L)).count()==1);
        Assertions.assertThat(response.stream().filter(r -> r.getProductCode().equals("L09")).filter(r -> r.getProductQuantity().equals(6L)).count()==1);

        Assertions.assertThat(response.stream().filter(r -> r.getProductCode().equals("T58")).filter(r -> r.getProductQuantity().equals(5L)).count()==2);
        Assertions.assertThat(response.stream().filter(r -> r.getProductCode().equals("T58")).filter(r -> r.getProductQuantity().equals(3L)).count()==1);
    }
}