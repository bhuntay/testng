module org.testng.core.api {
  requires com.google.guice;
  requires java.desktop;
  requires transitive org.slf4j;
  requires transitive org.testng.collections;
  requires org.testng.reflection.utils;
  requires javax.inject;

  exports org.testng;
  exports org.testng.annotations;
  exports org.testng.xml;
  exports org.testng.reporters;
  exports org.testng.internal;
  exports org.testng.log4testng;
}
