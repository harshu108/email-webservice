package com.harsh.emailservice;


import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Test {

    public static void main(String[] args) {

        MultiValuedMap<String, String> map = new ArrayListValuedHashMap<>();
        String val1 = "val1";
        String val2 = "val2";
        map.put("a", val1);
        map.put("a", "val2");
        map.put("b", "val1");
        map.put("b", "val2");

        System.out.println(Objects.equals(map.get("a"), val1));
        boolean isRemoved = map.removeMapping("a", "val1");
        System.out.println(map.toString());
        System.out.println(isRemoved);

    }
}
