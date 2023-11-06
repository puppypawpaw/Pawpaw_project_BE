package kr.co.pawpaw.mysql.common.util;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import lombok.experimental.UtilityClass;

@UtilityClass
public class QueryUtil {
    public BooleanBuilder fullTextSearchCondition(
        final StringPath qPath,
        final String query
    ) {
        return new BooleanBuilder()
            .and(Expressions.numberTemplate(
                Double.class,
                "function('match',{0},{1})",
                qPath,
                query
            ).gt(0));
    }

    public BooleanBuilder withInPositionCondition(
        final NumberPath<Double> latitude,
        final NumberPath<Double> longitude,
        final double latitudeMin,
        final double latitudeMax,
        final double longitudeMin,
        final double longitudeMax
    ) {
        return new BooleanBuilder()
            .and(Expressions.numberTemplate(
                Double.class,
                "function('within',{0},{1},{2},{3},{4},{5},{6},{7},{8},{9},{10},{11})",
                latitude,
                longitude,
                latitudeMin,
                longitudeMin,
                latitudeMin,
                longitudeMax,
                latitudeMax,
                longitudeMax,
                latitudeMax,
                longitudeMin,
                latitudeMin,
                longitudeMin
            ).gt(0));
    }
}
