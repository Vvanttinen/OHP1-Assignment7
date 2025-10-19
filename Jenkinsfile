pipeline {
    agent any
    tools{
        maven 'Maven3'
    }

    environment {
        PATH = "C:\\Program Files\\Docker\\Docker\\resources\\bin;${env.PATH}"
        DOCKERHUB_CREDENTIALS_ID = 'Docker_Hub'
        DOCKER_IMAGE = 'vvanttinen/ohp1-assignment7'
        DOCKER_TAG = 'latest'
    }

    stages {
        stage('Setup Maven') {
            steps {
                script {
                    def mvnHome = tool name: 'Maven3', type: 'maven'
                    env.PATH = "${mvnHome}/bin:${env.PATH}"
                }
            }
        }

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Vvanttinen/OHP1-Assignment7.git'
            }
        }

        stage('Build') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn clean package -DskipTests'
                    } else {
                        bat 'mvn clean package -DskipTests'
                    }
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn test'
                    } else {
                        bat 'mvn test'
                    }
                }
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', env.DOCKERHUB_CREDENTIALS_ID) {
                        def image = docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                        image.push()
                    }
                }
            }
        }
    }

  post {
    always {
        junit(testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true)
        jacoco(execPattern: '**/target/jacoco.exec', classPattern: '**/target/classes', sourcePattern: '**/src/main/java', inclusionPattern: '**/*.class', exclusionPattern: '')
    }
  }
}
