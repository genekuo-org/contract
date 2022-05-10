# Contract application

## How to build source code?

```bash
make
```

## How to run this demo?

```bash
docker run -d --name demo \
  -p 8080:8080 \
  genedocker/contract
```

## Functional tests without Docker
```
mvn clean package
java -jar target/contract-0.0.1-SNAPSHOT.jar --server.port=8888
./test.sh
```

## Test the demo

Use curl command or open url ```127.0.0.1:8888/contract/1``` in the browser. You should be able to see output.

## Switch to this folder in the source code:
```
cd template
```

## Build the app:
```
docker-compose -f contract/docker-compose.yml build
```

## Run the app and test:
```
cd contract
./test.sh start stop
```

## Check the running  containers:
```
docker ps
```

## Deploy in Kubernetes:
```
kubectl apply -f contract/kubernetes/
```

## Get the new URL:
```
kubectl get svc contract -o jsonpath='http://{.status.loadBalancer.ingress[0].*}:8011'
```

## Browse

## Change source code and recompile
```
cd contract
mvn clean package
```

## Rebuild
```
cd ..
docker-compose -f contract/docker-compose.yml build
```

## Redeploy
```
kubectl apply -f contract/kubernetes/
```

## Delete the existing Pod to recreate it:
```
kubectl delete pod -l app=contract
```

## Deploy the Git server:
```
kubectl apply -f infrastructure/gogs.yaml
```

## Wait for it to spin up:
```
kubectl wait --for=condition=ContainersReady pod -l app=gogs
```

## Create a private repository from browser http://localhost:3000/kiamol
```
contract
```

## Add your local Git server to the repository--
## this grabs the URL from the Service to use as the target:
```
git remote add gogs $(kubectl get svc gogs -o jsonpath='http://{.status.loadBalancer.ingress[0].*}:3000/kiamol/contract.git')
```

## Push the code to your server--authenticate with 
## username kiamol and password kiamol 
```
git push --set-upstream gogs master
```

## Find the server URL:
```
kubectl get svc gogs -o jsonpath='http://{.status.loadBalancer.ingress[0].*}:3000'
```

## Browse and sign in with the same kiamol credentials

## Deploy BuildKit:
```
kubectl apply -f infrastructure/buildkitd.yaml
```

## Wait for it to spin up:
```
kubectl wait --for=condition=ContainersReady pod -l app=buildkitd
```

## Verify that Git and BuildKit are available:
```
kubectl exec deploy/buildkitd -- sh -c 'git version && buildctl --version'
```

## Check that Docker isn’t installed--this command will fail:
```
kubectl exec deploy/buildkitd -- sh -c 'docker version'
```

## Connect to a session on the BuildKit Pod:
```
kubectl exec -it deploy/buildkitd -- sh
```

## Clone the source code from your Gogs server:
```
cd ~
git clone http://gogs:3000/kiamol/contract.git
```

## Switch to the app directory:

```
cd contract/contract/
```

## Build the app using BuildKit; the options tell BuildKit
## to use Buildpacks instead of a Dockerfile as input and to 
## produce an image as the output:
```
buildctl build \
 --frontend=dockerfile.v0 \
 --local context=. \
 --local dockerfile=. \
 --output type=image,name=name=genedocker/contract:buildkit
```

## Leave the session when the build completes
```
exit
```

## Create a new namespace:
kubectl create namespace contract

## Collect the details--on Windows: 
```
. .\set-registry-variables.ps1
```

## OR on Linux/Mac:
```. ./set-registry-variables.sh
```

## Create the Secret using the details from the script:
```
kubectl create secret docker-registry registry-creds --docker-server=$REGISTRY_SERVER --docker-username=$REGISTRY_USER --docker-password=$REGISTRY_PASSWORD
```

## Show the Secret details:
```
kubectl get secret registry-creds
```

## Deploy Jenkins:
```
kubectl apply -f infrastructure/jenkins.yaml
```

## Wait for the Pod to spin up:
```
kubectl wait --for=condition=ContainersReady pod -l app=jenkins
```

## Check that kubectl can connect to the cluster:
```
kubectl exec deploy/jenkins -- sh -c 'kubectl version --short'
```

## Check that the registry Secret is mounted:
```
kubectl exec deploy/jenkins -- sh -c 'ls -l /root/.docker'
```

## Get the URL for Jenkins:
```
kubectl get svc jenkins -o jsonpath='http://{.status.loadBalancer.ingress[0].*}:8080'
```

## Browse and login with username kiamol and password kiamol; 
## if Jenkins is still setting itself up you’ll see a wait screen

## Click enable for the Kiamol job and wait . . .

## When the pipeline completes, check the deployment:
```
kubectl get pods -n contract -l app.kubernetes.io/name=contract -o=custom-columns=NAME:.metadata.name,IMAGE:.spec.containers[0].image
```

## Find the URL of the test app:
```
kubectl get svc -n contract contract -o jsonpath='http://{.status.loadBalancer.ingress[0].*}:8012/create'
```

## Browse

## Add your code change, and push it to Git:
```
git add -A .
git commit -m 'Ad descriptions'
git push gogs
```

# Browse back to Jenkins, and wait for the new build to finish

# Check that the application Pod is using the new image version:
```
kubectl get pods -n contract -l app.kubernetes.io/name=contract -o=custom-columns=NAME:.metadata.name,IMAGE:.spec.containers[0].image
```

# Browse back to the app

```
kubectl exec -it deploy/jenkins -- sh
helm history contract --namespace contract
helm rollback contract 1 --namespace contract
```