pipeline {
    agent any
    stages {
        stage('Build') { 
            steps {
                sh 'mvn -B -DskipTests clean package' 
            }
        }
        stage('Deliver') { 
            steps {
                sh 'cd ./target'
                sh 'java -jar api-0.0.1-SNAPSHOT.jar' 
            }
        }
    }
}