mvn install:install-file -Dfile=lib/org.moxieapps.gwt.highcharts-1.5.0.jar -DgroupId=org.moxieapps.gwt -DartifactId=highcharts -Dversion=1.5.0 -Dpackaging=jar
 
mvn clean -U gwt:clean compile gwt:resources gwt:compile package -DskipTests=true -Dgwt.module=com.bugseer.BugSeer