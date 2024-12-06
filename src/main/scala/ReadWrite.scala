// Import necessary libraries
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.Row

object ReadWrite {
  def main(args: Array[String]): Unit = {

    // Create SparkSession
    val spark = SparkSession.builder().appName("writetests").getOrCreate()

    val schema = StructType(Seq(StructField("col1",StringType), StructField("col2",StringType)))
    val df = spark.createDataFrame(spark.sparkContext.parallelize(Seq(Row("1", "One"),Row("2", "Two"))), schema)

    // Write to Iceberg
    df.writeTo("default.icebergtable").using("iceberg").create()

    // Write to Hive
    df.write.saveAsTable("default.hivetable")
    }
}
