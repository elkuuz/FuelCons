pipeline {
    agent any

    environment {
        IMAGE_NAME = 'fuelcons'
        IMAGE_TAG = "${BUILD_NUMBER}"
        PATH = "/opt/homebrew/bin:/usr/local/bin:${env.PATH}"
        SONARQUBE_SERVER = 'SonarQubeServer'
        SONAR_PROJECT_KEY = 'Fuel_Cons'
        SONAR_PROJECT_NAME = 'Fuel_Cons'
    }

    tools {
        jdk 'JDK'

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

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv("${SONARQUBE_SERVER}") {
                    sh '''#!/bin/zsh
                        set -e
                        mvn -B sonar:sonar \
                            -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                            -Dsonar.projectName=${SONAR_PROJECT_NAME} \
                            -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    '''
                }
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
