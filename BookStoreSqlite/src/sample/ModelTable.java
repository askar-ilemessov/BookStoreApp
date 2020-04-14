package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ModelTable {

    private StringProperty id , title, author, genre, isbn  , year;


    public  ModelTable(String id, String title, String author, String genre, String isbn, String year){

        this.id = new SimpleStringProperty(id);

        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.genre = new SimpleStringProperty(genre);
        this.isbn = new SimpleStringProperty(isbn);
        this.year = new SimpleStringProperty(year);

    }

    public String getId() {
        return id.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getAuthor() {
        return author.get();
    }

    public String getGenre() {
        return genre.get();
    }

    public String getIsbn() {
        return isbn.get();
    }

    public String getYear() {
        return year.get();
    }


    public void setId(String id) {
        this.id.set(id);
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public void setIsbn(String isbn) {
        this.isbn.set(isbn);
    }

    public void setYear(String year) {
        this.year.set(year);
    }
}
