package org.dromara.compliance.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * PostgreSQL TEXT[] 数组类型处理器
 * 用于处理 compliance_rule 表的 keywords 字段
 *
 * @author compliance
 */
@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.ARRAY)
public class ComplianceStringArrayTypeHandler extends BaseTypeHandler<List<String>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null || parameter.isEmpty()) {
            ps.setNull(i, Types.ARRAY);
        } else {
            // 创建PostgreSQL数组
            Connection conn = ps.getConnection();
            Array array = conn.createArrayOf("text", parameter.toArray());
            ps.setArray(i, array);
        }
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return toArray(rs.getArray(columnName));
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return toArray(rs.getArray(columnIndex));
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toArray(cs.getArray(columnIndex));
    }

    /**
     * 将PostgreSQL Array转换为List<String>
     */
    private List<String> toArray(Array array) throws SQLException {
        if (array == null) {
            return null;
        }
        Object arrayObj = array.getArray();
        if (arrayObj == null) {
            return null;
        }
        // 处理PostgreSQL数组
        if (arrayObj instanceof String[]) {
            return Arrays.asList((String[]) arrayObj);
        } else if (arrayObj instanceof Object[]) {
            Object[] objArray = (Object[]) arrayObj;
            List<String> result = new ArrayList<>(objArray.length);
            for (Object obj : objArray) {
                if (obj != null) {
                    result.add(obj.toString());
                }
            }
            return result;
        }
        return null;
    }
}
