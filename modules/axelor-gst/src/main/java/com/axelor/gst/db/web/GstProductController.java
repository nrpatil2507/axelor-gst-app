package com.axelor.gst.db.web;

import com.axelor.gst.db.Product;
import com.axelor.gst.db.ProductCategory;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class GstProductController {

  public void setGstRate(ActionRequest request, ActionResponse response) {
    Product product = request.getContext().asType(Product.class);

    ProductCategory productCategory = product.getCategory();

    response.setValue("gstRate", productCategory != null ? productCategory.getGstRate() : null);
  }
}
