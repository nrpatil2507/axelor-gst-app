package com.axelor.app;

import com.axelor.auth.AuthModule;
import com.axelor.db.JpaModule;
import com.google.inject.AbstractModule;

public class TestModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new JpaModule("persistenceUnit"));
    install(new AppModule());
    install(new AuthModule());
  }
}
