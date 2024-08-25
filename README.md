# Library Management System

This is a simple library management system written in Java. It provides some functionalities to manage books and members in a library. The system includes features for book and member management, book issuance, renewals, and graphical representation of data.

## Features

- **Manage Books and Members:**
  - **Add**, **delete**, and **update** book and member details.

- **Book Issuance:**
  - Issue books to members.
  - **Renew** books after a specified number of days.
  - **Return** books to the system upon submission.

- **Graphical Representation:**
  - View a simple graphical representation of:
    - All books and their issuance status.
    - Issued books vs. total books.
    - Number of members vs. issued books.

## Libraries Used

- **JavaFX:** For creating the graphical user interface.
- **Apache Derby:** For the embedded database to store book and member information.
- **Gson:** For parsing and handling JSON data.

## Regular Login

The system uses the following default credentials for regular login:
- **Username:** `admin`
- **Password:** `admin`
