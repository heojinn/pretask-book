package com.example.pretask.book;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BookCsvLoader implements ApplicationRunner {

	private static final String EXPECTED_HEADER = "id,title,author,publisher,category,published_date,isbn,price,stock";
	private static final int COLUMN_COUNT = 9;
	private static final int BATCH_SIZE = 1_000;

	private final BookRepository bookRepository;
	private final Path csvPath;

	public BookCsvLoader(BookRepository bookRepository, @Value("${app.book-csv.path:books.csv}") String csvPath) {
		this.bookRepository = bookRepository;
		this.csvPath = Path.of(csvPath);
	}

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		loadFrom(csvPath);
	}

	public void loadFrom(Path path) {
		bookRepository.deleteAllInBatch();

		try (BufferedReader reader = Files.newBufferedReader(path)) {
			String header = reader.readLine();
			if (!EXPECTED_HEADER.equals(header)) {
				throw new IllegalStateException("Invalid books.csv header: " + header);
			}

			List<BookEntity> batch = new ArrayList<>(BATCH_SIZE);
			String line;
			int lineNumber = 1;

			while ((line = reader.readLine()) != null) {
				lineNumber++;
				batch.add(parseBook(line, lineNumber));

				if (batch.size() == BATCH_SIZE) {
					bookRepository.saveAll(batch);
					batch.clear();
				}
			}

			if (!batch.isEmpty()) {
				bookRepository.saveAll(batch);
			}
		}
		catch (IOException exception) {
			throw new IllegalStateException("Failed to read books CSV: " + path, exception);
		}
	}

	private BookEntity parseBook(String line, int lineNumber) {
		String[] columns = line.split(",", -1);
		if (columns.length != COLUMN_COUNT) {
			throw new IllegalStateException("Invalid column count at line " + lineNumber + ": " + columns.length);
		}

		for (int index = 0; index < columns.length; index++) {
			if (columns[index].isBlank()) {
				throw new IllegalStateException("Blank value at line " + lineNumber + ", column " + (index + 1));
			}
		}

		try {
			return new BookEntity(
					columns[1],
					columns[2],
					columns[3],
					columns[4],
					LocalDate.parse(columns[5]),
					columns[6],
					Integer.parseInt(columns[7]),
					Integer.parseInt(columns[8])
			);
		}
		catch (DateTimeParseException | NumberFormatException exception) {
			throw new IllegalStateException("Invalid value at line " + lineNumber, exception);
		}
	}
}
