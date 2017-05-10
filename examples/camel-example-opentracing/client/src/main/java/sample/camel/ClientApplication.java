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

import javax.enterprise.event.Observes;

import org.apache.camel.cdi.ContextName;
import org.apache.camel.management.event.CamelContextStartingEvent;
import org.apache.camel.opentracing.OpenTracingTracer;

/*
import com.uber.jaeger.Tracer;
import com.uber.jaeger.metrics.Metrics;
import com.uber.jaeger.metrics.NullStatsReporter;
import com.uber.jaeger.reporters.RemoteReporter;
import com.uber.jaeger.reporters.Reporter;
import com.uber.jaeger.samplers.ConstSampler;
import com.uber.jaeger.samplers.Sampler;
import com.uber.jaeger.senders.Sender;
import com.uber.jaeger.senders.UDPSender;
*/

import io.opentracing.contrib.tracerresolver.TracerResolver;

@ContextName("Server1")
public class ClientApplication {

    public void setupCamel(@Observes CamelContextStartingEvent event) {
        OpenTracingTracer ottracer = new OpenTracingTracer();
        ottracer.setTracer(initTracer());
        ottracer.init(event.getContext());
    }

    public static io.opentracing.Tracer initTracer() {
/*
        Sampler sampler = new ConstSampler(true);
        Sender sender = new UDPSender(null, 0, 0);
        Reporter reporter = new RemoteReporter(sender, 500, 1000, Metrics.fromStatsReporter(new NullStatsReporter()));
        Tracer tracer = new Tracer.Builder("client", reporter, sampler).build();
        return tracer;
*/
	System.setProperty("JAEGER_SERVICE_NAME", "client2");
	return TracerResolver.resolveTracer();
    }
}
