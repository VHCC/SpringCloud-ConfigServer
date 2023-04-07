package com.frank.spring.cloud.configserver.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author IChen.Chu
 * @Date 2023/03/15
 */
public class RequestHelper {

    private static Set<String> filteredKeys;

    static {
        filteredKeys = new LinkedHashSet<String>() {
            {
                add("longitude");
                add("latitude");
            }
        };
    }


    private static ValueFilter filter = new ValueFilter() {
        @Override
        public Object process(Object object, String name, Object value) {
            if ((value instanceof Double || value instanceof BigDecimal || value instanceof Float) && !filteredKeys.contains(name)) {
                return new BigDecimal(value.toString()).setScale(1, BigDecimal.ROUND_HALF_UP);
            }
            return value;
        }
    };


    public static ResponseEntity<?> formatSuccess() {
        return formatError(0, "Success");
    }

    public static ResponseEntity<?> formatError(int errCode, Object errMsg) {
        Map<String, Object> rootmap = new LinkedHashMap<>();
        rootmap.put("errCode", errCode);
        if (errMsg == null) {
            errMsg = new Object();
        }
        rootmap.put("errMsg", errMsg);
        String body = JSON.toJSONString(rootmap, SerializerFeature.WriteMapNullValue);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public static ResponseEntity<?> formatSuccessResult(Object data, SerializerFeature... serializerFeature) {
        Map<String, Object> rootmap = new LinkedHashMap<>();
        rootmap.put("errCode", 0);
        rootmap.put("errMsg", "Success");
        if (data == null) {
            data = new Object();
        }
        rootmap.put("data", data);
        String body = JSON.toJSONString(rootmap, filter, serializerFeature);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
