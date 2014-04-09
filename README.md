#First time
1. Get the list of files boostrap csvs - smb://batcave/engineering/hackathon-Q2-2014/bugseer/bootstrap_csvs/bugseer-csv.tar.gz and put in the ${project.home}
2. run scripts/create_db.sql
   mysql -h HOST -u USER -pPASSWORD < scripts/create_db.sql
3. mvn install:install-file -Dfile=lib/org.moxieapps.gwt.highcharts-1.5.0.jar -DgroupId=org.moxieapps.gwt -DartifactId=highcharts -Dversion=1.5.0 -Dpackaging=jar

#Package and deploy
1. mvn clean -U gwt:clean compile gwt:resources gwt:compile package -DskipTests=true -Dgwt.module=com.bugseer.BugSeer
2. Copy the directory target/bugseer into your tomcat webapps directory, restart tomcat
