package com.example.pretask.book;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "books", indexes = {
		@Index(name = "idx_books_title", columnList = "title")
})
public class BookEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String author;

	@Column(nullable = false)
	private String publisher;

	@Column(nullable = false)
	private String category;

	@Column(name = "published_date", nullable = false)
	private LocalDate publishedDate;

	@Column(nullable = false)
	private String isbn;

	@Column(nullable = false)
	private int price;

	@Column(nullable = false)
	private int stock;

	protected BookEntity() {
	}

	public BookEntity(
			String title,
			String author,
			String publisher,
			String category,
			LocalDate publishedDate,
			String isbn,
			int price,
			int stock
	) {
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.category = category;
		this.publishedDate = publishedDate;
		this.isbn = isbn;
		this.price = price;
		this.stock = stock;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getPublisher() {
		return publisher;
	}

	public String getCategory() {
		return category;
	}

	public LocalDate getPublishedDate() {
		return publishedDate;
	}

	public String getIsbn() {
		return isbn;
	}

	public int getPrice() {
		return price;
	}

	public int getStock() {
		return stock;
	}
}
