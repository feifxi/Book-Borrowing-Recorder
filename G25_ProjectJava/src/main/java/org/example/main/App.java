package org.example.main;

import org.example.ui.BookBorrowingRecorderUI;

public class App {
        public static void main(String[] args) {
            var app = new BookBorrowingRecorderUI(true);   // Database
//            var app = new BookBorrowingRecorderUI(false);     // File
//            var app = new BookBorrowingRecorderUI();          // Memory

            app.start();
        }
}


