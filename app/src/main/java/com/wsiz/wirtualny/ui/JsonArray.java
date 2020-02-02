package com.wsiz.wirtualny.ui;

import java.util.ArrayList;
import java.util.List;

public class JsonArray {
    private List<JsonObiect> jsonArray;

    public JsonArray(){
        jsonArray=new ArrayList<>();
    }

    public void add(JsonObiect jsonObiect){
        jsonArray.add(jsonObiect);
    }

    public List<JsonObiect> getJsonArray() {
        return jsonArray;
    }

}
