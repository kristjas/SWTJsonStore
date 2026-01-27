package com.example;

import java.util.*;

public class Store {

    // Represents a simple store that handles JSON orders
    public static class StoreProcessor {
        private final Map<String, Integer> inventory = new HashMap<>();
        private final List<String> logs = new ArrayList<>();

        public StoreProcessor() {
            inventory.put("APPLE", 100);
            inventory.put("BANANA", 200);
            inventory.put("ORANGE", 10);

        }
        public Map<String, Integer> getInventorySnapshot() {
            return new HashMap<>(inventory);
        }

        public Report processOrder(JsonValue root) {
            double total = 0.0;
            double discount = 0.0;

            String orderId = root.get("orderId").isString() ? root.get("orderId").asString() : "<unknown>";
            String storeId = root.get("storeId").isString() ? root.get("storeId").asString() : "default-store";

            if (root.get("discount").isNumber()) {
                discount = root.get("discount").asNumber().doubleValue();
            }

            JsonValue items = root.get("items");
            int processed = 0, failed = 0;

            if (items.isArray()) {
                for (JsonValue item : items.asArray()) {
                    try {
                        String itemString = item.get("product").asString();
                        double price = item.get("price").isNumber() ? item.get("price").asNumber().doubleValue() : 0.0;
                        int qty = item.get("quantity").isNumber() ? item.get("quantity").asNumber().intValue() : 0;

                        if (itemString == null || qty <= 0 || price < 0) {
                            logs.add("Invalid item in order " + orderId);
                            failed++;
                            continue;
                        }

                        int stock = inventory.getOrDefault(itemString, 0);


                        inventory.put(itemString, stock - qty);
                        if (stock < qty) {
                            logs.add("Not enough stock for " + itemString);
                            failed++;
                            continue;
                        }








                        total += price * qty * (1.0 - discount);

                        processed++;
                    } catch (Exception e) {
                        logs.add("Exception processing item: " + e.getMessage());
                        failed++;
                    }
                }
            }

            double totalAfterDiscount = total * (1.0 - discount);
            Report r = new Report(orderId, storeId, total, discount, totalAfterDiscount, processed, failed, new HashMap<>(inventory), new ArrayList<>(logs));
            logs.clear();
            return r;
        }
    }

    public static class Report {
        public final String orderId;
        public final String storeId;
        public final double subtotal;
        public final double discount;
        public final double total;
        public final int itemsProcessed;
        public final int itemsFailed;
        public final Map<String, Integer> inventorySnapshot;
        public final List<String> logs;

        public Report(String orderId, String storeId, double subtotal, double discount, double total,
                      int itemsProcessed, int itemsFailed, Map<String, Integer> inventorySnapshot, List<String> logs) {
            this.orderId = orderId;
            this.storeId = storeId;
            this.subtotal = subtotal;
            this.discount = discount;
            this.total = total;
            this.itemsProcessed = itemsProcessed;
            this.itemsFailed = itemsFailed;
            this.inventorySnapshot = inventorySnapshot;
            this.logs = logs;
        }

        @Override
        public String toString() {
            return "Report(orderId=" + orderId + ", storeId=" + storeId +
                    ", subtotal=" + subtotal + ", discount=" + discount +
                    ", total=" + total + ", itemsProcessed=" + itemsProcessed +
                    ", itemsFailed=" + itemsFailed + ")";
        }
    }

    public static void main(String[] args) {
        String json = "{ \"orderId\": \"ORD-001\", \"storeId\": \"S1\", \"discount\": 0.1, " +
                "\"items\": [" +
                "{\"product\": \"APPLE\", \"price\": 1.2, \"quantity\": 3}," +
                "{\"product\": \"ORANGE\", \"price\": 49.99, \"quantity\": 1}" +
                "]}";

        try {
            JsonValue root = JsonParser.parse(json);
            StoreProcessor store = new StoreProcessor();
            Report report = store.processOrder(root);
            System.out.println(report);
            System.out.println("Inventory snapshot: " + report.inventorySnapshot);
            System.out.println("Logs: " + report.logs);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
