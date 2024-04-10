## Discovery API | Android Impl
This project aims to implement the Ticketmaster Discovery API, providing developers with an avenue to create powerful applications that leverage event data in general. The goal is to create a fluid and easy-to-use API SDK that simplifies integration with the Ticketmaster API while adhering to best practices in Android development with the goal of reusing libraries to centralize processes or API calls.

## Overview
This repository contains an Android application developed with clean architecture, integrating the Ticketmaster Discovery API v2. The project leverages libraries such as Paging3, Room, Retrofit, Hilt, and follows the principles of Clean Architecture. The main feature implemented is the HomeFragment, which provides users with an updated list of events and an input search to discover new events.

#### Ticketmaster Discovery API

The Ticketmaster Discovery API v2 allows users to search for events, attractions, or venues. To access the API, an API key is required, which should be included in the secrets.properties file. The API key should be added as a query parameter in the API calls.

#### API Key Setup

To work with the secrets.properties file, follow these steps:

    Copy the secrets.defaults.properties file.
    Create a new file named secrets.properties.
    Add the sensitive keys or variables, such as the API key, to the secrets.properties file.


#### Project Structure

The project follows the Clean Architecture principles and utilizes various Android libraries to ensure modularity, scalability, and maintainability. The architecture separates the application into layers, including presentation, domain, and data layers, to promote a clear separation of concerns.
<img src="https://developer.android.com/static/topic/libraries/architecture/images/paging3-layered-lifecycle.png"/>

#### Libraries

The project integrates the following libraries:

    Paging3: For efficient pagination of data.
    Room: To provide an abstraction layer over SQLite for database operations.
    Retrofit: For making network requests to the Ticketmaster Discovery API.
    Hilt: For dependency injection to ensure a modular and testable codebase.

Feature: HomeFragment

The HomeFragment feature provides users with an updated list of events and an input search to discover new events. This feature leverages the capabilities of the Ticketmaster Discovery API to fetch and display event data.

### Getting Started

To run the project locally, follow these steps:

    Clone the repository.
    Set up the secrets.properties file as described in the "API Key Setup" section.
    Build and run the project using Android Studio or the command line.


### Contribution Guidelines

Contributions to the project are welcome. If you would like to contribute, please follow the contribution guidelines outlined in the repository. By providing comprehensive details about the project, its features, and the Ticketmaster Discovery API integration, this README aims to offer users a clear understanding of the repository and its purpose.

If you have any further questions or need additional information, feel free to reach out.
