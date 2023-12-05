package org.codevillage;

import org.codevillage.fetching.DataFetcher;
import org.codevillage.fetching.GithubDataFetcher;
import org.codevillage.fetching.LocalDataFetcher;
import org.codevillage.fetching.SVNDataFetcher;
import org.json.JSONArray;

import java.util.ArrayList;

public class JavaParserFunctionality implements JavaParser{
    DataFetcher dataFetcher;
    public JavaParserFunctionality(String method) {
        switch (method) {
            case "github":
                dataFetcher = new GithubDataFetcher();
                break;
            case "local":
                dataFetcher = new LocalDataFetcher();
                break;
            case "svn":
                dataFetcher = new SVNDataFetcher();
                break;
        }
    }
    public JSONArray parse(String url, boolean isLambdaEnvironment) {
        JSONArray jsonArray = new JSONArray();
        ArrayList<JavaEntity> entities = new ArrayList<>();
        SourceCodeParser sourceCodeParser = new SourceCodeParser();
        try {
            entities = sourceCodeParser.parseSourceFiles(dataFetcher.downloadPackage(url, isLambdaEnvironment));
        } catch (Exception e) {
            e.printStackTrace();
        }
        EntityCleaning entityCleaning = new EntityCleaning();
        entityCleaning.clean(entities);
        for (JavaEntity entity : entities) {
            jsonArray.put(entity.toJSON());
        }
        return jsonArray;
    }

    public static void main(String[] args) {
        JavaParserFunctionality javaParserFunctionality = new JavaParserFunctionality("github");
        JSONArray jsonArray = javaParserFunctionality.parse("https://github.com/smartyro/FinalProject308", false);
        System.out.println(jsonArray.toString());
    }
}
