import com.google.cloud.bigtable.hbase.BigtableConfiguration
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapreduce.Job
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions.{col, upper}
import org.apache.spark.{SparkContext, sql}
object POC_3 {
    def main(args: Array[String]) {
   // System.setProperty("hadoop.home.dir", "C:\\Hadoop\\bin")
    val appName = this.getClass.getSimpleName.replace("$", "")
    println(s"$appName Spark application is starting up...")

    val projectId ="maximal-valve-330811"
    val instanceId = "poc3-instance"
    val toTable = "poc3_output"
    import org.apache.spark.sql.SparkSession

    val spark = SparkSession
      .builder()
      .appName("Spark POC-3")
      .config("spark.master", "local")
      .getOrCreate()
    println(s"Spark version: ${spark.version}")



   // val conf = BigtableConfiguration.configure(projectId, instanceId)

    // Creating HBaseContext explicitly to use the conf above
    // That's how to use command-line arguments for projectId and instanceId
    // Otherwise, we'd have to use hbase-site.xml
    // See HBaseSparkConf.USE_HBASECONTEXT option in hbase-connectors project
   // new HBaseContext(spark.sparkContext, conf)

      var df = spark.read.options(Map("inferSchema" -> "true", "delimiter" -> ",", "header" -> "true")).csv("gs://equifax-poc-3//sparkdemo.csv")
        df.show()
    val colName = "Name"
    var df2 = df.withColumn(colName, upper(col(colName)))
    val columns = Seq("Name", "Age")
    val df3 = df2.toDF(columns: _*)

      println(s"Writing records to $toTable")

      var hConf = BigtableConfiguration.configure(projectId, instanceId)
      hConf.set(TableOutputFormat.OUTPUT_TABLE, toTable)
      val job = Job.getInstance(hConf)
      job.setOutputFormatClass(classOf[TableOutputFormat[ImmutableBytesWritable]])
      hConf = job.getConfiguration

      import org.apache.spark.SparkConf

      val config = new SparkConf()

      config.set("spark.hadoop.validateOutputSpecs", "false")

      val sc = SparkContext.getOrCreate(config)
      val rows: RDD[sql.Row] = df3.rdd
      val newrows=rows.map(record => (record.get(0).toString(),record.get(1).toString))
      val wordCounts = newrows
        .map { case (name, age) =>
          val ColumnFamilyBytes = Bytes.toBytes("cf")
          val ColumnNameBytes = Bytes.toBytes("Name")
          val put = new Put(Bytes.toBytes(name))
            .addColumn(ColumnFamilyBytes, ColumnNameBytes, Bytes.toBytes(age))
          // The KEY is ignored while the output value must be either a Put or a Delete instance
          // The underlying writer ignores keys, only the value matters here.
          (null, put)
        }
      wordCounts.saveAsNewAPIHadoopDataset(hConf)

    println(s"Writing to $toTable...DONE")

  }
}


