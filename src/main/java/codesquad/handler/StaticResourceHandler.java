package codesquad.handler;

import static codesquad.http.HttpStatus.NOT_FOUND;
import static codesquad.http.HttpStatus.OK;

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
    public void service(HttpRequest request, HttpResponse response) {
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

            response.setHttpResponseLine(new HttpResponseLine(httpVersion, OK));
            response.setContentType(httpMediaType);
            response.setContentLength(body.length);
            response.setBody(body);

        } catch (FileNotFoundException e) {

            response.setHttpResponseLine(new HttpResponseLine(httpVersion, NOT_FOUND));
            response.setBody(NOT_FOUND.getRepresentation().getBytes());

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
