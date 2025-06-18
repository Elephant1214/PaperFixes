package me.elephant1214.paperfixes.core.agent;

import java.lang.instrument.Instrumentation;

public final class AgentMain {
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
        System.setProperty("log4j2.AsyncQueueFullPolicy", "io.papermc.paper.LogFullPolicy");
    }
}
