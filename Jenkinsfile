#!/usr/bin/env groovy

@Library('jenkins-shared-library') _

releaseBranchRegex = /release|master/

def CREDENTIALS_ARTIFACTORY = 'jenkins-artifactory-credentials'
def CREDENTIALS_GITHUB = 'jenkins-github'
def MAVEN_CONFIGURATION_FILE = 'MavenArtifactorySettingsCustomFile'

pipeline {
    agent { node { label 'build' } }
    options { disableConcurrentBuilds() }

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
                     extensions: [[$class: 'LocalBranch', localBranch: "**"]],
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

		stage('Release') {
			when {
				expression {
					env.BRANCH_NAME =~ releaseBranchRegex
				}
			}
			steps {
				configFileProvider(
						[configFile(fileId: MAVEN_CONFIGURATION_FILE, variable: 'MAVEN_SETTINGS')]) {
					sh 'cp $MAVEN_SETTINGS mavenSettings'
				}
				withCredentials([
						[$class          : 'UsernamePasswordMultiBinding', credentialsId: CREDENTIALS_ARTIFACTORY,
						usernameVariable: 'ARTIFACTORY_USER',
						passwordVariable: 'ARTIFACTORY_PASSWORD']
				]) {
					echo 'Increasing version and publish...'
					sshagent(credentials: [CREDENTIALS_GITHUB]) {
						sh "mvn versions:revert"
						sh "mvn -s mavenSettings -B release:clean -DpreparationGoals='' release:prepare -DtagNameFormat='@{project.version}' -Dgoals='' release:perform -Darguments=\"-DARTIFACTORY_USER=$ARTIFACTORY_USER -DARTIFACTORY_PASSWORD=$ARTIFACTORY_PASSWORD\""
					}
				}
			}
		}
	}
}
