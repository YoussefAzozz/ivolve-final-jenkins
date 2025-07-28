def call(String imageName, String awsRegion, String ecrRepoUri) {
    sh """
        # Authenticate Docker with ECR using instance profile
        aws ecr get-login-password --region ${awsRegion} | docker login --username AWS --password-stdin ${ecrRepoUri}

        # Build the Docker image
        docker build -t ${imageName}:${BUILD_NUMBER} .

        # Tag it with ECR URI
        docker tag ${imageName}:${BUILD_NUMBER} ${ecrRepoUri}:${BUILD_NUMBER}

        # Push to ECR
        docker push ${ecrRepoUri}:${BUILD_NUMBER}
    """
}
