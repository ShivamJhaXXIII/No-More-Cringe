# 🧨 No More Cringe — Resume Roast App

A brutally honest resume roasting app powered by Spring Boot, Llama, and Groq, with PDF parsing magic. Upload your resume (PDF), get roasted by AI, and improve with spicy suggestions. 💀🔥

-----

## 🚀 Features

  - 🔍 Upload resume (PDF) via web UI
  - 🧠 Uses **Llama** and **Groq** to roast your resume
  - 📈 Generates a "Cringe Score" (1–100)
  - 🗒️ Gives constructive suggestions
  - 📦 Spring Boot backend with Thymeleaf frontend
  - 📄 Jar release for one-click setup

-----

## 🛠️ Tech Stack

| Layer | Tool/Tech |
|---|---|
| Backend | Spring Boot (Java) |
| Frontend | Thymeleaf |
| PDF Parsing | Apache PDFBox |
| AI Roast | Llama (via Groq API) |
| Template Engine | Thymeleaf |

-----

## 📦 Run the Project

You have a few options to get "No More Cringe" up and running:

### Executable Jar

1.  Go to [Releases](https://github.com/ShivamJhaXXIII/No-More-Cringe/releases)
2.  Download the latest `.jar` file.
3.  Run it:
    ```bash
    java -jar no-more-cringe-1.0.0.jar
    ```

### From Source (using Maven)

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/your-repo-name.git
    cd no-more-cringe
    ```
2.  **Build the project:**
    ```bash
    mvn clean install
    ```
3.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```

### Using Docker

1.  **Build the Docker image:**
    ```bash
    docker build -t no-more-cringe .
    ```
2.  **Run the Docker container:**
    ```bash
    docker run -p 8080:8080 -e GROQ_API_KEY=your_groq_api_key no-more-cringe
    ```
    (Remember to replace `your_groq_api_key` with your actual Groq API key.)

-----

## 🔑 API Key Setup

This application uses the Groq API to access Llama models for the resume roasting. You'll need to set up your Groq API key for the application to function correctly.

1.  **Get your Groq API Key:**

      * Visit the [Groq Console](https://console.groq.com/keys).
      * Sign in or create an account.
      * Generate a new API key.

2.  **Set the API Key:**

      * **For Jar/Maven:** Set the `GROQ_API_KEY` environment variable before running the application.
          * **Linux/macOS:**
            ```bash
            export GROQ_API_KEY="your_groq_api_key_here"
            java -jar no-more-cringe-1.0.0.jar # or mvn spring-boot:run
            ```
          * **Windows (Command Prompt):**
            ```bash
            set GROQ_API_KEY="your_groq_api_key_here"
            java -jar no-more-cringe-1.0.0.jar # or mvn spring-boot:run
            ```
          * **Windows (PowerShell):**
            ```powershell
            $env:GROQ_API_KEY="your_groq_api_key_here"
            java -jar no-more-cringe-1.0.0.jar # or mvn spring-boot:run
            ```
      * **For Docker:** As shown in the Docker run command above, you can pass it directly using the `-e` flag:
        ```bash
        docker run -p 8080:8080 -e GROQ_API_KEY=your_groq_api_key no-more-cringe
        ```

-----
