FROM java:8-jdk
MAINTAINER Diego Pacheco - diego.pacheco.it@gmail.com

RUN apt-get update && apt-get install -y \
	autoconf \
	build-essential \
	dh-autoreconf \
	git \
	libssl-dev \
	libtool \
	python-software-properties \
	redis-server \
	tcl8.5 \
	dos2unix \ 
	unzip

RUN git clone https://github.com/Netflix/dynomite.git

RUN cd /dynomite/ && git checkout tags/v0.5.6 

ADD redis.conf /etc/redis/
ADD start.sh /usr/local/dynomite/
COPY dynomite_florida.yml /dynomite/conf/dynomite_florida.yml

RUN chmod 777 /usr/local/dynomite/start.sh

WORKDIR /dynomite/

RUN autoreconf -fvi \
	&& ./configure --enable-debug=log \
	&& CFLAGS="-ggdb3 -O0" ./configure --enable-debug=log \
	&& make \
	&& make install

EXPOSE 8101
EXPOSE 6379
EXPOSE 22222
EXPOSE 8102

CMD ["/usr/local/dynomite/start.sh"]

#
# To gen the config: 
#    python generate_yamls.py 172.18.0.101:rack1 172.18.0.102:rack2 172.18.0.103:rack3
#
# To clean up building env
#
# docker stop $(docker ps -a -q)
# docker rm `docker ps -aq`
# docker rmi -f $(docker images | grep "<none>" | awk "{print \$3}")
#
#
# to Build: $ docker build -t diegopacheco/dynomite .
# Network : $ docker network create --subnet=172.18.0.0/16 myDockerNetDynomite
# to Run  : 
# $ docker run -d --net myDockerNetDynomite --ip 172.18.0.101 --name dynomite1 -e DYNOMITE_RACK=rack1 -e DYNOMITE_TOKENS=2093391979 diegopacheco/dynomite
# $ docker run -d --net myDockerNetDynomite --ip 172.18.0.102 --name dynomite2 -e DYNOMITE_RACK=rack2 -e DYNOMITE_TOKENS=4203913991 diegopacheco/dynomite
# $ docker run -d --net myDockerNetDynomite --ip 172.18.0.103 --name dynomite3 -e DYNOMITE_RACK=rack3 -e DYNOMITE_TOKENS=2975898557 diegopacheco/dynomite
# 
#
# $ docker ps
# $ docker exec -i -t dynomite1 bash
# $ ps aux | grep redis
# $ cat /var/log/dynomite/dynomite_log.txt
#
# $ docker ps
# $ docker exec -i -t dynomite2 bash
# $ ps aux | grep redis
# $ cat /var/log/dynomite/dynomite_log.txt
#
# $ docker ps
# $ docker exec -i -t dynomite3 bash
# $ ps aux | grep redis
# $ cat /var/log/dynomite/dynomite_log.txt
#
# $ ./gradlew build run