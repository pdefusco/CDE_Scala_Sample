// Import necessary libraries
import org.apache.spark.sql.SparkSession
//import org.apache.spark.sql.types._
//import org.apache.spark.sql.Row

object CdeScalaExample {
  def main(args: Array[String]): Unit = {

    // Create SparkSession
    val spark = SparkSession.builder().appName("SimpleCDEExample").getOrCreate()

    val data = Seq(
      ("Olympia", "Washington"),
      ("Boise", "Idaho"),
      ("Helena", "Montana"),
      ("Lincoln", "Nebraska"),
      ("Concord", "New Hampshire"),
      ("Albany", "New York")
    )

    val df = spark.createDataFrame(data).toDF("Capital", "State")

    df.show()
    }
}
