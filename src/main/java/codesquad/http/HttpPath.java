package codesquad.http;

import java.util.Map;

public class HttpPath {

    private final String defaultPath;
    private final Map<String, String> queryString;

    private HttpPath(String defaultPath, Map<String, String> queryString) {
        this.defaultPath = defaultPath;
        this.queryString = queryString;
    }

    public static HttpPath of(String defaultPath, Map<String, String> queryString) {
        return new HttpPath(defaultPath, queryString);
    }

    public static HttpPath ofOnlyDefaultPath(String defaultPath) {
        return new HttpPath(defaultPath, Map.of());
    }

    public String getDefaultPath() {
        return defaultPath;
    }

    @Override
    public String toString() {
        return "path={defaultPath='" + defaultPath + '\'' +
                ", queryString=" + queryString + "}";
    }
}
