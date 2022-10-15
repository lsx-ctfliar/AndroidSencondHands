package com.example.second_handshop;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetGoodsDetail {
    private int price;
    private String addr;
    private String userName;
    private String content;
    private String typeName;
    private URL imageUrL;

    public GetGoodsDetail(int price, String addr, String userName, String content, String typeName, URL imageUrL) {
        this.price = price;
        this.addr = addr;
        this.userName = userName;
        this.content = content;
        this.typeName = typeName;
        this.imageUrL = imageUrL;
    }

    public GetGoodsDetail(int price, String addr) {
        this.price = price;
        this.addr = addr;
    }

    private static int[] iconArray;
    private static String[] nameArray;
    private static String[] descArray;

    public static List<GetGoodsDetail> getDefaultList() {
        List<GetGoodsDetail> planetList = new ArrayList<GetGoodsDetail>();
        for (int i = 0; i < iconArray.length; i++) {
            planetList.add(new GetGoodsDetail(iconArray[i], nameArray[i]));
        }
        return planetList;
    }

}
