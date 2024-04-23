package org.example;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;
import io.micrometer.core.instrument.MeterRegistry;

import io.micrometer.core.instrument.Timer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    public final MeterRegistry registry;
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private final JmsProducer jmsProducer;

    public GreetingController(MeterRegistry registry, JmsProducer jmsProducer) {
        this.registry = registry;
        this.jmsProducer = jmsProducer;
        final Timer timer = Timer
                .builder("test.timer")
                .publishPercentiles(0.3, 0.5, 0.95)
                //.publishPercentileHistogram()
                .serviceLevelObjectives(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(750), Duration.ofMillis(800))
                .minimumExpectedValue(Duration.ofMillis(1))
                .maximumExpectedValue(Duration.ofSeconds(10))
                .register(this.registry);
    }

    @Counter(classname = "greetings.total")
    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        Timer.Sample sample = Timer.start(this.registry);
        //registry.counter("greetings.total", "name", name).increment();
        sample.stop(registry.timer("test.timer"));
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @Counter(classname = "postmessage.total")
    @GetMapping("/postmessage")
    public void postmessage(@RequestParam(value = "name", defaultValue = "World") String name) {
        Timer.Sample sample = Timer.start(this.registry);
        //registry.counter("greetings.total", "name", name).increment();
        sample.stop(registry.timer("test.timer"));
        jmsProducer.sendMessage(name);

    }
}
