## CDE Scala Example

#### Objective

This project provides a quickstart for submitting Spark applications compiled from Scala code in Cloudera Data Engineering. It contains useful information targeted towards Spark developers and users who are migrating to Cloudera Data Engineering, such as:

* Context about Scala Jobs and the usage of Jars when running Spark Applications in CDE.
* Useful information about the CDE CLI and how it can simplify your migration efforts to CDE.
* Links to references and other recommended examples such as submitting Spark App Code located in S3 and the CDE Spark Migration CLI Tool.

#### Requirements

In order to reproduce this example you will need a CDE Service in Cloudera on Cloud or On Prem and a working installation of the CDE CLI.

The Scala job application has already been compiled into a Jar via sbt and is available in the `target/scala-2.12/cde-scala-example_2.12-0.1.jar` path.

#### Background Information on Scala, Spark and the Jar File Usage

A JAR (Java ARchive) file is essentially a compressed file that contains compiled Java or Scala code, along with any dependencies or libraries that the code relies on. In the context of Spark, these JARs are used to package and distribute Spark applications.

To develop a Spark application using Scala, the process typically involves writing the application code in Scala in an IDE, compiling the code into bytecode, and packaging it into a JAR file. This JAR file contains all the necessary components for Spark to execute the application on a distributed cluster.

The JAR is then submitted to a Spark cluster using the `spark-submit` command, which handles the distribution of tasks across the cluster, allowing for parallel processing of data.

When submitting a Spark application, the JAR file is provided as part of the job configuration, and Spark automatically loads the necessary code and libraries from the JAR to run the application. This makes Scala JARs an essential component for deploying and executing Spark applications, especially in distributed environments, where scalability and fault tolerance are key requirements.

#### Steps to Reproduce

The process of submitting a Scala application as a Jar to a CDE Virtual Cluster is very similar to that of a Spark Cluster. In CDE, you can use the CDE CLI or UI, which offers syntax that is nearly identical to that of a spark-submit command, along with enhanced options to manage, monitor, schedule jobs, resources inside the CDE Virtual Cluster, and more.

Using the CDE CLI, run the following commands:

**Spark Submit with App Code in Jar file**

You can run the spark submit directly with the CDE CLI:

```
cde spark submit \
  --executor-cores 2 \
  --executor-memory "4g" \
  <path/to/jar-file.jar>
```

For example:

```
% cde spark submit --executor-cores 2 --executor-memory "4g" target/scala-2.12/cde-scala-example_2.12-0.1.jar
3.9KB/3.9KB 100% [==============================================] cde-scala-example_2.12-0.1.jar
Job run 12 submitted
Waiting for job run 12 to start...  
+ echo driver --proxy-user pauldefusco --properties-file /opt/spark/conf/spark.properties --class CdeScalaExample spark-internal
driver --proxy-user pauldefusco --properties-file /opt/spark/conf/spark.properties --class CdeScalaExample spark-internal
+ [[ true == \t\r\u\e ]]
+ POSITIONAL=()
+ [[ 8 -gt 0 ]]
+ key=driver
+ case $key in
[...]
25-03-17 00:03:35 INFO S3ADelegationTokens: Creating Delegation Token: duration 0:00.000s
25-03-17 00:03:35 INFO SharedState: Warehouse path is 's3a://pdf-3425-buk-c59557bd/data/warehouse/tablespace/external/hive'.
25-03-17 00:03:35 INFO ServerInfo: Adding filter to /SQL: com.cloudera.cde.security.authentication.server.JWTRedirectAuthenticationFilter
25-03-17 00:03:35 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@7d569c10{/SQL,null,AVAILABLE,@Spark}
25-03-17 00:03:35 INFO ServerInfo: Adding filter to /SQL/json: com.cloudera.cde.security.authentication.server.JWTRedirectAuthenticationFilter
25-03-17 00:03:35 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@6da53709{/SQL/json,null,AVAILABLE,@Spark}
25-03-17 00:03:35 INFO ServerInfo: Adding filter to /SQL/execution: com.cloudera.cde.security.authentication.server.JWTRedirectAuthenticationFilter
25-03-17 00:03:35 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@2b55ac77{/SQL/execution,null,AVAILABLE,@Spark}
25-03-17 00:03:35 INFO ServerInfo: Adding filter to /SQL/execution/json: com.cloudera.cde.security.authentication.server.JWTRedirectAuthenticationFilter
25-03-17 00:03:35 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@12ec5e73{/SQL/execution/json,null,AVAILABLE,@Spark}
25-03-17 00:03:35 INFO ServerInfo: Adding filter to /static/sql: com.cloudera.cde.security.authentication.server.JWTRedirectAuthenticationFilter
25-03-17 00:03:35 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@42130a5c{/static/sql,null,AVAILABLE,@Spark}
25-03-17 00:03:37 INFO CodeGenerator: Code generated in 163.50901 ms
25-03-17 00:03:37 INFO CodeGenerator: Code generated in 15.652979 ms
+-------+-------------+
|Capital|        State|
+-------+-------------+
|Olympia|   Washington|
|  Boise|        Idaho|
| Helena|      Montana|
|Lincoln|     Nebraska|
|Concord|New Hampshire|
| Albany|     New York|
+-------+-------------+
```

For more information on the CDE Spark-Submit, more in-depth examples of the CDE CLI and migrating spark-submits to CDE via the Spark Migration CLI tool, visit the artilces and references listed in the Summary and Next Steps section.

**CDE Spark Job with App Code in Jar File**

You can also create a CDE Resource to host the Jar dependency inside the CDE Virtual Cluster, and then create the CDE Job of type Spark.

This is particularly advantageous when you have to manage complex spark submits as it allows you to create a reusable job definitions which are automatically tracked across runs.

Job definitions are essentially collections of all spark-submit options, configurations, and dependencies.

Create the CDE Files Resource:

```
cde resource create \
  --type files \
  --name my-jars
```

```
cde resource upload \
  --name my-jars \
  --local-path target/scala-2.12/cde-scala-example_2.12-0.1.jar
```

And finally create and run the Job. Notice that executor resource options are the same as those in the spark-submit. You can assume that all spark-submit options, aside few exceptions, are compatible with the CDE CLI and the CDE Job definition specifically.

```
cde job create \
  --name cde-scala-job \
  --type spark \
  --executor-cores 2 \
  --executor-memory "4g" \
  --mount-1-resource my-jars \
  --application-file cde-scala-example_2.12-0.1.jar
```

```
cde job run \
  --name cde-scala-job
```

```
cde job run \
  --name cde-scala-job \
  --executor-memory "2g"
```

Notice the `application file` option is used to designate the jar file containing application code. The file is located in the `my-jars` files resource we created earlier, which is referenced via the `mount-1-resource` option. As a way to manage complex spark-submits, you can mount more than one files resource when creating or executing your jobs.

Also notice the `executor memory` option was set to `4g` during job creation and then overridden to `2g` during execution. Another advantage of CDE Jobs vs Spark-Submits is the ability to override properties on a dynamic basis. In this case, you first created a template for the job such that, unless specified otherwise, all executions default to 2 GB executor memory - and ran the job. Then, you updated the default setting and ran for a second time.

For documentation and more in-depth examples of the CDE CLI and migrating spark-submits to CDE via the Spark Migration CLI tool, visit the artilces and references listed in the Summary and Next Steps section.

**Monitor CDE Files Resources and Job Executions**

As mentioned above, among many things the CDE CLI provides commands to monitor your jobs and resources. Run the following commands to monitor this use case.

Search for all jobs which use the Jar file as application code:

```
cde job list \
  --filter 'spark.file[eq]<your-jar-file-name>'
```

For example:

```
% cde job list --filter 'spark.file[eq]cde-scala-example_2.12-0.1.jar'
[
  {
    "name": "cde-scala-job",
    "type": "spark",
    "created": "2025-03-17T00:49:05Z",
    "modified": "2025-03-17T00:49:05Z",
    "retentionPolicy": "keep_indefinitely",
    "mounts": [
      {
        "dirPrefix": "/",
        "resourceName": "my-jars"
      }
    ],
    "spark": {
      "file": "cde-scala-example_2.12-0.1.jar",
      "driverMemory": "1g",
      "driverCores": 1,
      "executorMemory": "4g",
      "executorCores": 2
    },
    "schedule": {
      "enabled": false,
      "user": "pauldefusco"
    }
  }
]
```

You can also search for all job runs where `executor-memory` was set to `4g`:

```
% cde job list --filter 'spark.executorMemory[eq]4g'
  {
    "name": "cde-scala-job",
    "type": "spark",
    "created": "2025-03-17T00:49:05Z",
    "modified": "2025-03-17T00:49:05Z",
    "retentionPolicy": "keep_indefinitely",
    "mounts": [
      {
        "dirPrefix": "/",
        "resourceName": "my-jars"
      }
    ],
    "spark": {
      "file": "cde-scala-example_2.12-0.1.jar",
      "driverMemory": "1g",
      "driverCores": 1,
      "executorMemory": "4g",
      "executorCores": 2
    },
    "schedule": {
      "enabled": false,
      "user": "pauldefusco"
    }
  }
]
```

For more information on this specific topic, visit the `Monitoring CDE with the CDE CLI` article referenced in the Summary and Next Steps section.


## Summary and Next Steps

Cloudera Data Engineering (CDE) provides a command line interface (CLI) client. You can use the CLI to create and update jobs, view job details, manage job resources, run jobs, and so on.

In this article you learned how to use the CLI to submit Spark applications compiled as Jar files. If you are using the CDE CLI you might also find the following articles and demos interesting:

* [Installing the CDE CLI](https://docs.cloudera.com/data-engineering/cloud/cli-access/topics/cde-cli.html)
* [Simple Introduction to the CDE CLI](https://github.com/pdefusco/CDE_CLI_Simple)
* [CDE CLI Demo](https://github.com/pdefusco/CDE_CLI_demo)
* [CDE Concepts](https://docs.cloudera.com/data-engineering/cloud/cli-access/topics/cde-cli-concepts.html)
* [CDE CLI Command Reference](https://docs.cloudera.com/data-engineering/cloud/cli-access/topics/cde-cli-reference.html)
* [CDE CLI Spark Flag Reference](https://docs.cloudera.com/data-engineering/cloud/cli-access/topics/cde-cli-spark-flag-reference.html)
* [CDE CLI Airflow Flag Reference](https://docs.cloudera.com/data-engineering/cloud/cli-access/topics/cde-cli-airflow-flag-reference.html)
* [CDE CLI list command syntax reference](https://docs.cloudera.com/data-engineering/cloud/cli-access/topics/cde-cli-list-flag-reference.html)
* [CDE Jobs API Reference](https://docs.cloudera.com/data-engineering/cloud/jobs-rest-api-reference/index.html)
* [CDE Spark Migration Tool](https://docs.cloudera.com/data-engineering/1.5.0/cde-spark-submit-migration/topics/cde-using-cde-env-tool-migration.html)
* [CDE Spark Migration Tool Example](https://github.com/SuperEllipse/cde-spark-submit-migration)
* [Spark Scala Version Compatibility Matrix](Spark Scala Version Compatibility Matrix)
* [Monitoring CDE with the CDE CLI](https://community.cloudera.com/t5/Community-Articles/Efficiently-Monitoring-Jobs-Runs-and-Resources-with-the-CDE/ta-p/379893)
* [Creating a Spark Application with Code Located in S3](https://community.cloudera.com/t5/Community-Articles/Creating-a-CDE-Job-with-Spark-Application-Code-located-in-S3/ta-p/404160)
