pipeline {
    agent any
    stages {
        stage('Pre') { 
            steps {
                sh 'docker container stop my-jenkins'
                sh 'docker container prune --force my-jenkins'
            }
        }

        stage('Build') { 
            steps {
                // sh 'export A=$(lsof -t -i:8082)'
                sh 'mvn -B -DskipTests clean install' 
                sh 'docker build -t myjenkins .'
                sh 'docker container run -d -p 8082:8082 --name my-jenkins myjenkins'
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