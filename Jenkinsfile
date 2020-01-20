pipeline {
    agent any

    stages {
        stage("Environment") {
            steps {
                sh "node -v"
                sh "npm -v"
            }
        }
        stage("Build") {
            steps {
                sh "rm -rf node_modules"
                sh "npm install && npm run build"
            }
        }
    }
}
