package com.trading.cryptotradingsim.cryptotradingsimbe.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

import static com.trading.cryptotradingsim.cryptotradingsimbe.util.RepositoryUtil.*;

@Slf4j
public abstract class SimpleJdbcRepository<T, ID> implements SimpleRepository<T, ID> {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<T> rowMapper;
    private final String tableName;
    private final String idColumn;
    private final Class<T> entityType;
    private final Class<ID> idType;

    public SimpleJdbcRepository(JdbcTemplate jdbcTemplate,
                                String tableName,
                                String idColumn,
                                RowMapper<T> rowMapper,
                                Class<T> entityType,
                                Class<ID> idType) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableName = tableName;
        this.rowMapper = rowMapper;
        this.idColumn = idColumn;
        this.entityType = entityType;
        this.idType = idType;
    }

    @Override
    public Optional<T> getById(ID id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(getByColumnSql(tableName, idColumn), rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<T> getAll() {
        return jdbcTemplate.query(getAllSql(tableName), rowMapper);
    }

    @Override
    public void deleteById(ID id) {
        jdbcTemplate.update(deleteByColumnSql(tableName, idColumn), id);
    }

    protected JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    protected RowMapper<T> getRowMapper() {
        return rowMapper;
    }

    protected String getTableName() {
        return tableName;
    }

    protected String getIdColumn() {
        return idColumn;
    }

    protected Class<T> getEntityType() {
        return entityType;
    }

    protected Class<ID> getIdType() {
        return idType;
    }
}
