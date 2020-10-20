pipeline {
    agent any
    stages {
        stage('Build') { 
            steps {
                sh 'sudo kill $(lsof -t -i:8082)'
                sh 'mvn -B -DskipTests clean install' 
            }
        }
        // stage('Deliver') { 
        //     steps {
        //         // sh 'cd ./target'
        //         // sh 'pwd'
        //         sh 'java -jar ./target/api-0.0.1-SNAPSHOT.jar' 
        //     }
        // }
    }
    // post {
    //     always {
    //         cleanWs()
    //     }
    // }
}