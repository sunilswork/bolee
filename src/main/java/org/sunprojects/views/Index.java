package org.sunprojects.views;

import j2html.tags.*;

import static j2html.TagCreator.h1;
import static j2html.TagCreator.form;

public class Index {
    public String getIndexPage() {
        //StringBuilder stringBuilder = new StringBuilder();

                return MainView.render(
                "Signup page",
                h1("Please sign up"),
                form().withMethod("post").with(
                        MainView.emailInput("Email address"),
                        MainView.choosePasswordInput("Choose Password"),
                        MainView.repeatPasswordInput("Repeat Password"),
                        MainView.submitButton("Sign up")
                )
        );

        //return stringBuilder.toString();
    }
}
