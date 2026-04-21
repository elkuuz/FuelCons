pipeline {
    agent any

    environment {
        IMAGE_NAME = 'fuelcons'
        DOCKERHUB_NAMESPACE = 'elkuuz'
        DOCKERHUB_CREDENTIALS_ID = 'dockerhub-creds'
        PATH = "/opt/homebrew/bin:/usr/local/bin:${env.PATH}"
        SONARQUBE_SERVER = 'SonarQubeServer'
        SONAR_PROJECT_KEY = 'Fuel_Cons'
        SONAR_PROJECT_NAME = 'Fuel_Cons'
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
                sh 'docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} .'
            }
        }

        stage('Push Docker Hub Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${DOCKERHUB_CREDENTIALS_ID}", usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_TOKEN')]) {
                    sh '''#!/bin/zsh
                        set -e
                        DOCKERHUB_REPOSITORY="${DOCKERHUB_NAMESPACE}/${IMAGE_NAME}"
                        echo "Pushing tags: ${DOCKERHUB_REPOSITORY}:${BUILD_NUMBER} and ${DOCKERHUB_REPOSITORY}:latest"
                        echo "$DOCKERHUB_TOKEN" | docker login -u "$DOCKERHUB_USERNAME" --password-stdin
                        docker tag "${IMAGE_NAME}:${BUILD_NUMBER}" "${DOCKERHUB_REPOSITORY}:${BUILD_NUMBER}"
                        docker tag "${IMAGE_NAME}:${BUILD_NUMBER}" "${DOCKERHUB_REPOSITORY}:latest"
                        docker push "${DOCKERHUB_REPOSITORY}:${BUILD_NUMBER}"
                        docker push "${DOCKERHUB_REPOSITORY}:latest"
                        docker logout
                    '''
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
        }
        success {
            echo "Build completed. Docker image: ${IMAGE_NAME}:${BUILD_NUMBER}"
        }
    }
}

