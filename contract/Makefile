# Build parameters
OUT?=./target
DOCKER_TMP?=$(OUT)/docker_temp/
DOCKER_TAG?=genedocker/contract:latest
BINARY_NAME?=$(OUT)/contract-0.0.1-SNAPSHOT.jar

docker-build: build
	rm -rf $(DOCKER_TMP)
	mkdir $(DOCKER_TMP)
	cp $(BINARY_NAME) $(DOCKER_TMP)
	cp ./Dockerfile $(DOCKER_TMP)Dockerfile
	cp ./pom.xml $(DOCKER_TMP)pom.xml
	docker build -t $(DOCKER_TAG) $(DOCKER_TMP)
	rm -rf $(DOCKER_TMP)
build:
	mvn package
docker-push: docker-build
	docker push $(DOCKER_TAG)
clean:
	rm -rf $(OUT)

.PHONY: build docker-build docker-push clean
