package mapper;

import model.Book;
import model.builder.BookBuilder;
import view.model.BookDTO;
import view.model.builder.BookDTOBuilder;

import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class BookMapper {
    public static BookDTO convertBookToBookDTO(Book book){
        return new BookDTOBuilder()
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setPrice(book.getPrice())
                .setStock(book.getStock())
                .build();
    }

    public static Book convertBookDTOToBook(BookDTO bookDTO){
        return new BookBuilder().
                setTitle(bookDTO.getTitle())
                .setAuthor(bookDTO.getAuthor())
                .setPublishedDate(LocalDate.of(2010,1,1))
                .setPrice(bookDTO.getPrice())
                .setStock(bookDTO.getStock())
                .build();
    }
    public static BookDTO convertBookToBookDTOSales(Book book){
        /*return new BookDTOBuilder().setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setPrice(book.getPrice())
                .setStock(book.getStock())
                .build();*/
        return new BookDTOBuilder().setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setPrice(book.getPrice())
                .build();
    }
    public static List<BookDTO> convertBookListToBookDTOListSales(List<Book> books){
        return books.parallelStream().map(BookMapper::convertBookToBookDTOSales).collect(Collectors.toList());
    }
    public static List<BookDTO> convertBookListToBookDTOList(List<Book> books){
        return books.parallelStream().map(BookMapper::convertBookToBookDTO).collect(Collectors.toList());
    }

    public static List<Book> convertBookDTOListToBookList(List<BookDTO> bookDTOS){
        return bookDTOS.parallelStream().map(BookMapper::convertBookDTOToBook).collect(Collectors.toList());
    }
}