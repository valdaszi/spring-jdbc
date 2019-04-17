package lt.bta.java2.sprngjdbc;

import lt.bta.java2.sprngjdbc.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class SprngJdbcApplication {

	public static void main(String[] args) {
		SpringApplication.run(SprngJdbcApplication.class, args);
	}

}

class ProductMapper implements RowMapper<Product> {
	@Override
	public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
		Product p = new Product();

		p.setId(rs.getInt("id"));
		p.setName(rs.getString("name"));
		p.setDescription(rs.getString("description"));
		p.setPrice(rs.getBigDecimal("price"));
		p.setImage(rs.getString("image"));

		return p;
	}
}

@RestController
class Api {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@GetMapping("/ask")
	public Map method() {
		return Collections.singletonMap("value", 42);
	}

	@GetMapping("/product/{id}")
	public Product product(@PathVariable int id) {
//		return jdbcTemplate.query(
//				"SELECT * FROM products WHERE id = ?",
//
//				new Object[] {id},
//
//				(rs) -> {
//					if (rs.next()) {
//						return new ProductMapper().mapRow(rs, 1);
//					}
//					return null;
//				});

		// arba galima paprasčiau:
		return jdbcTemplate.queryForObject("SELECT * FROM products WHERE id = ?", new ProductMapper(), id);
	}

	@GetMapping("/product")
	public List<Product> products() {
		return jdbcTemplate.query("SELECT * FROM products", new ProductMapper());
	}

	@PostMapping("/product")
	public Product createProduct(@RequestBody Product product) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(con -> {
			PreparedStatement stmt = con.prepareStatement("INSERT INTO products(name, description, price, image) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, product.getName());
			stmt.setString(2, product.getDescription());
			stmt.setBigDecimal(3, product.getPrice());
			stmt.setString(4, product.getImage());
			return stmt;
		}, keyHolder);

		if (keyHolder.getKey() != null) product.setId(keyHolder.getKey().intValue());
		return product;
	}

	@PutMapping("/product/{id}")
	public Integer updateProduct(@PathVariable int id, @RequestBody Product product) {
		return jdbcTemplate.update("UPDATE products SET name = ?, description = ?, price = ?, image = ? WHERE id = ?",
				product.getName(), product.getDescription(), product.getPrice(), product.getImage(), id);
	}

	@GetMapping("/product/count")
	public Integer count() {
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM products", Integer.class);
	}

}
