package com.example.userservice.repository;

import com.example.core.database.repository.BaseRepository;
import com.example.userservice.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<User, Long> {

    private static final String INSERT_USER = """
        INSERT INTO users (name, email, created_at, updated_at) 
        VALUES (:name, :email, :createdAt, :updatedAt)
        """;

    private static final String UPDATE_USER = """
        UPDATE users SET name = :name, email = :email, updated_at = :updatedAt 
        WHERE id = :id
        """;

    private static final String SELECT_ALL_USERS = """
        SELECT id, name, email, created_at, updated_at FROM users 
        ORDER BY created_at DESC
        """;

    private static final String SELECT_USER_BY_ID = """
        SELECT id, name, email, created_at, updated_at FROM users WHERE id = ?
        """;

    private static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?";

    private static final String EXISTS_USER_BY_ID = "SELECT COUNT(*) FROM users WHERE id = ?";

    private static final String SELECT_USER_BY_EMAIL = """
        SELECT id, name, email, created_at, updated_at FROM users WHERE email = ?
        """;

    private final RowMapper<User> userRowMapper = new UserRowMapper();

    public UserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            // Insert new user
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("name", user.getName())
                    .addValue("email", user.getEmail())
                    .addValue("createdAt", user.getCreatedAt())
                    .addValue("updatedAt", user.getUpdatedAt());

            Long id = executeInsertAndReturnKey(INSERT_USER, params);
            user.setId(id);
        } else {
            // Update existing user
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("id", user.getId())
                    .addValue("name", user.getName())
                    .addValue("email", user.getEmail())
                    .addValue("updatedAt", user.getUpdatedAt());

            namedParameterJdbcTemplate.update(UPDATE_USER, params);
        }
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        List<User> users = jdbcTemplate.query(SELECT_USER_BY_ID, userRowMapper, id);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(SELECT_ALL_USERS, userRowMapper);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_USER_BY_ID, id);
    }

    @Override
    public boolean existsById(Long id) {
        Integer count = jdbcTemplate.queryForObject(EXISTS_USER_BY_ID, Integer.class, id);
        return count != null && count > 0;
    }

    public Optional<User> findByEmail(String email) {
        List<User> users = jdbcTemplate.query(SELECT_USER_BY_EMAIL, userRowMapper, email);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return user;
        }
    }
}