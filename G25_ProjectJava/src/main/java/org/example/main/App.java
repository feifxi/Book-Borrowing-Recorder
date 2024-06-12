package org.example.main;

import org.example.ui.BookBorrowingRecorderUI;

public class App {
        public static void main(String[] args) {
            var app = new BookBorrowingRecorderUI();
            app.start();    // Start App!
        }
}
// File Storage - Make sure to set filepath first! (in FilePATH class)
// Database Storage - Make sure to connecting to database and specify password first! (in DatabaseConnection class)