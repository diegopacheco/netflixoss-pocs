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
	tcl8.5 \
	dos2unix \ 
	unzip

RUN git clone https://github.com/diegopacheco/netflixoss-pocs

WORKDIR /netflixoss-pocs/NetflixDynomite-DEMO/

RUN /netflixoss-pocs/NetflixDynomite-DEMO/gradlew clean build

EXPOSE 8101
EXPOSE 6379
EXPOSE 22222
EXPOSE 8102

CMD ["/bin/bash"]

#
# to Build: $ docker build -t diegopacheco/dyno .
# to Run  : $ docker run --net myDockerNet --ip 172.18.0.200 -i -t diegopacheco/dyno bash
#
#
# $ ./gradlew build run
#
#