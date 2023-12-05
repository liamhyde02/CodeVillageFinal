package org.codevillage;
import org.json.JSONArray;

public interface JavaParser {
    JSONArray parse(String url, boolean isLambdaEnvironment);
}
