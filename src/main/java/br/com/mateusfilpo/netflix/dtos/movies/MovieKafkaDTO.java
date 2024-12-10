package br.com.mateusfilpo.netflix.dtos.movies;

import java.io.Serializable;

public class MovieKafkaDTO implements Serializable {

    public String title;
    public String description;

    public MovieKafkaDTO() {
    }

    public MovieKafkaDTO(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "MovieKafkaDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
