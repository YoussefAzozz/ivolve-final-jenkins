def call(String newTag = "${env.BUILD_NUMBER}", int replicas = 4) {
    script {
        sh """
            rm -rf argocd-repo
            git clone https://github.com/YoussefAzozz/argocd-ivolve-final.git argocd-repo
        """

        // Update replicas
        sh """
            sed -i 's/replicas: [0-9]\\+/replicas: ${replicas}/' argocd-repo/overlays/prod/patch-deployment.yml
        """

        // Update image tag
        sh """
            sed -i 's/newTag: .*/newTag: "${newTag}"/' argocd-repo/overlays/prod/kustomization.yml
        """

        // Commit and push
        dir('argocd-repo') {
            withCredentials([usernamePassword(credentialsId: 'github-creds', usernameVariable: 'GIT_USER', passwordVariable: 'GIT_PASS')]) {
                sh """
                    git config user.email "jenkins@example.com"
                    git config user.name "Jenkins CI"
                    git add .
                    git commit -m "Update image tag to ${newTag} and replicas to ${replicas}" || echo "No changes to commit"
                    git push https://${GIT_USER}:${GIT_PASS}@github.com/YoussefAzozz/argocd-ivolve-final.git HEAD:main
                """
            }
        }
    }
}
