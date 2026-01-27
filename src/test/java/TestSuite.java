import com.example.JsonParser;
import com.example.JsonValue;
import com.example.Store;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class TestSuite {
    //1. JSON PARSE TESTS

    @Test
    void testParseSimpleObject() {
        JsonValue val = JsonParser.parse("{\"name\":\"Alice\",\"age\":30}");
        assertTrue(val.isObject());
        assertEquals("Alice", val.get("name").asString());
        assertEquals(30, val.get("age").asNumber().intValue());
    }

    @Test
    void testParseArray() {
        JsonValue val = JsonParser.parse("[1, 2, 3]");
        assertTrue(val.isArray());
        assertEquals(3, val.asArray().size());
        assertEquals(2, val.get(1).asNumber().intValue());
    }

    @Test
    void testParseNestedObject() {
        String json = "{\"user\":{\"name\":\"Bob\",\"roles\":[\"admin\",\"user\"]}}";
        JsonValue root = JsonParser.parse(json);
        assertEquals("Bob", root.get("user").get("name").asString());
        assertEquals("admin", root.get("user").get("roles").get(0).asString());
    }

    @Test
    void testParseInvalidJsonThrows() {
        assertThrows(RuntimeException.class, () -> {
            JsonParser.parse("{\"badJson\": ");
        });
    }

    @Test
    void testParseBooleanAndNull() {
        JsonValue val = JsonParser.parse("{\"a\":true,\"b\":false,\"c\":null}");
        assertTrue(val.get("a").asBoolean());
        assertFalse(val.get("b").asBoolean());
        assertTrue(val.get("c").isNull());
    }

    @Test
    void testParseNumberTypes() {
        JsonValue val = JsonParser.parse("{\"int\":10,\"float\":12.5}");
        assertEquals(10, val.get("int").asNumber().intValue());
        assertEquals(12.5, val.get("float").asNumber().doubleValue());
    }

    @Test
    void testStringEscapes() {
        JsonValue val = JsonParser.parse("{\"text\":\"Line1\\nLine2\"}");
        assertEquals("Line1\nLine2", val.get("text").asString());
    }






    //2. STORE PROCESS TESTS

/*
    @Test
    void testValidOrderProcessesCorrectly() {
        String json = """
        {
          "orderId": "ORD-100",
          "storeId": "S1",
          "discount": 0.1,
          "items": [
            {"product": "APPLE", "price": 1.0, "quantity": 10},
            {"product": "BANANA", "price": 2.0, "quantity": 5}
          ]
        }""";

        JsonValue root = JsonParser.parse(json);
        Store.StoreProcessor processor = new Store.StoreProcessor();
        Store.Report report = processor.processOrder(root);

        assertEquals(2, report.itemsProcessed);
        assertEquals(0, report.itemsFailed);
        assertEquals(18, report.total); // discount applied
    }



    @Test
    void testInvalidProductIsLoggedAndFails() {
        String json = """
        {"orderId":"ORD-200","storeId":"S1","items":[
          {"product":"NOTFOUND","price":1.0,"quantity":2}
        ]}""";

        JsonValue root = JsonParser.parse(json);
        Store.StoreProcessor processor = new Store.StoreProcessor();
        Store.Report report = processor.processOrder(root);
        //This product is not in our inventory so the test fails
        assertEquals(0, report.itemsProcessed);
        assertEquals(1, report.itemsFailed);
        assertFalse(report.logs.isEmpty());
    }

    @Test
    void testNegativePriceFails() {
        String json = """
        {"orderId":"ORD-300","storeId":"S1","items":[
          {"product":"APPLE","price":-5,"quantity":2}
        ]}""";
        JsonValue root = JsonParser.parse(json);
        Store.StoreProcessor processor = new Store.StoreProcessor();
        Store.Report report = processor.processOrder(root);
        //One failed item due to the price being negative
        assertEquals(0, report.itemsProcessed);
        assertEquals(1, report.itemsFailed);
    }
    @Test
    void testStockInsufficient() {
        Store.StoreProcessor processor = new Store.StoreProcessor();

        // Capture inventory BEFORE processing
        Map<String, Integer> beforeInventory = processor.getInventorySnapshot();

        String json = """
       {"orderId":"ORD-400","storeId":"S1","items":[
         {"product":"ORANGE","price":50,"quantity":999}
       ]}
       """;

        JsonValue root = JsonParser.parse(json);
        Store.Report report = processor.processOrder(root);

        // Capture inventory AFTER processing
        Map<String, Integer> afterInventory = processor.getInventorySnapshot();

        // When stock is insufficient, the order is not fulfilled and inventory stays the same.
        assertEquals(beforeInventory, afterInventory);
    }



    @Test
    void testDiscountZeroOrMissingHandled() {
        String json = """
        {"orderId":"ORD-500","storeId":"S1","items":[
          {"product":"APPLE","price":2.0,"quantity":2}
        ]}""";
        JsonValue root = JsonParser.parse(json);
        Store.StoreProcessor processor = new Store.StoreProcessor();
        Store.Report report = processor.processOrder(root);

        assertEquals(4.0, report.subtotal);
        //The subtotal and total should be the same
        assertEquals(report.subtotal, report.total); // no discount applied
    }

    @Test
    void testEmptyItemsArray() {
        String json = """
        {"orderId":"ORD-600","storeId":"S1","items":[]}""";
        JsonValue root = JsonParser.parse(json);
        Store.StoreProcessor processor = new Store.StoreProcessor();
        Store.Report report = processor.processOrder(root);

        //No items so everything = 0
        assertEquals(0, report.itemsProcessed);
        assertEquals(0, report.itemsFailed);
        assertEquals(0.0, report.subtotal);
    }

    @Test
    void testMissingItemsFieldHandled() {

        String json = "{\"orderId\":\"ORD-700\",\"storeId\":\"S1\"}";
        JsonValue root = JsonParser.parse(json);
        Store.StoreProcessor processor = new Store.StoreProcessor();
        Store.Report report = processor.processOrder(root);

        //No items dealt with gracefully
        assertEquals(0, report.itemsProcessed);
        assertTrue(report.logs.isEmpty()); // no items mean nothing to process
    }

 */


}
