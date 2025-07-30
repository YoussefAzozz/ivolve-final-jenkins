def call() {
    script {
        sh """
            rm -rf argocd-repo
            git clone https://github.com/YoussefAzozz/argocd-ivolve-final.git argocd-repo
        """

        // Update replicas
        sh """
            sed -i 's/replicas: [0-9]\\+/replicas: 4/' argocd-repo/overlays/prod/patch-deployment.yml
        """

        // Update image tag
        sh """
            sed -i 's/newTag: .*/newTag: "${BUILD_NUMBER}"/' argocd-repo/overlays/prod/kustomization.yml
        """

        // Commit and push
        dir('argocd-repo') {
            withCredentials([usernamePassword(credentialsId: 'github-creds', usernameVariable: 'GIT_USER', passwordVariable: 'GIT_PASS')]) {
                sh """
                    git config user.email "jenkins@example.com"
                    git config user.name "Jenkins CI"
                    git add .

                    if git diff --quiet; then
                        echo "No changes to commit"
                    else
                        git commit -m "Update image tag to ${BUILD_NUMBER} and replicas to 4"
                        git push https://${GIT_USER}:${GIT_PASS}@github.com/YoussefAzozz/argocd-ivolve-final.git HEAD:main
                    fi
                """
            }
        }
    }
}
