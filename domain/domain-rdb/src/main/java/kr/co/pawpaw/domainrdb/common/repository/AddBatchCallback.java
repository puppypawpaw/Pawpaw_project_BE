package kr.co.pawpaw.domainrdb.common.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface AddBatchCallback<T> {
    void addBatch(final PreparedStatement ps, final T entity) throws SQLException;
}
