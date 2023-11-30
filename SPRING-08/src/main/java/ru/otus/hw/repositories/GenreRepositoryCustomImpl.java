package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.hw.models.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@RequiredArgsConstructor
public class GenreRepositoryCustomImpl implements  GenreRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Genre> findGenresByNames(List<String> genreNames) {
        Aggregation agg = Aggregation.newAggregation(
                match(Criteria.where("name").in(genreNames))
                , project().and("name").as("name")
        );
        AggregationResults<Genre> outputType = mongoTemplate.aggregate(agg,"genres", Genre.class);
        return outputType.getMappedResults();
    }

    private Genre findGenreByName(String name) {
        Aggregation agg = Aggregation.newAggregation(
                match(Criteria.where("name").is(name))
                , project().and("name").as("name")
        );
        AggregationResults<Genre> outputType = mongoTemplate.aggregate(agg,"genres", Genre.class);
        return outputType.getUniqueMappedResult();
    }

}
