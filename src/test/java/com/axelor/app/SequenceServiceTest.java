package com.axelor.app;

import static org.junit.Assert.*;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.db.service.GstSequenceService;
import com.axelor.inject.Beans;
import com.axelor.test.GuiceModules;
import com.axelor.test.GuiceRunner;
import com.google.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GuiceRunner.class)
@GuiceModules({TestModule.class})
public class SequenceServiceTest {

  @Inject private GstSequenceService service;

  @Test
  public void testdata() {
    Sequence isSequence =
        Beans.get(SequenceRepository.class).all().filter("self.model.name=?", "Invoice").fetchOne();
    assertEquals("Invoice0000sd", service.setSequence(isSequence));
  }

  @Test
  public void testSequence() {
    Sequence isSequence =
        Beans.get(SequenceRepository.class).all().filter("self.model.name=?", "Party").fetchOne();
    assertEquals("party0000", service.setSequence(isSequence));
    service.setReference(isSequence);
  }
}
