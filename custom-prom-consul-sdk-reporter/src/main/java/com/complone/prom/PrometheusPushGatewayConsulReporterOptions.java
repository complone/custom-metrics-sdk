package com.complone.prom;

import org.apache.flink.annotation.docs.Documentation;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;
import org.apache.flink.configuration.description.Description;
import org.apache.flink.configuration.description.LinkElement;
import org.apache.flink.configuration.description.TextElement;
@Documentation.SuffixOption
public class PrometheusPushGatewayConsulReporterOptions {
    public static final ConfigOption<String> HOST = ConfigOptions
            .key("host")
            .noDefaultValue()
            .withDescription("The PushGateway server host.");

    public static final ConfigOption<Integer> PORT = ConfigOptions
            .key("port")
            .defaultValue(-1)
            .withDescription("The PushGateway server port.");

    public static final ConfigOption<String> JOB_NAME = ConfigOptions
            .key("jobName")
            .defaultValue("")
            .withDescription("The job name under which metrics will be pushed");

    public static final ConfigOption<Boolean> RANDOM_JOB_NAME_SUFFIX = ConfigOptions
            .key("randomJobNameSuffix")
            .defaultValue(true)
            .withDescription("Specifies whether a random suffix should be appended to the job name.");

    public static final ConfigOption<Boolean> DELETE_ON_SHUTDOWN = ConfigOptions
            .key("deleteOnShutdown")
            .defaultValue(true)
            .withDescription("Specifies whether to delete metrics from the PushGateway on shutdown.");

    public static final ConfigOption<Boolean> FILTER_LABEL_VALUE_CHARACTER = ConfigOptions
            .key("filterLabelValueCharacters")
            .defaultValue(true)
            .withDescription(Description.builder()
                    .text("Specifies whether to filter label value characters." +
                            " If enabled, all characters not matching [a-zA-Z0-9:_] will be removed," +
                            " otherwise no characters will be removed." +
                            " Before disabling this option please ensure that your" +
                            " label values meet the %s.", LinkElement.link("https://prometheus.io/docs/concepts/data_model/#metric-names-and-labels", "Prometheus requirements"))
                    .build());

    public static final ConfigOption<String> FILTER_LABEL_KEY = ConfigOptions
            .key("filterLabelKey")
            .defaultValue("")
            .withDescription("Specifies all the labels that should be removed.Separate by comma.");


    public static final ConfigOption<String> GROUPING_KEY = ConfigOptions
            .key("groupingKey")
            .defaultValue("")
            .withDescription(Description.builder()
                    .text("Specifies the grouping key which is the group and global labels of all metrics." +
                                    " The label name and value are separated by '=', and labels are separated by ';', e.g., %s." +
                                    " Please ensure that your grouping key meets the %s.",
                            TextElement.code("k1=v1;k2=v2"),
                            LinkElement.link("https://prometheus.io/docs/concepts/data_model/#metric-names-and-labels", "Prometheus requirements"))
                    .build());
}
