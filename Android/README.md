### Service end-points:

Pending...

### For build and deploy on PCF use bellow command.
	mvn clean install
	mvn com.fdc.cloudfoundry::cf-maven-plugin::1.0.1-SNAPSHOT::simpleDeploy -Dcf.plugin.password=xxx
	