package com.test.post;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.http.NameValuePair;


public class People {

    private String name;


    @JsonCreator
    public People(@JsonProperty("name") String name) {
        name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name;
    }

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                '}';
    }
}
