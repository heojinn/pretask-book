package com.example.pretask.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class BookService {

	public static final int DEFAULT_PAGE = 0;
	public static final int DEFAULT_SIZE = 10;
	public static final int MAX_SIZE = 100;

	private final BookRepository bookRepository;

	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	public Page<BookEntity> searchByTitle(String query, Integer page, Integer size) {
		PageRequest pageRequest = PageRequest.of(normalizePage(page), normalizeSize(size));
		if (!StringUtils.hasText(query)) {
			return Page.empty(pageRequest);
		}

		return bookRepository.findByTitleContainingIgnoreCase(query.trim(), pageRequest);
	}

	private int normalizePage(Integer page) {
		if (page == null || page < 0) {
			return DEFAULT_PAGE;
		}

		return page;
	}

	private int normalizeSize(Integer size) {
		if (size == null || size <= 0) {
			return DEFAULT_SIZE;
		}

		return Math.min(size, MAX_SIZE);
	}
}
