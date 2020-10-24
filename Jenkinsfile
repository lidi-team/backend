pipeline {
    agent any
    //  tools {
    //     maven 'M3'
    // }
    stages {
        stage('Pre') { 
            steps {
                sh 'bash ./pre.sh'
            }
        }

        stage('Build') { 
            steps {
                // sh 'export A=$(lsof -t -i:8082)'
                sh 'mvn clean package' 
                sh 'docker build -t backend .'
                sh 'docker container run -d -p 8081:8080 --name my-backend backend'
            }
        }
        // stage('Deliver') { 
        //     steps {
        //         // sh 'cd ./target'
        //         // sh 'pwd'
        //         // sh 'java -jar ./target/api-0.0.1-SNAPSHOT.jar' 
        //     }
        // }
    }
    // post {
    //     always {
    //         cleanWs()
    //     }
    // }
}