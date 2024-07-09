package codesquad.webserver;

import codesquad.servlet.HandlerMapper;
import codesquad.servlet.MappingMediaTypeFileExtensionResolver;
import codesquad.servlet.StaticResourceReader;
import codesquad.servlet.handler.HttpRequestHandler;
import codesquad.servlet.handler.StaticResourceHandler;
import codesquad.utils.time.CurrentZonedDateTimeGenerator;
import codesquad.utils.time.ZonedDateTimeGenerator;
import codesquad.webserver.http.HttpRequestMapper;
import codesquad.webserver.http.HttpRequestParser;

public class Container {

    public Connector connector() {
        return new Connector(httpProcessor());
    }

    private HttpProcessor httpProcessor() {
        return new HttpProcessor(httpRequestMapper(), httpRequestHandler());
    }

    private HttpRequestMapper httpRequestMapper() {
        return new HttpRequestMapper(httpRequestParser());
    }

    private HttpRequestParser httpRequestParser() {
        return new HttpRequestParser();
    }

    private HttpRequestHandler httpRequestHandler() {
        return new HttpRequestHandler(handlerMapper(), staticResourceHandler(), zonedDateTimeGenerator());
    }

    private HandlerMapper handlerMapper() {
        return new HandlerMapper();
    }

    private StaticResourceHandler staticResourceHandler() {
        return new StaticResourceHandler(staticResourceReader(), mappingMediaTypeFileExtensionResolver());
    }

    private StaticResourceReader staticResourceReader() {
        return new StaticResourceReader();
    }

    private MappingMediaTypeFileExtensionResolver mappingMediaTypeFileExtensionResolver() {
        return new MappingMediaTypeFileExtensionResolver();
    }

    private ZonedDateTimeGenerator zonedDateTimeGenerator() {
        return new CurrentZonedDateTimeGenerator();
    }

}
