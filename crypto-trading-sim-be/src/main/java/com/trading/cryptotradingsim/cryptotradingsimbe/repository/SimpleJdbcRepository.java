package com.trading.cryptotradingsim.cryptotradingsimbe.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.trading.cryptotradingsim.cryptotradingsimbe.util.RepositoryUtil.*;
import static org.apache.commons.beanutils.BeanUtils.setProperty;

@Slf4j
public class SimpleJdbcRepository<T, ID> implements SimpleRepository<T, ID> {
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
    public T save(T entity) {
        Map<String, Object> columns = getParameterValues(new BeanPropertySqlParameterSource(entity), idColumn);
        
        log.error(Arrays.toString(columns.keySet().toArray(new String[0])));
        String insertSql = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(tableName)
                .usingColumns(columns.keySet().toArray(new String[0]))
                .getInsertString();
        log.info("Insert SQL: {}", insertSql);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(insertSql, new MapSqlParameterSource(columns), keyHolder);

        if (keyHolder.getKey() != null) {
            try {
                setProperty(entity, idColumn, keyHolder.getKey());
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(String.format("Failed to set ID property: %s", idColumn), e);
            }
        }
        return entity;
    }

    @Override
    public T update(T entity) {
        //TODO
        return entity;
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
}
