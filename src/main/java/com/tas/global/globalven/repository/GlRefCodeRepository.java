package com.tas.global.globalven.repository;

import com.tas.global.globalven.dto.GlRefCodeDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class GlRefCodeRepository {
    
    private final JdbcTemplate sqlServerJdbcTemplate;
    private final SimpleJdbcCall simpleJdbcCall;
    
    public GlRefCodeRepository(@Qualifier("sqlServerJdbcTemplate") JdbcTemplate sqlServerJdbcTemplate,
                              @Qualifier("sqlServerDataSource") DataSource sqlServerDataSource) {
        this.sqlServerJdbcTemplate = sqlServerJdbcTemplate;
        this.simpleJdbcCall = new SimpleJdbcCall(sqlServerDataSource)
                .withProcedureName("GET_GLREF_CODES");
    }
      /**
     * Execute GET_GLREF_CODES stored procedure
     * @return List of GlRefCodeDto objects
     */    public List<GlRefCodeDto> getGlRefCodes() {
        try {
            // Execute the stored procedure
            Map<String, Object> result = simpleJdbcCall.execute();
            
            // Get the result set from the procedure
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.get("#result-set-1");
            
            // Convert to DTO objects
            return resultList.stream()
                    .map(this::mapRowToGlRefCodeDto)
                    .toList();
                    
        } catch (Exception e) {
            // Fallback to direct SQL execution if SimpleJdbcCall fails
            System.out.println("SimpleJdbcCall failed, using direct SQL: " + e.getMessage());
            return executeDirectSql();
        }
    }
    
    /**
     * Fallback method to execute stored procedure using direct SQL
     */
    private List<GlRefCodeDto> executeDirectSql() {
        String sql = "EXEC GET_GLREF_CODES";
        return sqlServerJdbcTemplate.query(sql, new GlRefCodeRowMapper());
    }
    
    /**
     * Execute stored procedure with parameters (if needed in future)
     * @param parameters Map of parameter names and values
     * @return List of GlRefCodeDto objects
     */
    public List<GlRefCodeDto> getGlRefCodesWithParams(Map<String, Object> parameters) {
        try {
            Map<String, Object> result = simpleJdbcCall.execute(parameters);
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.get("#result-set-1");
            
            return resultList.stream()
                    .map(this::mapRowToGlRefCodeDto)
                    .toList();
                    
        } catch (Exception e) {
            throw new RuntimeException("Error executing stored procedure with parameters", e);
        }
    }
      /**
     * Map a row from the result set to GlRefCodeDto
     */
    private GlRefCodeDto mapRowToGlRefCodeDto(Map<String, Object> row) {
        GlRefCodeDto dto = new GlRefCodeDto();
        
        // Map actual SQL Server column names to DTO fields
        dto.setId(null); // No ID column in stored procedure
        dto.setCode(getStringValue(row, "CHT_ACC_ALIAS"));
        dto.setDescription(getStringValue(row, "CHT_ACC_NAME"));
        dto.setCategory(null); // No category column in stored procedure
        dto.setIsActive(true); // Assume all returned records are active
        dto.setCreatedDate(null); // No created date column in stored procedure
        dto.setUpdatedDate(null); // No updated date column in stored procedure
        
        return dto;
    }
      /**
     * Row mapper for direct SQL execution
     */
    private static class GlRefCodeRowMapper implements RowMapper<GlRefCodeDto> {
        @Override
        public GlRefCodeDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            GlRefCodeDto dto = new GlRefCodeDto();
            
            // Map actual SQL Server column names to DTO fields
            dto.setId(null); // No ID column in stored procedure
            dto.setCode(rs.getString("CHT_ACC_ALIAS"));
            dto.setDescription(rs.getString("CHT_ACC_NAME"));
            dto.setCategory(null); // No category column in stored procedure
            dto.setIsActive(true); // Assume all returned records are active
            dto.setCreatedDate(null); // No created date column in stored procedure
            dto.setUpdatedDate(null); // No updated date column in stored procedure
            
            return dto;
        }
    }
    
    // Helper methods for safe type conversion
    private Long getLongValue(Map<String, Object> row, String key) {
        Object value = row.get(key);
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).longValue();
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    private String getStringValue(Map<String, Object> row, String key) {
        Object value = row.get(key);
        return value != null ? value.toString() : null;
    }
    
    private Boolean getBooleanValue(Map<String, Object> row, String key) {
        Object value = row.get(key);
        if (value == null) return null;
        if (value instanceof Boolean) return (Boolean) value;
        if (value instanceof Number) return ((Number) value).intValue() != 0;
        if (value instanceof String) {
            String str = (String) value;
            return "true".equalsIgnoreCase(str) || "1".equals(str) || "Y".equalsIgnoreCase(str);
        }
        return null;
    }
}
