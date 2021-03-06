package ru.sbtqa.tag.api;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import org.aeonbits.owner.ConfigFactory;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.environment.ApiEnvironment;
import ru.sbtqa.tag.api.properties.ApiConfiguration;
import ru.sbtqa.tag.api.repository.ApiPair;
import ru.sbtqa.tag.api.storage.BlankStorage;
import ru.sbtqa.tag.api.utils.PathUtils;
import ru.sbtqa.tag.api.utils.PlaceholderUtils;
import ru.sbtqa.tag.api.utils.TemplateUtils;

import static io.restassured.RestAssured.given;
import static ru.sbtqa.tag.api.annotation.ParameterType.*;

/**
 * An endpoint request (ala Page Object).
 * <p>
 * It symbolizes the request for an api endpoint. You need to extends your entries from this class
 * and annotate with {@link Endpoint}
 */
public abstract class EndpointEntry {

    private static final ApiConfiguration PROPERTIES = ConfigFactory.create(ApiConfiguration.class);

    private EndpointEntryReflection reflection;
    private BlankStorage blankStorage;
    private Rest method;
    private String path;
    private String template;
    private String title;

    public EndpointEntry() {
        Endpoint endpoint = this.getClass().getAnnotation(Endpoint.class);
        method = endpoint.method();
        path = endpoint.path();
        template = endpoint.template();
        title = endpoint.title();

        reflection = new EndpointEntryReflection(this);
        blankStorage = ApiEnvironment.getBlankStorage();
    }

    public void send(Map<String, String> data) {
        for (Map.Entry<String, String> dataTableRow : data.entrySet()) {
            reflection.setParameterValueByTitle(dataTableRow.getKey(), dataTableRow.getValue());
        }

        send();
    }

    public void send() {
        reflection.applyAnnotations();
        String url = PathUtils.unite( PROPERTIES.getBaseURI(), path);

        RequestSpecification request = buildRequest();
        Response response;
        switch (method) {
            case GET:
                response = request.get(url);
                break;
            case POST:
                response = request.post(url);
                break;
            case PUT:
                response = request.put(url);
                break;
            case PATCH:
                response = request.patch(url);
                break;
            case DELETE:
                response = request.delete(url);
                break;
            case OPTIONS:
                response = request.options(url);
                break;
            case HEAD:
                response = request.head(url);
                break;
            default:
                throw new UnsupportedOperationException("Request method " + method + " is not supported");
        }

        ApiEnvironment.getRepository().add(this.getClass(), new ApiPair(request, response.then().log().all()));
    }

    private RequestSpecification buildRequest() {
        RequestSpecification request = given().log().all();

        request.queryParams(getQueryParameters());
        request.headers(getHeaders());

        if (!Rest.isBodiless(method)) {
            request.body(getBody());
        }
        
        return request;
    }

    private Map<String, ?> getQueryParameters() {
        Map<String, Object> queries = new HashMap<>();

        queries.putAll(reflection.getParameters(QUERY));
        queries.putAll(blankStorage.get(title).getQueries());

        return queries;
    }

    private Map<String, ?> getHeaders() {
        Map<String, Object> headers = new HashMap<>();

        headers.putAll(reflection.getParameters(HEADER));
        headers.putAll(blankStorage.get(title).getHeaders());

        return headers;
    }

    private String getBody() {
        String body = TemplateUtils.loadFromResources(this.getClass(), template, PROPERTIES.getTemplateEncoding());

        Map<String, Object> parameters = new HashMap<>();
        parameters.putAll(reflection.getParameters(BODY));
        parameters.putAll(blankStorage.get(title).getBodies());

        return PlaceholderUtils.replacePlaceholders(body, parameters);
    }

    public void validate(String title, Object... params) {
        reflection.validate(title, params);
    }

    public void validate(Object... params) {
        reflection.validate(params);
    }

    public ValidatableResponse getResponse() {
        return ApiEnvironment.getRepository().get(this.getClass()).getResponse();
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
