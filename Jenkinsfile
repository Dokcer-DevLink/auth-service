pipeline {
    agent any

    environment {
        // Define environment variables
        DOCKERHUB_CREDENTIALS = credentials('dockerhub') // Replace with your Jenkins credentials ID for DockerHub
        IMAGE_NAME = 'digitaltulbo/jenkins-cicd' // Your DockerHub repository name
        IMAGE_TAG = 'latest' // Replace with your desired tag name, or use dynamic values like ${BUILD_NUMBER}
        REGISTRY = 'docker.io' // DockerHub registry
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the Git repository
                checkout scm
            }
        }
        stage('Build') {
            steps {
                sh './gradlew bootJar'
            }
        }
        stage('Docker Build and Push') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: DOCKERHUB_CREDENTIALS, usernameVariable: 'DOCKERHUB_USER', passwordVariable: 'DOCKERHUB_PASS')]) {
                        // Build Docker image
                        sh "docker build -t $IMAGE_NAME:$IMAGE_TAG ."

                        // Login to DockerHub
                        sh "echo $DOCKERHUB_PASS | docker login -u $DOCKERHUB_USER --password-stdin"

                        // Push the image to DockerHub
                        sh "docker push $IMAGE_NAME:$IMAGE_TAG"

                        // Logout from DockerHub
                        sh "docker logout"
                    }
                }
            }
        }
    }
}