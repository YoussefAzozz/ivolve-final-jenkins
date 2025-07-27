def call(String imageName, String credentialsId, String image_environment) {
    withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
        sh """
            docker build -t ${imageName}:${image_environment}${BUILD_NUMBER} .
            echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
            docker push ${imageName}:${image_environment}${BUILD_NUMBER}
        """
    }
}
