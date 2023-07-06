import java.util.*;
import java.util.Date;

/**
 * This class represents a book in the library.
 */
class Book {
    private String title;
    private boolean isIssued;
    private static int bookCount = 0;
    private final int bookId;
    private Date issueDate;
    private Date returnDate;

    /**
     * Constructs a new book with the given title.
     *
     * @param title the title of the book
     */
    Book(String title) {
        this.title = title;
        this.isIssued = false;
        this.bookId = ++bookCount;
    }

    /**
     * Returns the title of the book.
     *
     * @return the title of the book
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Returns the ID of the book.
     *
     * @return the ID of the book
     */
    public int getBookId() {
        return this.bookId;
    }

    /**
     * Returns whether the book is issued.
     *
     * @return true if the book is issued, false otherwise
     */
    public boolean isIssued() {
        return this.isIssued;
    }

    /**
     * Issues the book. If the book is already issued, a RuntimeException is thrown.
     */
    public void issueBook() {
        if (this.isIssued) {
            throw new RuntimeException("Book is already issued.");
        }
        this.isIssued = true;
        this.issueDate = new Date();
    }
    /**
     * Returns the book. If the book is not issued, a RuntimeException is thrown.
     */
    public void returnBook() {
        if (this.isIssued) {
            this.isIssued = false;
            this.returnDate = new Date();
        } else {
            throw new RuntimeException("Book is not issued.");
        }
    }

    /**
     * Checks if this book has the same title as the other book.
     *
     * @param other the other book
     * @return true if the titles are the same, false otherwise
     */
    public boolean hasSameTitle(Book other) {
        return this.title.equals(other.title);
    }
}

/**
 * This abstract class represents a person.
 */
abstract class Person {
    private String name;
    private int age;

    /**
     * Constructs a new person with the given name and age.
     *
     * @param name the name of the person
     * @param age the age of the person
     */
    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    /**
     * Returns the name of the person.
     *
     * @return the name of the person
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the age of the person.
     *
     * @return the age of the person
     */
    public int getAge() {
        return this.age;
    }
}
/**
 * This class represents a member of the library.
 */
class Member extends Person {
    private int memberId;
    private static int memberCount = 0;
    private Stack<Book> issuedBooks;
    private static final int MAX_ISSUED_BOOKS = 5;

    /**
     * Constructs a new member with the given name and age.
     *
     * @param name the name of the member
     * @param age the age of the member
     */
    Member(String name, int age) {
        super(name, age);
        this.memberId = ++memberCount;
        this.issuedBooks = new Stack<>();
    }

    /**
     * Returns the ID of the member.
     *
     * @return the ID of the member
     */
    public int getMemberId() {
        return this.memberId;
    }

    /**
     * Issues a book to the member. If the member has already issued the maximum number of books, a message is printed.
     *
     * @param book the book to issue
     */
    public void issueBook(Book book) {
        if (this.issuedBooks.size() >= MAX_ISSUED_BOOKS) {
            System.out.println("Maximum number of books already issued to this member.");
            return;
        }
        this.issuedBooks.push(book);
        book.issueBook();
    }
    /**
     * Returns a book from the member. If the book is not issued to the member, a RuntimeException is thrown.
     *
     * @param book the book to return
     */
    public void returnBook(Book book) {
        if (this.issuedBooks.remove(book)) {
            book.returnBook();
        } else {
            throw new RuntimeException("This book is not issued to this member.");
        }
    }

    /**
     * Returns the last book issued by the member.
     *
     * @return the last book issued by the member
     */
    public Book getLastIssuedBook() {
        return this.issuedBooks.peek();
    }
}

/**
 * This class represents a librarian.
 */
class Librarian extends Person {
    private int librarianId;
    private static int librarianCount = 0;

    /**
     * Constructs a new librarian with the given name and age.
     *
     * @param name the name of the librarian
     * @param age the age of the librarian
     */
    Librarian(String name, int age) {
        super(name, age);
        this.librarianId = ++librarianCount;
    }

    /**
     * Returns the ID of the librarian.
     *
     * @return the ID of the librarian
     */
    public int getLibrarianId() {
        return this.librarianId;
    }
}
/**
 * This interface defines the operations that a library should support.
 */
interface LibraryOperations {
    void addBook(String title);
    void addMember(String name, int age);
    void addLibrarian(String name, int age);
    void issueBook(String memberName, String bookTitle);
    void returnBook(String memberName, String bookTitle);
    void displayBooks();
    void displayMembers();
}

/**
 * This class represents a library, which contains books and members.
 */
class Library implements LibraryOperations {
    private List<Book> books;
    private Map<Integer, Member> members;
    private Map<Integer, Librarian> librarians;

    /**
     * Constructs a new library.
     */
    Library() {
        this.books = new ArrayList<>();
        this.members = new HashMap<>();
        this.librarians = new HashMap<>();
    }

    /**
     * Adds a book to the library.
     *
     * @param title the title of the book
     */
    public void addBook(String title) {
        this.books.add(new Book(title));
    }

    /**
     * Adds a member to the library.
     *
     * @param name the name of the member
     * @param age  the age of the member
     */
    public void addMember(String name, int age) {
        Member member = new Member(name, age);
        this.members.put(member.getMemberId(), member);
    }

    /**
     * Adds a librarian to the library.
     *
     * @param name the name of the librarian
     * @param age the age of the librarian
     */
    public void addLibrarian(String name, int age) {
        Librarian librarian = new Librarian(name, age);
        this.librarians.put(librarian.getLibrarianId(), librarian);
    }

    /**
     * Finds a book in the library by title.
     *
     * @param title the title of the book
     * @return the book with the given title
     * @throws RuntimeException if the book is not found
     */
    public Book findBook(String title) {
        for (Book book : this.books) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        throw new RuntimeException("Book not found.");
    }

    /**
     * Finds a member in the library by name.
     *
     * @param name the name of the member
     * @return the member with the given name
     * @throws RuntimeException if the member is not found
     */
    public Member findMember(String name) {
        for (Member member : this.members.values()) {
            if (member.getName().equals(name)) {
                return member;
            }
        }
        throw new RuntimeException("Member not found.");
    }

    /**
     * Issues a book to a member.
     *
     * @param memberName the name of the member
     * @param bookTitle the title of the book
     */
    public void issueBook(String memberName, String bookTitle) {
        Member member = findMember(memberName);
        Book book = findBook(bookTitle);
        member.issueBook(book);
    }

    @Override
    public void returnBook(String memberName, String bookTitle) {

    }

    @Override
    public void displayBooks() {

    }

    @Override
    public void displayMembers() {

    }
}