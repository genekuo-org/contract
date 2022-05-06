docker kill demo
docker rm demo
docker run -d --name demo \
  -p 80:8080 \
  genedocker/contract

sleep 5
curl  http://127.0.0.1/create
