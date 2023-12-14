pipeline {
    agent any

    environment {
        imagename = "digitaltulbo/jenkins-cicd"
        registryCredential = 'dockerhub'
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
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
