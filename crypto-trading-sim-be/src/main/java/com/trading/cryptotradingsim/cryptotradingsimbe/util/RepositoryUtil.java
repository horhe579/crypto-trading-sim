package com.trading.cryptotradingsim.cryptotradingsimbe.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class RepositoryUtil {

    private static final String GET_BY_COLUMN_SQL = "SELECT * FROM %s WHERE %s = ?";
    private static final String DELETE_BY_COLUMN_SQL = "DELETE FROM %s WHERE %s = ?";
    private static final String GET_ALL_SQL = "SELECT * FROM %s";

    public static Map<String, Object> getParameterValues(BeanPropertySqlParameterSource source, String idColumn) {
        var variable = Arrays.stream(source.getParameterNames())
                .filter(name -> !name.equalsIgnoreCase(idColumn))
                .collect(Collectors.toMap(
                        paramName -> paramName,
                        paramName -> source.getValue(paramName)
                ));
        log.error(variable.toString());
        return variable;
    }

    public static String getByColumnSql(String tableName, String columnName) {
        return String.format(GET_BY_COLUMN_SQL, tableName, columnName);
    }

    public static String deleteByColumnSql(String tableName, String columnName) {
        return String.format(DELETE_BY_COLUMN_SQL, tableName, columnName);
    }

    public static String getAllSql(String tableName) {
        return String.format(GET_ALL_SQL, tableName);
    }
}
