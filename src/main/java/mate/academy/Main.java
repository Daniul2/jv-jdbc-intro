package mate.academy;

import java.math.BigDecimal;
import mate.academy.dao.BookDao;
import mate.academy.lib.Injector;
import mate.academy.model.Book;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        BookDao bookDao = (BookDao) injector.getInstance(BookDao.class);

        // CREATE
        Book book = new Book();
        book.setTitle("Clean Code");
        book.setPrice(BigDecimal.valueOf(25.99));
        bookDao.create(book);
        System.out.println("Created: " + book);

        // READ
        bookDao.findById(book.getId()).ifPresent(System.out::println);

        // UPDATE
        book.setPrice(BigDecimal.valueOf(29.99));
        bookDao.update(book);
        System.out.println("Updated: " + book);

        // FIND ALL
        System.out.println("All books: " + bookDao.findAll());

        // DELETE
        boolean deleted = bookDao.deleteById(book.getId());
        System.out.println("Deleted: " + deleted);
    }
}
