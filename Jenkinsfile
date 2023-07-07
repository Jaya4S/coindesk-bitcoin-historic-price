pipeline {
  agent any

  stages {
    stage('Checkout') {
      steps {
        git 'https://github.com/Jaya4S/bitcoin-historic-price.git'
      }
    }

    stage('Build') {
      steps {
        sh 'gradle clean build'
      }
    }

    stage('Test') {
      steps {
        sh 'gradle test'
      }
    }

    stage('Deploy') {
      steps {
        sh 'gradle bootRun'
      }
    }
  }
}
