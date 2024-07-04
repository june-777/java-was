package codesquad.handler;

import static codesquad.http.HttpStatus.NOT_FOUND;
import static codesquad.http.HttpStatus.OK;

import codesquad.http.HttpHeaders;
import codesquad.http.HttpMediaType;
import codesquad.http.HttpPath;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpResponseLine;
import codesquad.http.HttpVersion;
import java.io.FileNotFoundException;
import java.io.IOException;

public class StaticResourceHandler implements Handler {

    private final StaticResourceReader staticResourceReader;
    private final MappingMediaTypeFileExtensionResolver mappingMediaTypeFileExtensionResolver;

    public StaticResourceHandler(StaticResourceReader staticResourceReader,
                                 MappingMediaTypeFileExtensionResolver mappingMediaTypeFileExtensionResolver
    ) {
        this.staticResourceReader = staticResourceReader;
        this.mappingMediaTypeFileExtensionResolver = mappingMediaTypeFileExtensionResolver;
    }

    @Override
    public HttpResponse service(HttpRequest request, HttpResponse response) {
        HttpVersion httpVersion = request.getVersion();
        HttpPath path = request.getPath();

        try {
            String pathValue = path.getDefaultPath();
            if (path.isDirectoryPath()) {
                pathValue += "/index.html";
            }
            String fileExtension = getFileExtension(pathValue);
            HttpMediaType httpMediaType = mappingMediaTypeFileExtensionResolver.resolve(fileExtension);
            byte[] body = staticResourceReader.getFileContents(pathValue);

            HttpResponseLine httpResponseLine = new HttpResponseLine(httpVersion, OK);
            HttpHeaders httpHeaders = HttpHeaders.empty();
            httpHeaders.setContentType(httpMediaType);
            httpHeaders.setContentLength(body.length);
            
            return new HttpResponse(httpResponseLine, httpHeaders, body);
        } catch (FileNotFoundException e) {
            HttpResponseLine httpResponseLine = new HttpResponseLine(httpVersion, NOT_FOUND);
            HttpHeaders httpHeaders = HttpHeaders.empty();
            return new HttpResponse(httpResponseLine, httpHeaders, NOT_FOUND.getRepresentation().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFileExtension(String path) {
        int lastIndexOfDot = path.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return "";
        }
        return path.substring(lastIndexOfDot + 1);
    }

}
