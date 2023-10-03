package icare.interview.codingTest.service;

import icare.interview.codingTest.dto.BundleDto;
import icare.interview.codingTest.dto.OrderDto;
import icare.interview.codingTest.models.Bundle;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IFlowerShopService {

    List<BundleDto> orderElaboration(InputStream inputStream) throws IOException;

    OrderDto handleLine(String s);
}
