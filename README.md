Introduce

**custom-prom-consul-sdk-reporter** The main function is to automatically report the metric exporter to the consul service after the Flink task is started, avoiding the use of pushgateway single point service.
**custom-prometheus-sdk-java** mainly provides java version of prometheus sdk, which can support Flink metirc and prometheus metric (native). Currently supports qps, bps, latency related indicators.


How to use Flink prometheus_consul_reporter jar
---
1. Create the metrics-promconsul directory under the /usr/local/flink-1.11.2/plugins directory of the Flink client (put the Flink version 1.9 jar package under lib)

2. Put the custom-prom-consul-sdk-reporter-1.0-shaded.jar package under the metrics-promconsul directory

3. Modify the flink-conf.yaml file:
```
      Increase:

               metrics.reporter.prom.class: com.complone.prom.PrometheusConsulReporter
               metrics.reporter.prom.serviceName: flink #Override in the task submission command

               metrics.reporter.prom.consulToken: xxx # token requires application

               metrics.reporter.prom.filterLabelKey: host,instance,job_id,operator_id,task_attempt_id,task_attempt_num,task_id,tm_id,instance,task_name,operator_name

      And comment out other monitoring-related configurations
```
4. Submit the task command demo:
```
bin/flink run -m yarn-cluster -yqu bigdata \
-yD metrics.reporter.prom.serviceName=deptA_wordcount_hello \
-yD metrics.reporter.prom.consulToken=xxxxx \
-c org.apache.flink.streaming.examples.socket.SocketWindowWordCount \
examples/streaming/SocketWindowWordCount.jar --hostname 172.28.16.32 --port 9000

Among them: yD metrics.reporter.prom.jobname=wordcount_hello is the parameter to modify the job name

-yD metrics.reporter.prom.consulToken=xxxxx Replace with your own token
```

How to use Flink prometheus-sdk-java
---
1. Currently encapsulated for the metric of the Flink environment.
The prometheus-sdk-java lib package encapsulates the usage of metrics and supports custom metrics.

Support qps, bps, latency, and its metric suffixes are: numOfRecords, numOfBytes, latency_ms

2. Add the corresponding warehouse and dependencies to the pom.xml file:
```
<repositories>
    <repository>
        <id>releases</id>
        <url>xxxxx</url>
    </repository>
</repositories>
 
 
<dependencies>
    <dependency>
        <groupId>com.complone.monitor</groupId>
        <artifactId>custom-prometheus-sdk-java</artifactId>
        <version>1.0.1</version>
    </dependency>
</dependencies>
```
3. Call

1) How to use Flink Metric:
```
Demo:
DataStream<WordWithCount> windowCounts = text
    .flatMap(new RichFlatMapFunction<String, WordWithCount>() {
                    Qps qps;
                    Latency latency;
                    @Override
                    public void open(Configuration parameters) throws Exception {
                    // Initialize Flink MetricGroup;
                        FlinkCollectors flinkCollectors = new FlinkCollectors(getRuntimeContext().getMetricGroup(),"user_defined_scope");
                        // Create qps metric
                        qps = Qps.init(flinkCollectors);
                        // Create latency metric
                        latency = Latency.init(flinkCollectors,500);
                        // Create bps metric
                        bps = Bps.init(flinkCollectors);
            
                    }

                    @Override
                    public void flatMap(String value, Collector<WordWithCount> out) {
                         qps.inc(1); // count
                        long delay = (long)(Math.random()*(5000-100)+100);
                         latency.latency(delay); // delay time statistics
                        }
                })
```

4. Use Prom sql to calculate the corresponding index:
```
1) qps: sum(rate(xxx_numOfRecords{job_name="xxx"}[1m]))

2) bps: sum(rate(xxx_numOfBytes{job_name="xxx"}[1m]))

3) latency: max(xxx_latency_ms{job_name=xxx}) by(quantile)
``` 