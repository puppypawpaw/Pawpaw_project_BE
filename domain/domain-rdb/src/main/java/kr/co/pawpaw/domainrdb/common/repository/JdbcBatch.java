package kr.co.pawpaw.domainrdb.common.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcBatch<T> {
    private final PlatformTransactionManager txManager;
    private final JdbcTemplate jdbcTemplate;
    private final int batchSize = 1000;

    public List<Long> batchInsert(
        final String sql,
        final List<T> entities,
        final AddBatchCallback<T> callback
    ) {
        List<Long> generatedIds = new ArrayList<>();
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
        try {
            int requestSize = entities.size();
            for (int i = 0; i < requestSize; i += batchSize) {
                List<T> subEntities = entities.subList(i, Math.min(i + batchSize, requestSize));
                jdbcTemplate.execute((ConnectionCallback<Object>) conn -> {
                    PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    for (T entity : subEntities) {
                        callback.addBatch(ps, entity);
                    }
                    ps.executeBatch();

                    ResultSet rs = ps.getGeneratedKeys();
                    while (rs.next()) {
                        generatedIds.add(rs.getLong(1));
                    }
                    return null;
                });
            }
            txManager.commit(status);
        } catch (Exception e) {
            txManager.rollback(status);
            throw e;
        }
        return generatedIds;
    }
}