package mate.academy.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import mate.academy.dao.BookDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Book;
import mate.academy.util.ConnectionUtil;

@Dao
public class BookDaoImpl implements BookDao {
    @Override
    public Book create(Book book) {
        String query = "INSERT INTO books (title, price) VALUES (?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, book.getTitle());
            stmt.setBigDecimal(2, book.getPrice());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                book.setId(keys.getObject(1, Long.class));
            }
            return book;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't save book " + book, e);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        String query = "SELECT id, title, price FROM books WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(parseBook(rs));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get book by id " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Book> findAll() {
        String query = "SELECT id, title, price FROM books";
        List<Book> books = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(parseBook(rs));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get books", e);
        }
        return books;
    }

    @Override
    public Book update(Book book) {
        String query = "UPDATE books SET title = ?, price = ? WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, book.getTitle());
            stmt.setBigDecimal(2, book.getPrice());
            stmt.setLong(3, book.getId());
            stmt.executeUpdate();
            return book;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update book " + book, e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        String query = "DELETE FROM books WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            int updated = stmt.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete book by id " + id, e);
        }
    }

    private Book parseBook(ResultSet rs) throws SQLException {
        Long id = rs.getObject("id", Long.class);
        String title = rs.getString("title");
        BigDecimal price = rs.getBigDecimal("price");
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setPrice(price);
        return book;
    }
}
