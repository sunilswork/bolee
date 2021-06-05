package org.sunprojects.server;

import io.javalin.Javalin;
import org.sunprojects.views.Index;

public class Server {
    public static void main(String[] args) {
        var app = Javalin.create(
                javalinConfig -> {
                    javalinConfig.defaultContentType = "text/html; charset=UTF-8";
                }
        ).start(8082);
        var indexPage = new Index().getIndexPage();
        app.get("/", ctx -> ctx.result(indexPage));
    }
}
