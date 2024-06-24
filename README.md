# Aclepius: Cancer Detection Android App

Aclepius is a cancer detection Android app developed in Kotlin, integrating machine learning to detect cancer using TensorFlow Lite (tflite).

## Screenshots

<div align="center">
  <img src="https://github.com/Chlunidia/asclepius/assets/115222445/abbc450f-1093-420b-9eff-194f77333069" alt="App Screenshot">
</div>

## Features

1. Begin with the provided starter project template by Dicoding.
2. Includes a feature to capture images from the gallery and display a preview using Intent Gallery.
3. Uses a pre-trained cancer classification model from Dicoding.
4. Predicts images using TensorFlow Lite for machine learning model inference.
5. Displays prediction results in `ResultActivity`, including whether the image indicates cancer and the confidence score.
6. Use the uCrop library to crop and rotate images before processing.
7. Save prediction history using a local database.
8. Display relevant cancer information using the Indonesia Health News API.

## Usage

### Getting Started

1. **Clone the Repository**:
    ```sh
    git clone https://github.com/Chlunidia/asclepius.git
    ```

2. **Open in Android Studio**: Sync the project with Gradle files.

### Integrating Health News API

To provide users with relevant cancer-related information, integrate the Indonesia Health News API.

- **API Endpoint**:
    ```
    https://newsapi.org/v2/top-headlines?q=cancer&category=health&language=en&apiKey=[YOUR_API_KEY]
    ```

- **Display Articles**: Fetch and display articles related to cancer, including the title, description, and image. Use Retrofit to handle API requests and GSON for JSON parsing.
