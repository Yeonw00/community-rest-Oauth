# Community Space - Backend API

Backend API for Community Space service, built with Spring Boot, MySQL, and ElasticSearch.

## 🛠 Tech Stack
- **Language:** Java 17
- **Framework:** Spring Boot 3.x
- **Database:** MySQL (Relational Data) & **ElasticSearch** (High-speed Search)
- **Security:** Spring Security & JWT (JSON Web Token) & OAuth 2.0 (Google)
- **Storage:** Local Storage for user profile & post images

## 🌟 Key Features
- **Full-Text Search:** Implemented high-performance post/user search using **ElasticSearch's inverted index**.
- **Social Authentication:** Supports seamless user onboarding via Google OAuth 2.0 and secure session management with JWT.
- **Content Management:** Full CRUD operations for posts and comments with image upload capabilities.
- **Asynchronous Processing:** Efficient handling of image metadata and **thumbnail generation** for optimized listing.

## ⚙️ How to Run
1.  **Application Properties:**
    - Locate `src/main/resources/application.properties.sample`.
    - Copy and rename it to `application.properties`.
    - Fill in your actual credentials (MySQL, ElasticSearch, etc.).
2.  **Google OAuth Environment:**
    - Locate `google-client.env.sample`.
    - Copy and rename it to `google-client.env`.
    - Fill in your **Google Client ID** and **Client Secret**.
3.  **Services:**
    - Ensure MySQL and ElasticSearch services are running.
4.  **Run:**
    - Run the application: `./mvnw spring-boot:run`
