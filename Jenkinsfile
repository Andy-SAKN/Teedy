pipeline {
    agent any
    environment {
        DEPLOYMENT_NAME = "hello-node"  // 替换为你的部署名称
        CONTAINER_NAME = "teedy"       // 替换为你的容器名称
        IMAGE_NAME = "sakn959/teedy:latest"  // 替换为你的镜像名称
    }
    stages {
        stage('Start Minikube') {
            steps {
                sh '''
                if ! minikube status | grep -q "Running"; then
                    echo "Starting Minikube..."
                    minikube start
                else
                    echo "Minikube already running."
                fi
                '''
            }
        }
        stage('Set Image') {
            steps {
                sh '''
                echo "Setting image for deployment..."
                kubectl set image deployment/${DEPLOYMENT_NAME} ${CONTAINER_NAME}=${IMAGE_NAME}
                '''
            }
        }
        stage('Verify') {
            steps {
                sh 'kubectl rollout status deployment/${DEPLOYMENT_NAME}'
                sh 'kubectl get pods'
            }
        }
    }
}
