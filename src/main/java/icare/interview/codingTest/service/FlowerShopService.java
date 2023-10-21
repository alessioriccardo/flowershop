package icare.interview.codingTest.service;

import icare.interview.codingTest.dto.BundleDto;
import icare.interview.codingTest.dto.OrderDto;
import icare.interview.codingTest.models.Bundle;
import icare.interview.codingTest.repositories.BundleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import static java.util.Comparator.comparing;

@Service
public class FlowerShopService implements IFlowerShopService {

    @Autowired
    BundleRepository bundleRepository;

    @Override
    public List<BundleDto> orderElaboration(InputStream inputStream) {

        //opening a bufferedreader to read each line of the InputStream
        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        List<OrderDto> orderList;

        //collect the OrderDtos to process
        orderList = bf.lines().map(this::handleLine).collect(Collectors.toUnmodifiableList());

        List<BundleDto> bundlesForOrder = new ArrayList<>();

        orderList.forEach(order-> {
            Long productQuantity = order.getItems();

            //find the available Bundles for the desired product
            List<Bundle> bundleForProduct = bundleRepository.findByCode(order.getProductCode());

            if (!bundleForProduct.isEmpty()) {

                bundleForProduct.sort(Comparator.comparing(Bundle::getProductQuantity).reversed());

                //getting the minimun Set of Bundles needed to reach the given quantity of product required
                List<Bundle> bundlesForProduct = findBestBundles(productQuantity, bundleForProduct, 0, new ArrayList<Bundle>());

                //List<Bundle> bundlesForProduct = findBestBundles2(bundleForProduct, productQuantity);

                //Total Bundles of the Order mapped to BundleDto for the Controller
                bundlesForOrder.addAll(bundlesForProduct.stream().map(b -> mapMundleToBundleDto(b)).collect(Collectors.toList()));

                //Compute the total amount for the current product
                BigDecimal orderTotal = bundlesForProduct.stream().map(b -> b.getCost()).reduce(BigDecimal.ZERO, BigDecimal::add);

                System.out.println(order.getItems() + " " + order.getProductCode() + " $" + orderTotal);

                //Group the Bundles for the product by productQuantity to print only one line for duplicates Bundles
                Map<Long, Long> bundleGrouped = bundlesForProduct.stream().collect(Collectors.groupingBy(Bundle::getProductQuantity, Collectors.counting()));

                //print result to console
                for (Long key : bundleGrouped.keySet()) {
                    System.out.print("    ");
                    Optional<Bundle> optionalBundle = bundlesForProduct.stream().filter(b -> b.getProductQuantity().equals(key)).findFirst();
                    System.out.println(bundleGrouped.get(key) + " x " + optionalBundle.get().getProductQuantity() + " $" + optionalBundle.get().getCost() + " ");
                };
            }
        });

        return bundlesForOrder;
    }

    /**
     * Mapping method from Entity Bundle to Dto BundleDto
     * @param bundle
     * @return
     */
    private BundleDto mapMundleToBundleDto(Bundle bundle){
        return BundleDto.builder()
                .cost(bundle.getCost())
                .productCode(bundle.getProductCode().getCode())
                .productQuantity(bundle.getProductQuantity())
                .build();
    }

    /**
     * Utility method used to parse the input string and generated the OrderDto
     * @param inputLine
     * @return
     */
    @Override
    public OrderDto handleLine(String inputLine) {
        String[] splitLine = inputLine.split(" ");

        Long items = Long.valueOf(splitLine[0]);
        String code = splitLine[1];

        OrderDto order = new OrderDto(items, code);

        return order;
    }

    /**
     * Method that finds the minimun Set of Bundles needed to reach the given quantity of product required using Dynamic Programming
     * @param quantity of the product we want to reach
     * @param bundlesForProduct the types of Bundles available for the product of the order
     * @param index the index in the List to process in each iteration
     * @param tmpList utility List to store the previous results
     * @return
     */
    private List<Bundle> findBestBundles(Long quantity, List<Bundle> bundlesForProduct, int index,  List<Bundle> tmpList) {
        if (quantity == 0) return tmpList;

        if (quantity < 0 || index >= bundlesForProduct.size()) return null;

        //recursive call to findBestBundles excluding next item
        List<Bundle> resultExcludeList = findBestBundles(quantity, bundlesForProduct, index + 1, tmpList);

        //Include current item
        List<Bundle> includeList = new ArrayList<>(tmpList);
        includeList.add(bundlesForProduct.get(index));

        //recursive call to findBestBundles including current item
        List<Bundle> resultIncludeList = findBestBundles(quantity - bundlesForProduct.get(index).getProductQuantity(), bundlesForProduct, index, includeList);

        if (resultIncludeList == null && resultExcludeList == null){
            return null; //there is no Bundles for the Order with given quantity
        }
        else if (resultIncludeList != null && resultExcludeList != null) {
            //return the shorter list to have the minimun items in the Order
            return resultIncludeList.size() < resultExcludeList.size() ? resultIncludeList : resultExcludeList;
        }
        else {
            //Only one can be null
            return resultIncludeList == null ? resultExcludeList : resultIncludeList;
        }
    }


    /**
     * This is a recursive approach that finds the minimum List of Bundles only if bundlesForProduct are
     *         ordered from the biggest Bundle to the smallest considering the ProductQuantity of each Bundle
     * @param bundlesForProduct the types of Bundles available for the product of the order
     * @param quantity of the product we want to reach
     * @return
     */

    private List<Bundle> findBestBundles2(List<Bundle> bundlesForProduct, Long quantity)
    {
        // if there are no item left
        if (quantity == 0)
            return new ArrayList<Bundle>();

        // Trying every bundle that has quantity value less than given V
        for (int i = 0; i < bundlesForProduct.size(); i++) {
            Bundle bundle = bundlesForProduct.get(i);
            if (bundle.getProductQuantity() <= quantity){
                List<Bundle> result = findBestBundles2(bundlesForProduct, quantity-bundle.getProductQuantity());
                if (result != null) {
                    result.add(bundle);
                    return result;
                }
            }

        }
        return null;
    }
}
