pipeline {
    agent any

    environment {
        // Define environment variables
        //DOCKERHUB_CREDENTIALS = credentials('dockerhub') // Replace with your Jenkins credentials ID for DockerHub
        DOCKERHUB_CREDENTIALS = 'dockerhub' // Replace with your Jenkins credentials ID for DockerHub..
        //IMAGE_NAME = 'digitaltulbo/jenkins-cicd' // Your DockerHub repository name
        IMAGE_NAME = 'lordofkangs/jenkins-cicd' // Your DockerHub repository name
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
                //script {
                    // Login to DockerHub
                    withDockerRegistry([ credentialsId: DOCKERHUB_CREDENTIALS, url: "" ]){
                    //withCredentials([usernamePassword(credentialsId: DOCKERHUB_CREDENTIALS, usernameVariable: 'DOCKERHUB_USER', passwordVariable: 'DOCKERHUB_PASS')]) {
                        
                        sh "echo $DOCKERHUB_PASS | docker login $REGISTRY -u $DOCKERHUB_USER --password-stdin"
                        
                        // Build Docker image with tag
                        //sh "docker build -t $REGISTRY/$IMAGE_NAME:$IMAGE_TAG ."
                        sh "docker build -t $IMAGE_NAME:$IMAGE_TAG ."

                        // Push the image to DockerHub
                        //sh "docker push $REGISTRY/$IMAGE_NAME:$IMAGE_TAG"
                        sh "docker push $IMAGE_NAME:$IMAGE_TAG"

                  //  }
                }
            }
        }
    }

    // post {
    //     always {
    //         // Logout from DockerHub
    //         sh "docker logout $REGISTRY"
    //     }
    // }
}
