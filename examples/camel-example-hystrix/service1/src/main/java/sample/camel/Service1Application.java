/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sample.camel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.camel.opentracing.starter.CamelOpenTracing;
import org.springframework.context.annotation.Bean;

import com.uber.jaeger.Tracer;
import com.uber.jaeger.metrics.Metrics;
import com.uber.jaeger.metrics.NullStatsReporter;
import com.uber.jaeger.reporters.RemoteReporter;
import com.uber.jaeger.reporters.Reporter;
import com.uber.jaeger.samplers.ConstSampler;
import com.uber.jaeger.samplers.Sampler;
import com.uber.jaeger.senders.Sender;
import com.uber.jaeger.senders.UDPSender;

//CHECKSTYLE:OFF
/**
 * A Spring Boot application that runs Camel service 1
 */
@SpringBootApplication
@CamelOpenTracing
public class Service1Application {

    /**
     * A main method to start this application.
     */
    public static void main(String[] args) {
        SpringApplication.run(Service1Application.class, args);
    }

    @Bean
    public static io.opentracing.Tracer initTracer() {
        Sampler sampler = new ConstSampler(true);
        Sender sender = new UDPSender(null, 0, 0);
        Reporter reporter = new RemoteReporter(sender, 500, 1000, Metrics.fromStatsReporter(new NullStatsReporter()));
        Tracer tracer = new Tracer.Builder("service1", reporter, sampler).build();
        return tracer;
    }
}
//CHECKSTYLE:ON
