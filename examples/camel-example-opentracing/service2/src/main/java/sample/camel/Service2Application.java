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

import org.apache.camel.main.Main;
import brave.opentracing.BraveTracer;
import io.opentracing.Tracer;
import zipkin.Span;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.Reporter;
import zipkin.reporter.urlconnection.URLConnectionSender;

public final class Service2Application {

    private Service2Application() {
      // noop
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.bind("tracer", initTracer());
        main.addRouteBuilder(new Service2Route());
        main.run();
    }

    public static Tracer initTracer() {
        System.out.println("Using Zipkin Tracer");
        String zipkinServerUrl = String.format("%s/api/v1/spans", System.getenv("ZIPKIN_SERVER_URL"));
        Reporter<Span> reporter = AsyncReporter.builder(URLConnectionSender.create(zipkinServerUrl)).build();
        brave.Tracer tracer = brave.Tracer.newBuilder().localServiceName("service2").reporter(reporter).build();
        return BraveTracer.wrap(tracer);
    }
}
