# College Management System (CMS)

Welcome to the College Management System (CMS) repository! This project aims to be user on administrative tasks in educational institutions.

Created by: Bruno Bucci

## Table of Contents

1. [Introduction](#introduction)
2. [Getting Started](#getting-started)
    - [Installation](#installation)
    - [Logging In](#logging-in)
3. [Main Menu](#main-menu)
    - [User Roles](#user-roles)
    - [Available Actions](#available-actions)
4. [Using the CMS](#using-the-cms)
    - [Generating Reports](#generating-reports)
    - [Managing Users](#managing-users)
5. [Logging Out](#logging-out)

## 1. Introduction <a name="introduction"></a>

The College Management System (CMS) is a comprehensive application designed to be used on administrative tasks in educational institutions.
This App was created by Bruno Bucci in order to complete the integrated CA between: Database, Programming and Object Oriented Analysis and Design.

## 2. Getting Started <a name="getting-started"></a>

### Installation <a name="installation"></a>

Before using the CMS application, ensure that you have the necessary dependencies installed, including:

- Java Development Kit (JDK)
- MySQL Database Server

To install the CMS application:

1. Clone the repository from GitHub.
2. Set up the MySQL database using the provided schema or script.
3. Update the database connection details in the `Main.java` file.
4. Compile and run the application using a Java IDE or command line.

### Logging In <a name="logging-in"></a>

Upon launching the CMS application, you will be prompted to log in with your username and password. Enter your credentials to access the main menu.

## 3. Main Menu <a name="main-menu"></a>

### User Roles <a name="user-roles"></a>

The CMS application supports three user roles:

- **Administrator (ADMIN)**: Administrators can have full access to all features and functionalities of the CMS, including user management and report generation, however, the option for generate report was removed as requested in the app requirements.
- **Office Staff (OFFICE)**: Office staff members can generate all reports and manage their account details, but they do not have access to administrative functions.
- **Lecturer (LECTURER)**: Lecturers can generate reports related to their courses and manage their account details.

### Available Actions <a name="available-actions"></a>

The main menu presents different options based on your user role. Common actions include:

- Generating Course, Student, and Lecturer reports.
- Changing your username and password.
- Adding, removing, or updating user accounts (administrators only).

## 4. Using the CMS <a name="using-the-cms"></a>

### Generating Reports <a name="generating-reports"></a>

Depending on your role, you can generate various reports:

- **Course Report**: View information about courses offered.
- **Student Report**: Access student enrollment and academic records.
- **Lecturer Report**: Retrieve details about lecturers and their courses.

### Managing Users <a name="managing-users"></a>

Administrators have the ability to manage user accounts, including:

- Adding new users with specified roles.
- Removing existing users from the system.
- Updating user information, such as username, password, and role.

## 5. Logging Out <a name="logging-out"></a>

To exit the CMS application, simply select the "Logout" option from the main menu. This will securely log you out of your account and return you to the login screen.
