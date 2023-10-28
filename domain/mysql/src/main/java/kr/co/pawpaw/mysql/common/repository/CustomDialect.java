package kr.co.pawpaw.mysql.common.repository;

import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class CustomDialect extends MySQL8Dialect {
    public CustomDialect() {
        super();

        registerFunction("match",
            new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "match(?1) against (?2 in boolean mode)"));
    }
}
