package icare.interview.codingTest.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SubSetCup {

    static int[] a = {9, 5, 3};
    static int numberofCoins = 2;
    static int sum = 25;
    static int i = 0;
    static int partialSolution = 0;
    static List<Integer> partialList = new ArrayList<>();

    static int[] min_coins = new int[sum + 1];
    static int[] last_coin = new int[sum + 1];

    public static void main(String[] args) {
        //System.out.print(find2(a, 0, sum, partialList));
    }

    private static List<Integer> find2(int[] arr, int index, int sum, List<Integer> prefixList) {
        if (sum == 0) {

            return prefixList; // base case,
        }
        if (index >= arr.length || sum < 0){
            return null; // bad case,
        }

        //Exclude
        List<Integer> excludeList = find2(arr, index+1, sum, prefixList);

        //Include
        List<Integer> includePartialList = new ArrayList<>(prefixList);
        includePartialList.add(arr[index]);
        List<Integer> includeList = find2(arr, index, sum-arr[index], includePartialList);

        if (includeList == null && excludeList == null) return null; // both null,
        if (includeList != null && excludeList != null) { // both non-null,
            return includeList.size() < excludeList.size() ? includeList : excludeList;
        }// prefer to include elements with larger index,
        else{
            return includeList == null ? excludeList : includeList; // exactly one null,
        }

    }


}
