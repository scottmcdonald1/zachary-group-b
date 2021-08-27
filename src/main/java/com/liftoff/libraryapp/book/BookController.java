package com.liftoff.libraryapp.book;

import com.liftoff.libraryapp.models.Book;
import com.liftoff.libraryapp.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Optional;


@Controller
@RequestMapping("user")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("add")
    public String displayAddBookForm(Model model) {
        model.addAttribute(new Book());
        return "book/add";
    }

    @PostMapping("add")
    public String processAddBookForm(@ModelAttribute @Valid Book newBook,
                                    Errors errors, Model model, @RequestParam String title, @RequestParam String author, @RequestParam String isbn,
                                     @RequestParam String pages, @RequestParam String genre, @RequestParam String status, @RequestParam String rating) {
        if (errors.hasErrors()) {
            return "/add";
        }

        DateFormat Date = DateFormat.getDateInstance();
        Calendar cals = Calendar.getInstance();
        String currentDate = Date.format(cals.getTime());
        model.addAttribute("date", currentDate);

        newBook.setDate(currentDate);

        bookRepository.save(newBook);

        return "redirect:shelf";
    }

    @GetMapping("delete")
    public String displayDeleteBooksForm(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "book/delete";
    }

    @PostMapping("delete")
    public String processDeleteBooksForm(@RequestParam(required = false) int[] bookIds) {
        if (bookIds != null) {
            for (int id : bookIds) {
                bookRepository.deleteById(id);
            }
        }
        return "redirect:shelf";
    }

    @GetMapping("edit/{bookId}")
    public String displayEditForm(Model model, @PathVariable int bookId) {
        Optional<Book> selectedBook = bookRepository.findById(bookId);
        model.addAttribute("selectedBook",selectedBook);

        return "redirect:";
    }

    /*@PostMapping("edit")
    public String processEditForm(String name, String description) {
        Event selectedEvent = EventData.getById(eventId);
        selectedEvent.setName(name);
        selectedEvent.setDescription(description);
        return "redirect:";
    }*/

    @RequestMapping("shelf")
    public String displayBookshelf(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "book/shelf";
    }

    @GetMapping("view/{bookId}")
    public String displayViewBook(Model model, @PathVariable int bookId) {
        Optional<Book> optBook = bookRepository.findById(bookId);
        if (optBook.isPresent()) {
            Book book = (Book) optBook.get();
            model.addAttribute("book", book);
            return "book/view";
        } else {
            return "redirect:../shelf";
        }
    }
}
