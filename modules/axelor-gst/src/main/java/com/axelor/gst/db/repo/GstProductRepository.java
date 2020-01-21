package com.axelor.gst.db.repo;

import com.axelor.gst.db.Product;
import java.util.Map;

public class GstProductRepository extends ProductRepository {

  @Override
  public Map<String, Object> populate(Map<String, Object> json, Map<String, Object> context) {

    if (!context.containsKey("product-enhance")) {
      return json;
    }
    try {
      Long id = (Long) json.get("id");
      Product product = find(id);
      json.put("hasImage", product.getImage() != null);
    } catch (Exception e) {
    }
    return json;
  }
}
