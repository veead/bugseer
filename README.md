#First time
1. Get the list of files boostrap csvs from zip and extract it to the ${project.home} -https://appdirect.jira.com/wiki/display/~narayanan.venkiteswaran/bugseer
2. run scripts/create_db.sql
   mysql --local-infile -h HOST -u USER -pPASSWORD < scripts/create_db.sql
3. mvn install:install-file -Dfile=lib/org.moxieapps.gwt.highcharts-1.5.0.jar -DgroupId=org.moxieapps.gwt -DartifactId=highcharts -Dversion=1.5.0 -Dpackaging=jar

#Package and deploy
1. mvn clean -U gwt:clean compile gwt:resources gwt:compile package -DskipTests=true -Dgwt.module=com.bugseer.BugSeer
2. Copy the directory target/bugseer into your tomcat webapps directory
3. Change the app.properties in $TOMCAT_HOME/webapps/bugseer/WEB-INF/classes/app.properties and add your git credentials under git.username and git.password
4. restart tomcat
