package com.elastic.search.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
// Maps this class to an Elasticsearch index called "movies"
@Document(indexName = "movies")
public class Movie {

    @Id
    private String id; // Maps to the Elasticsearch _id field

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Keyword)
    private List<String> genre;

    @Field(type = FieldType.Integer)
    private int releaseYear;

    @Field(type = FieldType.Text)
    private String description;

}