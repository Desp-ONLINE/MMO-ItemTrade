package com.binggre.mmoitemshop.objects;

import com.binggre.mongolibraryplugin.base.MongoData;
import lombok.Getter;

import java.util.Map;

@Getter
public class ItemTrade implements MongoData<Integer> {

    private int id;
    private String name;

    private String resultId;
    private Map<String, Integer> materialIds;


}