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
package org.apache.camel.loanbroker;

import org.apache.camel.spring.Main;
import org.apache.camel.CamelContext;

import org.apache.camel.opentracing.OpenTracingTracer;

import com.uber.jaeger.Tracer;
import com.uber.jaeger.metrics.Metrics;
import com.uber.jaeger.metrics.NullStatsReporter;
import com.uber.jaeger.reporters.RemoteReporter;
import com.uber.jaeger.reporters.Reporter;
import com.uber.jaeger.samplers.ConstSampler;
import com.uber.jaeger.samplers.Sampler;
import com.uber.jaeger.senders.Sender;
import com.uber.jaeger.senders.UDPSender;

/**
 * Main class to start the loan broker server
 */
public final class LoanBroker {

    private LoanBroker() {
    }

    // START SNIPPET: starting
    public static void main(String... args) throws Exception {

        Main main = new Main() {
		protected void postProcessCamelContext(CamelContext camelContext)
                                throws Exception {
			System.out.println("CAMEL CONTEXT = "+camelContext);
			System.out.println("GPB: REGISTER TRACER....");
			OpenTracingTracer ottracer = new OpenTracingTracer();
			ottracer.setTracer(initTracer());
			ottracer.init(camelContext);
		}
	};
        // configure the location of the Spring XML file

        main.setApplicationContextUri("META-INF/spring/server.xml");
        main.addRouteBuilder(new LoanBrokerRoute());

        main.run();
    }
    // END SNIPPET: starting

    public static io.opentracing.Tracer initTracer() {
        Sampler sampler = new ConstSampler(true);
        Sender sender = new UDPSender(null, 0, 0);
        Reporter reporter = new RemoteReporter(sender, 500, 1000, Metrics.fromStatsReporter(new NullStatsReporter()));
        Tracer tracer = new Tracer.Builder("client", reporter, sampler).build();
        return tracer;
    }

}
