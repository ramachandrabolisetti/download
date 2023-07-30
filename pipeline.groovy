pipeline {
    agent any
    environment {
        EXAMPLE_CREDS = credentials('user1')
    }
    stages {
        stage('Build') {
            steps {
                // Clean before build
                cleanWs()
                sh '''
                git clone -b $BRANCH $RELENG_REPO 
                $WORKSPACE/java_releng/download_testing/jenkins_download_testing.sh $BUILD_VERSION $BUILD_LEVEL $JIM_URL $PLATFORM $WORKSPACE ${EXAMPLE_CREDS_USR} ${EXAMPLE_CREDS_PSW}
                '''
            }
        }
    }
    post {
        // Clean after build
        always {
            cleanWs(cleanWhenNotBuilt: false,
                    deleteDirs: true,
                    disableDeferredWipeout: true,
                    notFailBuild: true,
                    patterns: [[pattern: '.gitignore', type: 'INCLUDE'],
                               [pattern: '.propsfile', type: 'EXCLUDE']])
        }
    }
}
