package ar.edu.itba.paw.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

class Helpers {
    public static Optional<Long> getOptionalLong(ResultSet rs, String column) throws SQLException {
        long value = rs.getLong(column);
        return rs.wasNull() ? Optional.empty() : Optional.of(value);
    }

    public static Optional<Integer> getOptionalInt(ResultSet rs, String column) throws SQLException {
        int value = rs.getInt(column);
        return rs.wasNull() ? Optional.empty() : Optional.of(value);
    }

    public static String sqlPlaceholders(int size) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < size - 1; i++) {
            sb.append("?,");
        }
        sb.append("?");

        return sb.toString();
    }
}
