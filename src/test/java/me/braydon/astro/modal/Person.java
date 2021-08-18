package me.braydon.astro.modal;

import lombok.Getter;
import me.braydon.astro.mysql.Table;
import me.braydon.astro.mysql.column.Column;
import me.braydon.astro.mysql.column.ColumnType;

/**
 * This represents a Person in MySQL.
 *
 * @author Braydon
 */
@Table(name = "people") @Getter
public class Person extends Modal {
    @Column(type = ColumnType.INT) private long id;
    @Column(type = ColumnType.VARCHAR) private String firstName, lastName;
    @Column(type = ColumnType.INT) private int age;
}