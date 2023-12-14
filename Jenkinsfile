pipeline {
    agent any

     environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
        IMAGE_NAME = 'digitaltulbo/jenkins-cicd'
        IMAGE_TAG = "${BUILD_NUMBER}"
    }
    stages {
        stage('Checkout') {
            steps {
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
                        sh "echo $DOCKERHUB_PASS | docker login -u $DOCKERHUB_USER --password-stdin"
                        sh "docker build -t $IMAGE_NAME:$IMAGE_TAG ."
                        sh "docker push $IMAGE_NAME:$IMAGE_TAG"
                        sh "docker logout"
                    }
                }
            }
        }
    }

    post {
        always {
        }
    }
}