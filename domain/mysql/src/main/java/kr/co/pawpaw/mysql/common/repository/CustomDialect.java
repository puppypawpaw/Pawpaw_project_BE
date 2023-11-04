package kr.co.pawpaw.mysql.common.repository;

import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class CustomDialect extends MySQL8Dialect {
    public CustomDialect() {
        super();
        registerFunction("within", new SQLFunctionTemplate(
            StandardBasicTypes.DOUBLE,
            "ST_Within(ST_GeomFromText(CONCAT('POINT(', ?1, ' ', ?2, ')')), ST_GeomFromText(CONCAT('POLYGON ((', ?3, ' ', ?4, ', ', ?5, ' ', ?6, ', ', ?7, ' ', ?8, ', ', ?9, ' ', ?10, ', ', ?11, ' ', ?12,'))')))"));
        registerFunction("match", new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "match(?1) against (?2 in boolean mode)"));
    }
}
