package info.supercode.superchat.functons;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class MockWeatherService implements Function<MockWeatherService.Request, MockWeatherService.Response> {

	private static final Logger log = LoggerFactory.getLogger(MockWeatherService.class);

	public enum Unit { C, F }

	@JsonClassDescription("Get the weather in location")
	public record Request(@JsonProperty(required = true) String location, Unit unit) {}
	public record Response(double temp, Unit unit) {}

	public Response apply(Request request) {
		log.info("Weather Request: {}", request);
		return new Response(30.0, Unit.C);
	}

}
