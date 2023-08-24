package kr.co.pawpaw.domainrdb.term.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pawpaw.domainrdb.term.domain.QTerm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TermCustomRepository {
    private final JPAQueryFactory queryFactory;
    private static final QTerm qTerm = QTerm.term;

    public List<Long> findIdByOrderNotNullAndRequiredIsTrue() {
        return queryFactory.select(qTerm.order)
            .from(qTerm)
            .where(qTerm.order.isNotNull().and(qTerm.required.isTrue()))
            .fetch();
    }
}
