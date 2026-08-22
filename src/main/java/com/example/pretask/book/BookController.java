package com.example.pretask.book;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookController {

	private final BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@GetMapping({ "/", "/books" })
	public String books(
			@RequestParam(name = "query", required = false) String query,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size,
			Model model
	) {
		Page<BookEntity> booksPage = bookService.searchByTitle(query, page, size);
		boolean requestedSearch = query != null;

		model.addAttribute("query", query == null ? "" : query);
		model.addAttribute("booksPage", booksPage);
		model.addAttribute("books", booksPage.getContent());
		model.addAttribute("requestedSearch", requestedSearch);
		model.addAttribute("blankQuery", requestedSearch && !StringUtils.hasText(query));
		model.addAttribute("pageSize", booksPage.getSize());

		return "books";
	}
}
