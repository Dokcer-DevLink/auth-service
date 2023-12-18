pipeline {
    agent any

    environment {
        // Define environment variables.
        dockerHubRegistry = 'lordofkangs/k8s'
        DOCKERHUB_CREDENTIALS = 'dockerhub' // Replace with your Jenkins credentials ID for DockerHub..
        //IMAGE_NAME = 'digitaltulbo/jenkins-cicd' // Your DockerHub repository name.
        IMAGE_NAME = 'lordofkangs/auth-service' // Your DockerHub repository name
        IMAGE_TAG = 'tagname' // Replace with your desired tag name, or use dynamic values like ${BUILD_NUMBER}
        REGISTRY = 'docker.io' // DockerHub registry
        githubCredential = 'digitaltulbo'

    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the Git repository
                checkout scm
            }
        }
        stage('Spring APP Build') {
            steps {
                // sh './gradlew clean build --warning-mode all' 
                sh './gradlew bootJar'
                
                // Build your Java application (e.g., using Maven or Gradle)
                // Example: sh 'mvn clean package'
            }
        }

        stage('Docker Image Build') {
            steps {
                // Build Docker image with tag
                //sh "docker build -t $REGISTRY/$IMAGE_NAME:$IMAGE_TAG ."
                sh "docker build . -t ${dockerHubRegistry}:${currentBuild.number}"
                sh "docker build . -t ${dockerHubRegistry}:latest"
            }
        }

        stage('Docker Image Push') {
            steps {
                //script {
                    // Login to DockerHub
                    withDockerRegistry([ credentialsId: DOCKERHUB_CREDENTIALS, url: "" ]){
                    sh "docker push ${dockerHubRegistry}:${currentBuild.number}"
                    sh "docker push ${dockerHubRegistry}:latest"

                    sleep 10 /* Wait uploading */                    
                }
            }
            post {
                failure {
                  echo 'Docker Image Push failure !'
                  sh "docker rmi ${dockerHubRegistry}:${currentBuild.number}"
                  sh "docker rmi ${dockerHubRegistry}:latest"
                }
                success {
                    echo 'Docker image push success !'
                    sh "docker rmi ${dockerHubRegistry}:${currentBuild.number}"
                    sh "docker rmi ${dockerHubRegistry}:latest"

                }
            }
        }
    
stage('K8S Manifest Update') {
            steps {
                sh "ls"
                sh 'mkdir -p gitOpsRepo'
                dir('gitOpsRepo') {
                    git branch: 'main',
                        credentialsId: 'githubCredential',
                        url: 'https://github.com/Dokcer-DevLink/DevOps.git'
                    
                    sh "sed -i 's/k8s:.*\$/k8s:${BUILD_NUMBER}/' deployment.yaml"
                    sh "git add deployment.yaml"
                    sh "git commit -m '[UPDATE] k8s ${BUILD_NUMBER} image versioning'"
                    withCredentials([usernamePassword(credentialsId: 'githubCredential', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
                        sh "git remote set-url origin https://$GIT_USERNAME:$GIT_PASSWORD@github.com/Dokcer-DevLink/DevOps.git/manifests"
                        sh "git push -u origin main"
                    }
                }
            }
            post {
                failure {
                    echo 'K8S Manifest Update failure !'
                }
                success {
                    echo 'K8S Manifest Update success !'
                }
            }
        }
    }
    
    post {
        always {
            // Logout from DockerHub
            sh "docker logout"
        }
    }
}