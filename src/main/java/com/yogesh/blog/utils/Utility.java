package com.yogesh.blog.utils;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class Utility {
    public Set<String> getUniqueStringValuesForUpdatingObjects(List<?> list){
        Set<String> uniqueStrings = new HashSet<>();
        list.forEach(E->{
            var obj = (LinkedHashMap<?, ?>) E;
            String str =(String) obj.get("name");
            uniqueStrings.add(str);
        });
        return uniqueStrings;
    }
}
