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
                sh 'docker rmi $(docker images -f "dangling=true" -q)'
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
    post {
        failure {
        mail to: 'dinhlehoang35@gmail.com, hoangledinh65@gmail.com, sontung199x@gmail.com',
             subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
             body: "Something is wrong with ${env.BUILD_URL}"
    }
        success {
            mail to: 'dinhlehoang35@gmail.com, hoangledinh65@gmail.com, sontung199x@gmail.com',
             subject: "Success notification from Jenkins!",
             body: "Success!"
        }
    }
}