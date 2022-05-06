# Contract application

## How to build source code?

```bash
make
```

## How to run this demo?

```bash
docker run -d --name demo \
  -p 80:8080 \
  genedocker/contract
```

## Test the demo

Use curl command or open url ```127.0.0.1/create``` in the browser. You should be able to see output as the followings.

```bash
➜  milestone1 git:(master) ✗ curl 127.0.0.1/create
You have successfully checked out your shopping cart.
```

## switch to this folder in the source code:
```
cd template
```

## build the app:
```
docker-compose -f contract/docker-compose.yml build
```

## run the app:
```
docker-compose -f contract/docker-compose.yml up -d
```

## check the running  containers:
```
docker ps
```

## browse to the app at http://localhost:8010/create

## stop the app in Compose:
```
docker-compose -f contract/docker-compose.yml down
```

## deploy in Kubernetes:
```
kubectl apply -f contract/kubernetes/
```

## get the new URL:
```
kubectl get svc contract -o jsonpath='http://{.status.loadBalancer.ingress[0].*}:8011'
```

## browse

## Change source code and recompile
```
cd contract
mvn clean package
```

## rebuild and redelpoy
```
cd ..
docker-compose -f contract/docker-compose.yml build
```

## Redeploy
```
kubectl apply -f contract/kubernetes/
```

## delete the existing Pod to recreate it:
```
kubectl delete pod -l app=contract
```

## deploy the Git server:
```
kubectl apply -f infrastructure/gogs.yaml
```

## wait for it to spin up:
```
kubectl wait --for=condition=ContainersReady pod -l app=gogs
```

## Create a private repository from browser http://localhost:3000/kiamol
```
contract
```

## add your local Git server to the repository--
## this grabs the URL from the Service to use as the target:
```
git remote add gogs $(kubectl get svc gogs -o jsonpath='http://{.status.loadBalancer.ingress[0].*}:3000/kiamol/contract.git')
```

## push the code to your server--authenticate with 
## username kiamol and password kiamol 
```
git push --set-upstream gogs master
```

## find the server URL:
```
kubectl get svc gogs -o jsonpath='http://{.status.loadBalancer.ingress[0].*}:3000'
```

## browse and sign in with the same kiamol credentials

## deploy BuildKit:
```
kubectl apply -f infrastructure/buildkitd.yaml
```

## wait for it to spin up:
```
kubectl wait --for=condition=ContainersReady pod -l app=buildkitd
```

## verify that Git and BuildKit are available:
```
kubectl exec deploy/buildkitd -- sh -c 'git version && buildctl --version'
```

## check that Docker isn’t installed--this command will fail:
```
kubectl exec deploy/buildkitd -- sh -c 'docker version'
```

## connect to a session on the BuildKit Pod:
```
kubectl exec -it deploy/buildkitd -- sh
```

## clone the source code from your Gogs server:
```
cd ~
git clone http://gogs:3000/kiamol/contract.git
```

## switch to the app directory:

```
cd contract/contract/
```

## build the app using BuildKit; the options tell BuildKit
## to use Buildpacks instead of a Dockerfile as input and to 
## produce an image as the output:
```
buildctl build \
 --frontend=dockerfile.v0 \
 --local context=. \
 --local dockerfile=. \
 --output type=image,name=name=genedocker/contract:buildkit
```

## leave the session when the build completes
```
exit
```

## create a new namespace:
kubectl create namespace contract

## collect the details--on Windows: 
```
. .\set-registry-variables.ps1
```

## OR on Linux/Mac:
```. ./set-registry-variables.sh
```

## create the Secret using the details from the script:
```
kubectl create secret docker-registry registry-creds --docker-server=$REGISTRY_SERVER --docker-username=$REGISTRY_USER --docker-password=$REGISTRY_PASSWORD
```

## show the Secret details:
```
kubectl get secret registry-creds
```

## deploy Jenkins:
kubectl apply -f infrastructure/jenkins.yaml

## wait for the Pod to spin up:
kubectl wait --for=condition=ContainersReady pod -l app=jenkins

## check that kubectl can connect to the cluster:
kubectl exec deploy/jenkins -- sh -c 'kubectl version --short'

## check that the registry Secret is mounted:
kubectl exec deploy/jenkins -- sh -c 'ls -l /root/.docker'

## get the URL for Jenkins:
kubectl get svc jenkins -o jsonpath='http://{.status.loadBalancer.ingress[0].*}:8080'

## browse and login with username kiamol and password kiamol; 
## if Jenkins is still setting itself up you’ll see a wait screen

## click enable for the Kiamol job and wait . . .

## when the pipeline completes, check the deployment:
kubectl get pods -n contract -l app.kubernetes.io/name=contract -o=custom-columns=NAME:.metadata.name,IMAGE:.spec.containers[0].image
## find the URL of the test app:
kubectl get svc -n contract contract -o jsonpath='http://{.status.loadBalancer.ingress[0].*}:8012/create'

## browse

## add your code change, and push it to Git:
git add -A .
git commit -m 'Ad descriptions'
git push gogs

# browse back to Jenkins, and wait for the new build to finish

# check that the application Pod is using the new image version:
kubectl get pods -n kiamol-ch11-test -l app.kubernetes.io/name=contract -o=custom-columns=NAME:.metadata.name,IMAGE:.spec.containers[0].image

# browse back to the app