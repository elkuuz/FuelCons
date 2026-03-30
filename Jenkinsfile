pipeline {
    agent any

    environment {
        IMAGE_NAME = 'fuelcons'
        IMAGE_TAG = "${BUILD_NUMBER}"
        PATH = "/opt/homebrew/bin:/usr/local/bin:${env.PATH}"
    }

    tools {
        jdk 'JDK21'
        maven 'Maven'
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

        stage('Docker Preflight') {
            steps {
                sh '''#!/bin/zsh
                    set -e
                    echo "PATH=$PATH"
                    command -v docker
                    docker version
                '''
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
