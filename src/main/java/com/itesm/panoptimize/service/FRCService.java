package com.itesm.panoptimize.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FRCService {

    List<String> list = new ArrayList<>();
    list.add("contactHandled");
    list.add("contactsAbandoned");
    list.add("callbackContacts");

    JSONArray ja = new JSONArray(list);
    JSONObject requestMD = new JSONObject();
    requestMD.put("instance_id","1");
    requestMD.put("start_time","2024-01-01");
    requestMD.put("end_time","2024-01-31");
    requestMD.put("metrics",ja);

}
