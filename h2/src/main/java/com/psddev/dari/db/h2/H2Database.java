package com.psddev.dari.db.h2;

import com.psddev.dari.db.SqlVendor;
import com.psddev.dari.db.sql.AbstractSqlDatabase;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.SQLDialect;
import org.jooq.impl.SQLDataType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class H2Database extends AbstractSqlDatabase {

    private static final DataType<String> STRING_INDEX_TYPE = SQLDataType.VARCHAR.asConvertedDataType(new Converter<String, String>() {

        @Override
        public String from(String string) {
            return string;
        }

        @Override
        public String to(String string) {
            return string != null && string.length() > MAX_STRING_INDEX_TYPE_LENGTH
                    ? string.substring(0, MAX_STRING_INDEX_TYPE_LENGTH)
                    : string;
        }

        @Override
        public Class<String> fromType() {
            return String.class;
        }

        @Override
        public Class<String> toType() {
            return String.class;
        }
    });

    @Override
    public DataType<String> stringIndexType() {
        return STRING_INDEX_TYPE;
    }

    @Override
    public void setUp(AbstractSqlDatabase database) throws IOException, SQLException {
        Connection connection = database.openConnection();

        try {
            org.h2gis.ext.H2GISExtension.load(connection);

        } finally {
            database.closeConnection(connection);
        }

        super.setUp(database);
    }

    @Override
    protected String setUpResourcePath() {
        return "schema-12.sql";
    }

    @Override
    protected SQLDialect dialect() {
        return SQLDialect.H2;
    }

    @Override
    public SqlVendor getMetricVendor() {
        return new SqlVendor.H2();
    }
}