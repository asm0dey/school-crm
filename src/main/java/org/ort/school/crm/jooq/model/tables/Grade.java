/*
 * This file is generated by jOOQ.
*/
package org.ort.school.crm.jooq.model.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;
import org.ort.school.crm.jooq.model.Indexes;
import org.ort.school.crm.jooq.model.Keys;
import org.ort.school.crm.jooq.model.Public;
import org.ort.school.crm.jooq.model.tables.records.GradeRecord;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.0"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Grade extends TableImpl<GradeRecord> {

    private static final long serialVersionUID = -2138866535;

    /**
     * The reference instance of <code>PUBLIC.GRADE</code>
     */
    public static final Grade GRADE = new Grade();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<GradeRecord> getRecordType() {
        return GradeRecord.class;
    }

    /**
     * The column <code>PUBLIC.GRADE.ID</code>.
     */
    public final TableField<GradeRecord, Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>PUBLIC.GRADE.GRADE_NO</code>.
     */
    public final TableField<GradeRecord, Integer> GRADE_NO = createField("GRADE_NO", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>PUBLIC.GRADE.GRADE_LETTER</code>.
     */
    public final TableField<GradeRecord, String> GRADE_LETTER = createField("GRADE_LETTER", org.jooq.impl.SQLDataType.VARCHAR(1).nullable(false), this, "");

    /**
     * Create a <code>PUBLIC.GRADE</code> table reference
     */
    public Grade() {
        this(DSL.name("GRADE"), null);
    }

    /**
     * Create an aliased <code>PUBLIC.GRADE</code> table reference
     */
    public Grade(String alias) {
        this(DSL.name(alias), GRADE);
    }

    /**
     * Create an aliased <code>PUBLIC.GRADE</code> table reference
     */
    public Grade(Name alias) {
        this(alias, GRADE);
    }

    private Grade(Name alias, Table<GradeRecord> aliased) {
        this(alias, aliased, null);
    }

    private Grade(Name alias, Table<GradeRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.CONSTRAINT_INDEX_4, Indexes.PRIMARY_KEY_4);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<GradeRecord, Integer> getIdentity() {
        return Keys.IDENTITY_GRADE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<GradeRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_4;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<GradeRecord>> getKeys() {
        return Arrays.<UniqueKey<GradeRecord>>asList(Keys.CONSTRAINT_4, Keys.CONSTRAINT_40);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Grade as(String alias) {
        return new Grade(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Grade as(Name alias) {
        return new Grade(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Grade rename(String name) {
        return new Grade(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Grade rename(Name name) {
        return new Grade(name, null);
    }
}
