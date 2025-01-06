package info.supercode.superchat.functons;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class MockAzureAppRegistration implements Function<MockAzureAppRegistration.Request, MockAzureAppRegistration.Response> {

	private static final Logger log = LoggerFactory.getLogger(MockAzureAppRegistration.class);

	@JsonClassDescription("Create an Azure Entra ID application")
	public record Request(@JsonProperty(required = true) String displayName,
						  @JsonProperty(required = true) String signInAudience) {}
	public record Response(String appId) {}

	public Response apply(Request request) {
		log.info("Azure app Request: {}", request);
		return new Response("APP123");
	}
}