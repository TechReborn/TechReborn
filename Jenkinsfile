pipeline {
   agent any
   stages {

      stage('Init') {
         steps {
            sh "rm -rf build/libs/"
            sh "chmod +x gradlew"
         }
      }

      stage ('Build') {
         when {
                branch '1.16'
         }
         steps {
            sh "./gradlew clean build publish curseTools --refresh-dependencies --stacktrace"

            archiveArtifacts artifacts: '**/build/libs/*.jar', fingerprint: true
         }
      }
   }
}
