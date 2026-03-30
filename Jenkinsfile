pipeline {
    agent any

    environment {
        IMAGE_NAME = 'fuelcons'
        IMAGE_TAG = "${BUILD_NUMBER}"
    }

    tools {
        jdk 'jdk21'
        maven 'maven3'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                sh 'mvn -B clean verify'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .'
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
        }
        success {
            echo "Build completed. Docker image: ${IMAGE_NAME}:${IMAGE_TAG}"
        }
    }
}
