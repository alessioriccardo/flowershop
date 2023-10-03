package icare.interview.codingTest.controller;

import icare.interview.codingTest.dto.BundleDto;
import icare.interview.codingTest.models.Bundle;
import icare.interview.codingTest.service.FlowerShopService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("flowershop")
public class FlowerShopController {

    private static final Logger logger = Logger.getLogger(FlowerShopController.class.getName());

    @Autowired
    private FlowerShopService flowerShopService;

    @PostMapping("/sendInputFile")
    public ResponseEntity<List<BundleDto>> sendInputFile(@RequestParam("file") MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        return new ResponseEntity<>(flowerShopService.orderElaboration(inputStream), HttpStatus.OK);
    }

    @ExceptionHandler(SQLException.class)
    public String handleSQLException(HttpServletRequest request, Exception ex){
        logger.info("SQLException Occured:: URL="+request.getRequestURL());
        return "database_error";
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Order not valid")
    @ExceptionHandler(IOException.class)
    public void handleIOException(){
        logger.log(Level.SEVERE, "Order not valid");
    }
}
