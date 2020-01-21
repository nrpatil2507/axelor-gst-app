package com.axelor.gst.db.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.db.repo.GstPartyRepository;
import com.axelor.gst.db.repo.GstProductRepository;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.db.repo.ProductRepository;

public class GstModule extends AxelorModule {
  @Override
  protected void configure() {
    bind(PartyRepository.class).to(GstPartyRepository.class);
    bind(ProductRepository.class).to(GstProductRepository.class);
  }
}
