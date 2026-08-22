package com.example.pretask.book;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class BookCsvLoaderTests {

	@TempDir
	Path tempDir;

	@Autowired
	BookRepository bookRepository;

	@Test
	void loadFromDeletesExistingRowsAndStoresCsvRowsWithGeneratedIds() throws IOException {
		bookRepository.save(new BookEntity(
				"old title",
				"old author",
				"old publisher",
				"old category",
				LocalDate.of(2020, 1, 1),
				"9780000000000",
				1000,
				1
		));
		BookCsvLoader loader = new BookCsvLoader(bookRepository, "unused.csv");

		loader.loadFrom(writeCsv(
				"id,title,author,publisher,category,published_date,isbn,price,stock",
				"100,첫 번째 책,김하나,문학동네,소설,2022-01-02,9781111111111,15000,7",
				"200,두 번째 책,이두리,민음사,과학,2021-12-31,9782222222222,22000,3"
		));

		List<BookEntity> books = bookRepository.findAll();
		assertThat(books).hasSize(2);
		assertThat(books)
				.extracting(BookEntity::getTitle)
				.containsExactlyInAnyOrder("첫 번째 책", "두 번째 책");
		assertThat(books)
				.extracting(BookEntity::getId)
				.doesNotContain(100L, 200L);

		BookEntity firstBook = books.stream()
				.filter(book -> book.getTitle().equals("첫 번째 책"))
				.findFirst()
				.orElseThrow();
		assertThat(firstBook.getAuthor()).isEqualTo("김하나");
		assertThat(firstBook.getPublisher()).isEqualTo("문학동네");
		assertThat(firstBook.getCategory()).isEqualTo("소설");
		assertThat(firstBook.getPublishedDate()).isEqualTo(LocalDate.of(2022, 1, 2));
		assertThat(firstBook.getIsbn()).isEqualTo("9781111111111");
		assertThat(firstBook.getPrice()).isEqualTo(15000);
		assertThat(firstBook.getStock()).isEqualTo(7);
	}

	@Test
	void loadFromRejectsInvalidHeader() throws IOException {
		BookCsvLoader loader = new BookCsvLoader(bookRepository, "unused.csv");

		Path csv = writeCsv(
				"title,author,publisher,category,published_date,isbn,price,stock",
				"제목,저자,출판사,분류,2022-01-02,9781111111111,15000,7"
		);

		assertThatThrownBy(() -> loader.loadFrom(csv))
				.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("Invalid books.csv header");
	}

	@Test
	void loadFromRejectsInvalidDateOrNumber() throws IOException {
		BookCsvLoader loader = new BookCsvLoader(bookRepository, "unused.csv");

		Path csv = writeCsv(
				"id,title,author,publisher,category,published_date,isbn,price,stock",
				"1,제목,저자,출판사,분류,not-a-date,9781111111111,15000,7"
		);

		assertThatThrownBy(() -> loader.loadFrom(csv))
				.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("Invalid value at line 2");
	}

	private Path writeCsv(String... lines) throws IOException {
		Path csv = tempDir.resolve("books.csv");
		Files.write(csv, List.of(lines));
		return csv;
	}
}
