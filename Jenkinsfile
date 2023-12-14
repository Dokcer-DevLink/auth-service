pipeline {
    agent any

    environment {
        // Define environment variables
        DOCKERHUB_CREDENTIALS = credentials('dockerhub') // Replace with your Jenkins credentials ID for DockerHub
        IMAGE_NAME = 'digitaltulbo/jenkins-cicd' // Your DockerHub repository name
        IMAGE_TAG = 'tagname' // Replace with your desired tag name, or use dynamic values like ${BUILD_NUMBER}
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
                // sh './gradlew clean build --warning-mode all' 
                sh './gradlew bootJar'
                
                // Build your Java application (e.g., using Maven or Gradle)
                // Example: sh 'mvn clean package'
            }
        }
        stage('Docker Build and Push') {
            steps {
                sh 'docker build -t digitaltulbo/jenkins-cicd .' 
                    }
                }
        stage('Login') {
            steps {
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                }
            }
        stage('Push') {
      steps {
        sh 'docker push digitaltulbo/jenkins-cicd'
        }
    }


    post {
        always {
            // Logout from DockerHub
            sh "docker logout $REGISTRY"
        }
    }
}
