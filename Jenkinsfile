pipeline {
    agent any

    stages {
        stage("Environment") {
            steps {
                sh "apt-get install node"
                sh "node -v"
                sh "npm -v"
            }
        }
        stage("Build") {
            steps {
                sh "npm install && npm run build"
            }
        }
    }
}
