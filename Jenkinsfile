pipeline {
    agent any
    stages {
        stage('Pre') { 
            steps {
                sh 'bash ./pre.sh' 
            }
        }
        stage('Build') { 
            steps {
                sh 'mvn -B -DskipTests clean package' 
            }
        }
        stage('Deliver') { 
            steps {
                // sh 'cd ./target'
                // sh 'pwd'
                sh 'java -jar ./target/api-0.0.1-SNAPSHOT.jar &' 
            }
        }
        stage('Post') { 
            steps {
                sh 'bash ./post.sh' 
            }
        }
    }
}