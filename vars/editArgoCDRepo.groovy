def call() {
    script {
        sh '''
            rm -rf argocd-repo
            git clone https://github.com/YoussefAzozz/argocd-ivolve-final.git argocd-repo
        '''

        // Update replicas
        sh '''
            sed -i 's/replicas: [0-9]\\+/replicas: 4/' argocd-repo/overlays/prod/patch-deployment.yml
        '''

        // Update image tag
        sh """
            sed -i 's/newTag: .*/newTag: "${BUILD_NUMBER}"/' argocd-repo/overlays/prod/kustomization.yml
        """

        dir('argocd-repo') {
            withCredentials([usernamePassword(credentialsId: 'git-creds', usernameVariable: 'GIT_USER', passwordVariable: 'GIT_PASS')]) {
                sh '''#!/bin/bash
                    set -e
                    git config user.email "jenkins@example.com"
                    git config user.name "Jenkins CI"

                    git add overlays/prod/patch-deployment.yml overlays/prod/kustomization.yml

                    if git diff --cached --quiet; then
                        echo "No changes to commit"
                    else
                        git commit -m "Update image tag to ${BUILD_NUMBER} and replicas to 4"
                        git push https://${GIT_USER}:${GIT_PASS}@github.com/YoussefAzozz/argocd-ivolve-final.git HEAD:master
                    fi
                '''
            }
        }
    }
}
