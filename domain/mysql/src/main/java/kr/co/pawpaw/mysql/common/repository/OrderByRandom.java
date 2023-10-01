package kr.co.pawpaw.mysql.common.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;

public class OrderByRandom extends OrderSpecifier<Double> {
    public static final OrderByRandom DEFAULT = new OrderByRandom();

    private OrderByRandom() {
        super(Order.DESC, Expressions.numberTemplate(Double.class, "rand()"));
    }
}