package com.axelor.gst.db.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.db.repo.GstPartyRepository;
import com.axelor.gst.db.repo.GstProductRepository;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.gst.db.service.GstInvoiceService;
import com.axelor.gst.db.service.GstInvoiceServiceImpl;
import com.axelor.gst.db.service.GstSequenceService;
import com.axelor.gst.db.service.GstSequenceServiceImpl;

public class GstModule extends AxelorModule {
  @Override
  protected void configure() {
    bind(PartyRepository.class).to(GstPartyRepository.class);
    bind(ProductRepository.class).to(GstProductRepository.class);
    bind(GstInvoiceService.class).to(GstInvoiceServiceImpl.class);
    bind(GstSequenceService.class).to(GstSequenceServiceImpl.class);
  }
}
