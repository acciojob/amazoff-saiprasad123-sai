package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
public class OrderRepository {

    HashMap<String,Order> orderDb = new HashMap<>();
    HashMap<String,DeliveryPartner> partnerDb = new HashMap<>();
    HashMap<String,List<String>> assigned = new HashMap<>();
    HashSet<String> unAssigned = new HashSet<>();


    public void addOrder(Order order) {
        orderDb.put(order.getId(), order);
    }

    public void addPartner(String partnerId) {
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        partnerDb.put(partnerId,partner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if(assigned.get(partnerId)==null)
            assigned.put(partnerId,new ArrayList<>());
        assigned.get(partnerId).add(orderId);
    }

    public Order getOrderById(String orderId) {

        return orderDb.getOrDefault(orderId,null);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return partnerDb.getOrDefault(partnerId,null);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        return assigned.get(partnerId)==null?0:assigned.get(partnerId).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return assigned.getOrDefault(partnerId,new ArrayList<>());
    }

    public List<String> getAllOrders() {
        List<String> list = new ArrayList<>();
        for(String orderId:orderDb.keySet())
        list.add(orderId);
        return list;
    }

    public Integer getCountOfUnassignedOrders() {
        return unAssigned.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        Integer ans = 0;
        int timeGiven = Integer.valueOf(time.substring(0,2))*60+Integer.valueOf(4);
        List<String> list = assigned.get(partnerId);
        for(int i=0;i<list.size();i++){
            Order order = orderDb.get(list.get(i));
            if(order.getDeliveryTime()>timeGiven)
                ans++;
        }
        return ans;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        List<String> list = assigned.get(partnerId);
        int max = 0;
        for(int i=0;i<list.size();i++){
            Order order = orderDb.get(list.get(i));
            max = Math.max(max,order.getDeliveryTime());
        }
        String res = "";
        res+=max/60+":"+max%60;
        return res;
    }

    public void deletePartnerById(String partnerId) {
        List<String> list = assigned.getOrDefault(partnerId,new ArrayList<>());
        for(String s:list)
            unAssigned.add(s);
        partnerDb.remove(partnerId);
        if (assigned.containsKey(partnerId))
            assigned.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {
        if(unAssigned.contains(orderId))
            unAssigned.remove(orderId);
        for(String s:assigned.keySet()) {
            List<String> orderList = assigned.get(s);
            if (orderList.contains(orderId)) {
                orderList.remove(orderId);
                break;
            }
        }
        orderDb.remove(orderId);
    }
}
