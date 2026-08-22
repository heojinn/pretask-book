package com.example.pretask.book;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;

@DataJpaTest
class BookServiceTests {

	@Autowired
	BookRepository bookRepository;

	BookService bookService;

	@BeforeEach
	void setUp() {
		bookService = new BookService(bookRepository);
		bookRepository.deleteAllInBatch();
		bookRepository.save(newBook("자바 실전 입문"));
		bookRepository.save(newBook("스프링 부트와 자바"));
		bookRepository.save(newBook("데이터베이스 기초"));
		bookRepository.save(newBook("자바 웹 개발"));
		bookRepository.save(newBook("테스트 자동화"));
	}

	@Test
	void searchByTitleReturnsContainingMatches() {
		Page<BookEntity> result = bookService.searchByTitle("자바", 0, 10);

		assertThat(result.getTotalElements()).isEqualTo(3);
		assertThat(result.getContent())
				.extracting(BookEntity::getTitle)
				.containsExactlyInAnyOrder("자바 실전 입문", "스프링 부트와 자바", "자바 웹 개발");
	}

	@Test
	void searchByTitleReturnsEmptyPageWhenNoResult() {
		Page<BookEntity> result = bookService.searchByTitle("없는책", 0, 10);

		assertThat(result.getTotalElements()).isZero();
		assertThat(result.getContent()).isEmpty();
	}

	@Test
	void searchByTitleAppliesPagination() {
		Page<BookEntity> result = bookService.searchByTitle("자바", 1, 2);

		assertThat(result.getNumber()).isEqualTo(1);
		assertThat(result.getSize()).isEqualTo(2);
		assertThat(result.getTotalElements()).isEqualTo(3);
		assertThat(result.getTotalPages()).isEqualTo(2);
		assertThat(result.getContent()).hasSize(1);
	}

	@Test
	void searchByTitleNormalizesInvalidPageParameters() {
		Page<BookEntity> result = bookService.searchByTitle("자바", -1, 0);

		assertThat(result.getNumber()).isZero();
		assertThat(result.getSize()).isEqualTo(BookService.DEFAULT_SIZE);
		assertThat(result.getTotalElements()).isEqualTo(3);
	}

	@Test
	void searchByTitleReturnsEmptyPageForBlankQuery() {
		Page<BookEntity> result = bookService.searchByTitle(" ", 0, 10);

		assertThat(result.getTotalElements()).isZero();
		assertThat(result.getContent()).isEmpty();
	}

	private BookEntity newBook(String title) {
		return new BookEntity(
				title,
				"저자",
				"출판사",
				"분류",
				LocalDate.of(2024, 1, 1),
				"9780000000000",
				15_000,
				10
		);
	}
}
