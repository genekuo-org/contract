# EShop demo application

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
git push gogs
```

## find the server URL:
```
kubectl get svc gogs -o jsonpath='http://{.status.loadBalancer.ingress[0].*}:3000'
```

## browse and sign in with the same kiamol credentials