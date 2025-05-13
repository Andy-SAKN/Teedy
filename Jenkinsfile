pipeline {
    agent any

    environment {
        // Jenkins 中设置的 Docker Hub 凭据 ID
        DOCKER_HUB_CREDENTIALS = credentials('dockerhub_credentials')

        // Docker Hub 镜像名（格式：用户名/仓库）
        DOCKER_IMAGE = 'sakn959/teedy'

        // 使用 Jenkins 的构建编号作为 tag
        DOCKER_TAG = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Build') {
            steps {
                // 克隆 Git 仓库并编译 WAR 文件
                checkout scmGit(
                    branches: [[name: '*/b-12212251']], // ← 指定你的分支
                    extensions: [],
                    userRemoteConfigs: [[url: 'https://github.com/Andy-SAKN/Teedy.git']]
                )
                sh 'mvn -B -DskipTests clean package'
            }
        }

        stage('Building image') {
            steps {
                script {
                    docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                }
            }
        }

        stage('Upload image') {
            steps {
                script {
                    sh """
                        echo 'Csdn959926' | docker login -u 'sakn959' --password-stdin
                        docker push sakn959/teedy:${DOCKER_TAG}
                        docker tag sakn959/teedy:${DOCKER_TAG} sakn959/teedy:latest
                        docker push sakn959/teedy:latest
                    """
                }
            }
        }


        stage('Run containers') {
            steps {
                script {
                    def ports = [8082, 8083, 8084]
                    for (port in ports) {
                        sh "docker stop teedy-container-${port} || true"
                        sh "docker rm teedy-container-${port} || true"
                        docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").run(
                            "--name teedy-container-${port} -d -p ${port}:8080"
                        )
                    }
                    // 输出运行中的容器
                    sh 'docker ps --filter "name=teedy-container"'
                }
            }
        }
    }
}
