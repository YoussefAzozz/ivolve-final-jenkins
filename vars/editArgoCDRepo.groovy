def call(String image_environment){
  withCredentials([usernamePassword(credentialsId: '743c416b-fd33-4994-be23-6c1599d6e213', usernameVariable: 'GIT_USER', passwordVariable: 'GIT_PASS')]) {
    sh '''
    git clone https://${GIT_USER}:${GIT_PASS}@github.com/YoussefAzozz/argocd-lab.git
    cd argocd-lab
    sed -i "s|image:.*|image: '$DOCKER_IMAGE:${image_environment}${BUILD_NUMBER}'|g" deployment.yml
    cat deployment.yml
    '''
}
}
