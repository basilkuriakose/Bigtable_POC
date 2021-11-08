name := "POC-3NEW"

version := "0.1"

scalaVersion := "2.11.12"
crossPaths :=false //remove scalaversion from build name
isSnapshot :=false
overrideBuildResolvers := true
updateOptions := updateOptions.value.withGigahorse(false)

//site.settings see https://www.scala-sbt.org/sbt-site/migration-guide.html#migrating-from-version-0-x-x-to-1-x-x
//site.includeScaladoc()
//enablePlugins(SiteScaladocPlugin)


libraryDependencies ++= Seq(

    "org.apache.spark" %% "spark-sql" %  "2.4.0",

    "org.apache.hbase.connectors.spark" % "hbase-spark" % "1.0.0",
    "com.google.cloud.bigtable" % "bigtable-hbase-2.x-hadoop" % "1.23.1" exclude("org.apache.hbase","hbase-client"),

    "org.apache.spark" %% "spark-streaming" % "2.4.0"
  )


libraryDependencies ~= { _ map {
  case m if m.organization == "com.typesafe.play" =>
    m.exclude("commons-logging", "commons-logging").
      exclude("com.typesafe.play", "sbt-link")
  case m => m
}}

dependencyOverrides ++= {
  Seq(

    "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.6.7.1",
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.7",
    "com.fasterxml.jackson.core" % "jackson-core" % "2.6.7",


  )
}


unmanagedJars in Compile := (baseDirectory.value ** "*.jar").classpath




//
//  "org.apache.spark" %% "spark-core" % sparkVersion,
//  "org.apache.spark" %% "spark-sql" % sparkVersion,
//  "com.databricks" %% "spark-csv" % "1.5.0",
//  "org.apache.hbase.connectors.spark" % "hbase-spark" % "1.0val sparkVersion = "2.4.0"
////val bigtableVersion = "1.23.1"
////val hbaseVersion = "2.4.7"
////mainClass := Some("POC_3")
////libraryDependencies ++= Seq(
////  "org.apache.spark" %% "spark-sql" % "2.4.0",
////  "org.apache.hbase.connectors.spark" % "hbase-spark" % "1.0.0",
////  "com.google.cloud.bigtable" % "bigtable-hbase-2.x-hadoop" % "1.23.1" exclude("org.apache.hbase","hbase-client"),
////  "org.apache.spark" %% "spark-streaming" % "2.4.0",
////  "org.apache.zookeeper" % "zookeeper" % "3.5.7",
////  "io.grpc" % "grpc-core" % "1.39.0".0" ,
//  "com.google.cloud.bigtable" % "bigtable-hbase-2.x-hadoop" % "1.23.1" ,
//  "com.google.cloud.bigtable" % "bigtable-hbase" % "1.25.1",
  //"com.google.cloud.bigtable" % "bigtable-client-core" % "1.12.1",
 // "org.apache.hbase" % "hbase-spark" % "2.0.0-alpha3",
 // "io.grpc" % "grpc-all" % "1.29.0",
 // "org.apache.hadoop" % "hadoop-client" % "3.3.1",
 // "org.apache.hadoop" % "hadoop-hdfs" % "3.3.1" % Test
//)



//dependencyOverrides ++= {
//  Seq(
//
//    "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.6.7.1",
//    "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.7",
//    "com.fasterxml.jackson.core" % "jackson-core" % "2.6.7",
//
//
//  )
//}
//val scalatestVersion = "3.2.6"
//libraryDependencies += "org.scalactic" %% "scalactic" % scalatestVersion
//libraryDependencies += "org.scalatest" %% "scalatest" % scalatestVersion % "test"
//test in assembly := {}
//
//val fixes = Seq(
//  // Required by 'value org.apache.hadoop.hbase.spark.HBaseContext.dstream'
//  "org.apache.spark" %% "spark-streaming" % sparkVersion,
//  // hbase-server is needed because HBaseContext references org/apache/hadoop/hbase/fs/HFileSystem
//  // hbase-client is declared to override the version of hbase-client declared by bigtable-hbase-2.x-hadoop
//  "org.apache.hbase" % "hbase-server" % hbaseVersion,
//  "org.apache.hbase" % "hbase-client" % hbaseVersion
//)
//libraryDependencies ++= fixes
//
//// Fix for Exception: Incompatible Jackson 2.9.2
//// Version conflict between HBase and Spark
//// Forcing the version to match Spark
//dependencyOverrides += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.12.1"

// Excluding duplicates for the uber-jar
// There are other deps to provide necessary packages
//excludeDependencies ++= Seq(
//  ExclusionRule(organization = "asm", "asm"),
//  ExclusionRule(organization = "commons-beanutils", "commons-beanutils"),
//  ExclusionRule(organization = "commons-beanutils", "commons-beanutils-core"),
//  ExclusionRule(organization = "org.mortbay.jetty", "servlet-api")
//)

//assemblyMergeStrategy in assembly := {
//  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
//  case x => MergeStrategy.first
//}