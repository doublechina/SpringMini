package com.daycodeday.demo.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.daycodeday.demo.service.IQueryService;
import com.daycodeday.spring.annotation.Service;

/**
 * 查询业务
 */
@Service
public class QueryService implements IQueryService {

    /**
     * 查询
     */
    public String query(String name) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        String json = "{name:\"" + name + "\",time:\"" + time + "\"}";
        return json;
    }

}
