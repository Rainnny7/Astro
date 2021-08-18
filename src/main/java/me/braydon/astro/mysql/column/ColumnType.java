package me.braydon.astro.mysql.column;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Date;
import java.util.Set;

/**
 * This class represents a column type in MySQL.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter
public enum ColumnType {
    CHAR(char.class, 0, 255),
    VARCHAR(String.class, 0, 255),
    TINYTEXT(String.class, 0, 255),
    TEXT(String.class, 0, 65535),
    BLOB(String.class, 0, 65535),
    MEDIUMTEXT(String.class, 0, 16777215),
    MEDIUMBLOB(String.class, 0, 16777215),
    LONGTEXT(String.class, 0, 4294967295L),
    LONGBLOB(String.class, 0, 4294967295L),
    TINYINT(int.class, -128, 127),
    SMALLINT(int.class, -32768, 32767),
    MEDIUMINT(int.class, -8388608, 8388607),
    INT(int.class, Integer.MIN_VALUE, Integer.MAX_VALUE),
    BIGINT(long.class,-9223372036854775808L, 9223372036854775807L),
    FLOAT(float.class, -1, -1), // Determined by the SQL server
    DOUBLE(double.class, -1, -1), // Determined by the SQL server
    DECIMAL(double.class, Double.MIN_VALUE, Double.MAX_VALUE), // Determined by the SQL server
    DATE(Date.class, -1, -1), // Date has no length
    DATETIME(Date.class, -1, -1), // Datetime has no length
    TIMESTAMP(long.class, -1, -1), // Timestamp has no length
    ENUM(Enum.class, VARCHAR.minLength, VARCHAR.maxLength),
    SET(Set.class, VARCHAR.minLength, VARCHAR.maxLength),
    BOOLEAN(boolean.class, 1, 1);

    private final Class<?> type;
    private final double minLength, maxLength;
}