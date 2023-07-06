import java.util.*;
import java.util.Date;

class Book {
    private String title;
    private boolean isIssued;
    private static int bookCount = 0;
    private final int bookId;
    private Date issueDate;
    private Date returnDate;

    Book(String title) {
        this.title = title;
        this.isIssued = false;
        this.bookId = ++bookCount;
    }

    public String getTitle() {
        return this.title;
    }

    public int getBookId() {
        return this.bookId;
    }

    public boolean isIssued() {
        return this.isIssued;
    }

    public void issueBook() {
        if (this.isIssued) {
            throw new RuntimeException("Book is already issued.");
        }
        this.isIssued = true;
        this.issueDate = new Date();
    }

    public void returnBook() {
        if (this.isIssued) {
            this.isIssued = false;
            this.returnDate = new Date();
        } else {
            throw new RuntimeException("Book is not issued.");
        }
    }

    public boolean hasSameTitle(Book other) {
        return this.title.equals(other.title);
    }
}

abstract class Person {
    private String name;
    private int age;

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }
}

class Member extends Person {
    private int memberId;
    private static int memberCount = 0;
    private Stack<Book> issuedBooks;
    private static final int MAX_ISSUED_BOOKS = 5;

    Member(String name, int age) {
        super(name, age);
        this.memberId = ++memberCount;
        this.issuedBooks = new Stack<>();
    }

    public int getMemberId() {
        return this.memberId;
    }

    public void issueBook(Book book) {
        if (this.issuedBooks.size() >= MAX_ISSUED_BOOKS) {
            System.out.println("Maximum number of books already issued to this member.");
            return;
        }
        this.issuedBooks.push(book);
        book.issueBook();
    }

    public void returnBook(Book book) {
        if (this.issuedBooks.remove(book)) {
            book.returnBook();
        } else {
            throw new RuntimeException("This book is not issued to this member.");
        }
    }

    public Book getLastIssuedBook() {
        return this.issuedBooks.peek();
    }
}

class Librarian extends Person {
    private int librarianId;
    private static int librarianCount = 0;

    Librarian(String name, int age) {
        super(name, age);
        this.librarianId = ++librarianCount;
    }

    public int getLibrarianId() {
        return this.librarianId;
    }
}

interface LibraryOperations {
    void addBook(String title);
    void addMember(String name, int age);
    void addLibrarian(String name, int age);
    void issueBook(String memberName, String bookTitle);
    void returnBook(String memberName, String bookTitle);
    void displayBooks();
    void displayMembers();
}

class Library implements LibraryOperations {
    private List<Book> books;
    private Map<Integer, Member> members;
    private Map<Integer, Librarian> librarians;

    Library() {
        this.books = new ArrayList<>();
        this.members = new HashMap<>();
        this.librarians = new HashMap<>();
    }

    public void addBook(String title) {
        this.books.add(new Book(title));
    }

    public void addMember(String name, int age) {
        Member member = new Member(name, age);
        this.members.put(member.getMemberId(), member);
    }

    public void addLibrarian(String name, int age) {
        Librarian librarian = new Librarian(name, age);
        this.librarians.put(librarian.getLibrarianId(), librarian);
    }

    public Book findBook(String title) {
        for (Book book : this.books) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        throw new RuntimeException("Book not found.");
    }

    public Member findMember(String name) {
        for (Member member : this.members.values()) {
            if (member.getName().equals(name)) {
                return member;
            }
        }
        throw new RuntimeException("Member not found.");
    }

    public void issueBook(String memberName, String bookTitle) {
        Member member = findMember(memberName);
        Book book = findBook(bookTitle);
        member.issueBook(book);
    }

    public void returnBook(String memberName, String bookTitle) {
        Member member = findMember(memberName);
        Book book = findBook(bookTitle);
        member.returnBook(book);
    }

    public void displayBooks() {
        for (Book book : this.books) {
            System.out.printf("Book ID: %d, Title: %s, Issued: %b\n", book.getBookId(), book.getTitle(), book.isIssued());
        }
    }

    public void displayMembers() {
        for (Member member : this.members.values()) {
            System.out.println("Member ID: " + member.getMemberId() + ", Name: " + member.getName() + ", Age: " + member.getAge());
        }
    }

    public <T> T findById(int id, List<T> list) {
        for (T item : list) {
            if (item instanceof Book && ((Book) item).getBookId() == id) {
                return item;
            } else if (item instanceof Member && ((Member) item).getMemberId() == id) {
                return item;
            }
        }
        return null;
    }

    public void removeBook(String title) {
        Book book = findBook(title);
        this.books.remove(book);
    }

    public static Book findBookByTitleStatic(String title, List<Book> books) {
        for (Book book : books) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        throw new RuntimeException("Book not found.");
    }
}

public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Add book");
            System.out.println("2. Add member");
            System.out.println("3. Issue book");
            System.out.println("4. Return book");
            System.out.println("5. Display books");
            System.out.println("6. Display members");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // consume newline left-over
            switch (choice) {
                case 1:
                    System.out.print("Enter book title: ");
                    String title = scanner.nextLine();
                    library.addBook(title);
                    break;
                case 2:
                    System.out.print("Enter member name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter member age: ");
                    int age = scanner.nextInt();
                    scanner.nextLine();  // consume newline left-over
                    library.addMember(name, age);
                    break;
                case 3:
                    System.out.print("Enter member name: ");
                    String memberName = scanner.nextLine();
                    System.out.print("Enter book title: ");
                    String bookTitle = scanner.nextLine();
                    library.issueBook(memberName, bookTitle);
                    break;
                case 4:
                    System.out.print("Enter member name: ");
                    memberName = scanner.nextLine();
                    System.out.print("Enter book title: ");
                    bookTitle = scanner.nextLine();
                    library.returnBook(memberName, bookTitle);
                    break;
                case 5:
                    library.displayBooks();
                    break;
                case 6:
                    library.displayMembers();
                    break;
                case 7:
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
