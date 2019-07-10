#!/usr/bin/env groovy

@Library('jenkins-shared-library') _

releaseBranchRegex = /release|master/
pipeline {
    agent { node { label 'build' } }
    options { disableConcurrentBuilds() }
    environment{
      CREDENTIALS_GITHUB = 'jenkins-github'
    }

    stages {
        //skip pushing artifcat when maven release plugin checks in code.
        stage('skip release build') {
			when { changelog '.*\\[maven-release-plugin\\].*'}
			steps {
				script {
					pom = readMavenPom file: 'javasdk/pom.xml'
					currentBuild.displayName = pom.version
					currentBuild.result = 'NOT_BUILT'
				}
			error('Skipping release build')
			}
        }
        stage('Checkout project...') {
            steps {
                checkout([
                     $class: 'GitSCM',
                     branches: [[name: '*/master']],
                     userRemoteConfigs: scm.userRemoteConfigs
                 ])
            }
        }

        stage('Versioning') {
			steps {
			   script {
				   pom = readMavenPom file: 'pom.xml'
				   pomVersion = pom.version.substring(0, pom.version.lastIndexOf("-SNAPSHOT"))
				   withPullRequestBranch {
						pomVersion = "$pomVersion-PR${env.CHANGE_ID}-SNAPSHOT"
				   }
				   echo 'Setting build version...'
				   sh "./mvnw versions:set -DnewVersion=${pomVersion} -f 'pom.xml' | grep -v 'Props:'"
				   env.VERSION = "$pomVersion"
			   }
			}
		}

		stage('Building') {
			steps {
				script{
					sh "mvn clean package"
				}
			}
		}

		stage('Push Artifact') {
			steps {
				script{
					sh "mvn clean package"

					def server = Artifactory.server 'artifactory-appdirect'
					def rtMaven = Artifactory.newMavenBuild()
					rtMaven.deployer server: server, releaseRepo: 'libs-release-local', snapshotRepo: 'libs-snapshot-local'
					rtMaven.tool = 'M3'
					def buildInfo = rtMaven.run pom: 'pom.xml', goals: 'clean install'
				}
			}
		}

		stage('Release') {
			when {
				expression {
					env.BRANCH_NAME =~ releaseBranchRegex
				}
			}
			steps {

				echo 'Increasing version...'
				sshagent(credentials: [CREDENTIALS_GITHUB]) {
					sh "mvn versions:revert"
					sh "mvn -B release:clean -DpreparationGoals='' release:prepare -DtagNameFormat='@{project.version}' -Dgoals='' release:perform"
				}
			}
		}
	}
}
