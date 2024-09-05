package pl.poznan.put.planner_endpoints.Demo;

import java.util.concurrent.atomic.AtomicInteger;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

/**
 * Simple Demo REST controller
 */
@RestController
public class DemoController {
    /**
     * atomic counter for sequence
     */
    private final AtomicInteger counter = new AtomicInteger(0);
    /**
     * String template for GET responses
     */
    private static final String getTemplate = "GET query text was: '%s'";
    /**
     * String template for POST responses
     */
    private static final String postTemplate = "POST query text was: '%s'";

    /**
     * Simple GET mapping, returns query text + query number
     * @param text value of 'text' query param
     * @return response (text and sequence number)
     */
    @Operation(summary = "Return query param as response + sequence number")
    @GetMapping("/Demo")
    public Demo getDemo(@RequestParam(value = "text", defaultValue = "") String text) {
        return new Demo(String.format(getTemplate, text), counter.incrementAndGet());
    }

    /**
     * Simple POST mapping, returns query text + query number
     * @param text raw value of request
     * @return response (text and sequence number)
     */
    @Operation(summary = "Return raw body as response + sequence number")
    @PostMapping("/Demo")
    public Demo postDemo(@RequestBody String text) {
        return new Demo(String.format(postTemplate, text), counter.incrementAndGet());
    }
}
