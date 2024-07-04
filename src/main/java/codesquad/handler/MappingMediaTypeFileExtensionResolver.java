package codesquad.handler;

import static codesquad.http.SupportFileExtension.CSS;
import static codesquad.http.SupportFileExtension.HTML;
import static codesquad.http.SupportFileExtension.ICO;
import static codesquad.http.SupportFileExtension.JPEG;
import static codesquad.http.SupportFileExtension.PNG;
import static codesquad.http.SupportFileExtension.SVG;

import codesquad.http.HttpMediaType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MappingMediaTypeFileExtensionResolver {

    private final Map<String, HttpMediaType> mediaTypes = new ConcurrentHashMap<>();

    public MappingMediaTypeFileExtensionResolver() {
        mediaTypes.put(HTML.getName(), HttpMediaType.TEXT_HTML);
        mediaTypes.put(CSS.getName(), HttpMediaType.TEXT_CSS);
        mediaTypes.put(JPEG.getName(), HttpMediaType.APPLICATION_JSON);
        mediaTypes.put(ICO.getName(), HttpMediaType.IMAGE_X_ICON);
        mediaTypes.put(PNG.getName(), HttpMediaType.IMAGE_PNG);
        mediaTypes.put(JPEG.getName(), HttpMediaType.IMAGE_JPEG);
        mediaTypes.put(SVG.getName(), HttpMediaType.IMAGE_SVG);
    }

    public HttpMediaType resolve(String extension) {
        if (mediaTypes.containsKey(extension)) {
            return mediaTypes.get(extension);
        }
        return HttpMediaType.APPLICATION_OCTET_STREAM;
    }

}
