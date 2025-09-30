package:
	mvn clean package

efficiencyTest:
	java -Xms100m -Xmx100m  -XX:StartFlightRecording=filename=app.jfr,dumponexit=true,settings=profile -jar target/InventoryManagement-0.0.1-SNAPSHOT.jar --efficiency.test=true